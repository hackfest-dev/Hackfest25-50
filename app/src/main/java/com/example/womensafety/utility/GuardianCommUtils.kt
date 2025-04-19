package com.example.womensafety.utility

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.services.FireStoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.security.MessageDigest

object GuardianCommUtils {
    private val checkInResponses = MutableStateFlow<Map<String, Boolean?>>(emptyMap())
    private val locationResponses = MutableStateFlow<Map<String, String?>>(emptyMap())
    private val linkRequests = MutableStateFlow<Map<String, String>>(emptyMap()) // phone -> email
    private val bargeInResponses = MutableStateFlow<Map<String, Boolean?>>(emptyMap())
    private const val TAG = "GuardianCommUtils"

    private fun hashOTP(code: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(code.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

//    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
//    suspend fun generateAndSendOTP(context: Context, email: String, childEmail: String, phoneNumber: String, fireStoreService: FireStoreService): String {
//        return OTPUtils.generateAndSendPairingOTP(context, email, childEmail, phoneNumber, fireStoreService)
//    }
//
//    suspend fun verifyOTP(context: Context, email: String, childEmail: String, phoneNumber: String, inputCode: String, fireStoreService: FireStoreService): Boolean {
//        return OTPUtils.verifyPairingOTP(email, childEmail, phoneNumber, inputCode, fireStoreService)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    suspend fun requestLinkCancellation(context: Context, email: String, linkedEmail: String, phoneNumber: String, fireStoreService: FireStoreService) {
//        val db = AppDatabase.getDatabase(context)
//        val user = db.userDao().getUser(phoneNumber) ?: return
//        val otpCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
//            OTPUtils.generateAndSendCancellationOTP(context, email, linkedEmail, phoneNumber, fireStoreService)
//        } else {
//            TODO("VERSION.SDK_INT < VANILLA_ICE_CREAM")
//        }
//        if (otpCode.isNotEmpty()) {
//            linkRequests.value = linkRequests.value + (phoneNumber to email)
//            NotificationUtils.showNotification(
//                context,
//                "Link Cancellation Request",
//                "An OTP has been sent to $phoneNumber."
//            )
//        }
//    }

//    suspend fun verifyCancellationOTP(context: Context, email: String, linkedEmail: String, phoneNumber: String, inputCode: String, fireStoreService: FireStoreService): Boolean {
//        return withContext(Dispatchers.IO) {
//            val db = AppDatabase.getDatabase(context)
//            val user = db.userDao().getUser(phoneNumber) ?: return@withContext false
//            val isValid = OTPUtils.verifyCancellationOTP(email, linkedEmail, phoneNumber, inputCode, fireStoreService)
//            if (isValid) {
//                db.userDao().updateUser(user.copy(guardianPhoneNumber = null))
//                db.guardianDao().clearGuardian()
//                linkRequests.value = linkRequests.value - phoneNumber
//            }
//            isValid
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendCheckInRequest(guardianEmail: String, childPhone: String, context: Context): Boolean {
        checkInResponses.value = checkInResponses.value + (childPhone to null)
        val message = "Guardian ($guardianEmail) is checking in. Reply 'YES' within 30 seconds."
        val sent = runBlocking {
            SMSUtils.sendSMS(context, childPhone, message)
        }
        if (sent) {
            NotificationUtils.showCheckInNotification(context, guardianEmail, childPhone)
            return true
        }
        return false
    }

    fun observeCheckInResponse(childPhone: String, onResponse: (Boolean?) -> Unit) {
        val job = kotlinx.coroutines.GlobalScope.launch {
            while (true) {
                val response = checkInResponses.value[childPhone]
                onResponse(response)
                if (response != null) break
                delay(1000)
            }
        }
    }

    fun sendCheckInResponse(childEmail: String, response: Boolean) {
        checkInResponses.value = checkInResponses.value + (childEmail to response)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendLocationRequest(guardianEmail: String, childPhone: String, context: Context): Boolean {
        locationResponses.value = locationResponses.value + (childPhone to null)
        val message = "Guardian ($guardianEmail) requests location. Reply 'SHARE' within 30 seconds."
        val sent = runBlocking {
            SMSUtils.sendSMS(context, childPhone, message)
        }
        if (sent) {
            NotificationUtils.showLocationRequestNotification(context, guardianEmail, childPhone)
            return true
        }
        return false
    }

    fun observeLocationResponse(childPhone: String, onResponse: (String?) -> Unit) {
        val job = kotlinx.coroutines.GlobalScope.launch {
            while (true) {
                val response = locationResponses.value[childPhone]
                onResponse(response)
                if (response != null) break
                delay(1000)
            }
        }
    }

    fun sendLocationResponse(childEmail: String, location: String?) {
        locationResponses.value = locationResponses.value + (childEmail to location)
    }

    fun getLinkedChild(context: Context, guardianEmail: String, fireStoreService: FireStoreService): String? {
        return runBlocking {
            val querySnapshot = fireStoreService.queryDocuments("families", "guardianEmail", guardianEmail)
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.first().getString("childPhone")
            } else {
                null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendBargeInRequest(guardianEmail: String, childPhone: String, context: Context): Boolean {
        bargeInResponses.value = bargeInResponses.value + (childPhone to null)
        NotificationUtils.showNotification(
            context,
            "Barge-In Request",
            "Guardian requests immediate access."
        )
        return true
    }

    fun observeBargeInResponse(childPhone: String, onResponse: (Boolean?) -> Unit) {
        val job = kotlinx.coroutines.GlobalScope.launch {
            while (true) {
                val response = bargeInResponses.value[childPhone]
                onResponse(response)
                if (response != null) break
                delay(1000)
            }
        }
    }

    fun sendBargeInResponse(childEmail: String, authorized: Boolean) {
        bargeInResponses.value = bargeInResponses.value + (childEmail to authorized)
    }
}