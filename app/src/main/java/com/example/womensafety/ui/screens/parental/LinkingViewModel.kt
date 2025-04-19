package com.example.womensafety.ui.screens.parental

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LinkingViewModel(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val context: Context
) : ViewModel() {

    private val _linkStatus = MutableStateFlow<LinkStatus>(LinkStatus.Idle)
    val linkStatus: StateFlow<LinkStatus> = _linkStatus

    sealed class LinkStatus {
        object Idle : LinkStatus()
        object SendingOtp : LinkStatus()
        data class OtpSent(val phone: String) : LinkStatus()
        data class Error(val message: String) : LinkStatus()
        object LinkSuccess : LinkStatus()
    }

    fun initiateLink(childEmail: String, guardianPhone: String) {
        viewModelScope.launch {
            _linkStatus.value = LinkStatus.SendingOtp
            try {
                // 1. Verify guardian phone exists
                val guardianDoc = firestore.collection("phone_mappings")
                    .document(guardianPhone)
                    .get()
                    .await()

                if (!guardianDoc.exists()) {
                    _linkStatus.value = LinkStatus.Error("Phone number not registered")
                    return@launch
                }

                // 2. Generate and store OTP
                val otp = (100000..999999).random().toString()
                val otpData = mapOf(
                    "otp" to otp,
                    "childEmail" to childEmail,
                    "guardianPhone" to guardianPhone,
                    "expiresAt" to System.currentTimeMillis() + 300000 // 5 minutes
                )

                firestore.collection("linking_otps")
                    .document("$childEmail-$guardianPhone")
                    .set(otpData)
                    .await()

                // 3. Send OTP via email (since we can't use SMS)
                val guardianEmail = guardianDoc.getString("email") ?: ""
                sendEmailOtp(guardianEmail, otp)

                _linkStatus.value = LinkStatus.OtpSent(guardianPhone)
            } catch (e: Exception) {
                _linkStatus.value = LinkStatus.Error(e.message ?: "Linking failed")
            }
        }
    }

    fun verifyOtp(childEmail: String, guardianPhone: String, otp: String) {
        viewModelScope.launch {
            try {
                val doc = firestore.collection("linking_otps")
                    .document("$childEmail-$guardianPhone")
                    .get()
                    .await()

                if (!doc.exists()) {
                    _linkStatus.value = LinkStatus.Error("OTP expired")
                    return@launch
                }

                val storedOtp = doc.getString("otp")
                val expiresAt = doc.getLong("expiresAt") ?: 0

                if (storedOtp != otp || System.currentTimeMillis() > expiresAt) {
                    _linkStatus.value = LinkStatus.Error("Invalid OTP")
                    return@launch
                }

                // Create the parent-child relationship
                val currentUser = auth.currentUser?.uid ?: throw Exception("Not authenticated")

                firestore.collection("relationships")
                    .document("$currentUser-${doc.getString("guardianPhone")}")
                    .set(mapOf(
                        "childId" to currentUser,
                        "guardianPhone" to guardianPhone,
                        "createdAt" to System.currentTimeMillis()
                    ))
                    .await()

                // Update user roles
                firestore.collection("users").document(currentUser)
                    .update("role", "child")
                    .await()

                val guardianUserId = doc.getString("guardianUserId")
                guardianUserId?.let {
                    firestore.collection("users").document(it)
                        .update("role", "guardian")
                        .await()
                }

                _linkStatus.value = LinkStatus.LinkSuccess
            } catch (e: Exception) {
                _linkStatus.value = LinkStatus.Error(e.message ?: "Verification failed")
            }
        }
    }

    private fun sendEmailOtp(email: String, otp: String) {
        // Implement using Firebase Cloud Functions or EmailJS
        // This is a placeholder - you'll need to implement actual email sending
        Log.d("LinkingViewModel", "Sending OTP $otp to $email")
    }
}