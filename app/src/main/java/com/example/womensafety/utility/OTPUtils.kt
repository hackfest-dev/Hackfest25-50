package com.example.womensafety.utility

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.womensafety.services.FireStoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import java.util.Random
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


object OTPUtils {
    private const val OTP_LENGTH = 6
    private const val OTP_EXPIRY_MS = 5 * 60 * 1000 // 5 minutes
    private const val EMAIL_HOST = "smtp.gmail.com" // Configure as needed
    private const val EMAIL_PORT = "587"
    private const val EMAIL_USER = "your-email@gmail.com" // Replace with your email
    private const val EMAIL_PASSWORD = "your-app-password" // Replace with your app-specific password

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun generateAndSendPairingOTP(
        context: Context,
        guardianEmail: String,
        childEmail: String,
        fireStoreService: FireStoreService
    ): String {
        val otp = Random().nextInt(100000, 999999).toString()
        val familyId = "$guardianEmail-$childEmail"
        val otpData = mapOf(
            "otp" to otp,
            "guardianEmail" to guardianEmail,
            "childEmail" to childEmail,
            "expiresAt" to (System.currentTimeMillis() + OTP_EXPIRY_MS)
        )
        fireStoreService.addDocument("tempOtps", familyId, otpData)
        sendEmail(childEmail, "Pairing OTP from guardian $guardianEmail", "Your OTP for pairing is: $otp")
        return otp
    }

    suspend fun verifyPairingOTP(
        guardianEmail: String,
        childEmail: String,
        inputOtp: String,
        fireStoreService: FireStoreService
    ): Boolean {
        val familyId = "$guardianEmail-$childEmail"
        val document = fireStoreService.getDocument("tempOtps", familyId)
        return if (document != null && document.exists()) {
            val storedOtp = document.getString("otp")
            val expiresAt = document.getLong("expiresAt") ?: 0
            if (System.currentTimeMillis() < expiresAt && storedOtp == inputOtp) {
                fireStoreService.deleteDocument("tempOtps", familyId)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    suspend fun generateAndSendCancellationOTP(
        context: Context,
        guardianEmail: String,
        childEmail: String,
        fireStoreService: FireStoreService
    ): String {
        val otp = Random().nextInt(100000, 999999).toString()
        val familyId = "$guardianEmail-$childEmail"
        val otpData = mapOf(
            "cancelOtp" to otp,
            "guardianEmail" to guardianEmail,
            "childEmail" to childEmail,
            "expiresAt" to (System.currentTimeMillis() + OTP_EXPIRY_MS)
        )
        fireStoreService.addDocument("tempOtps", familyId, otpData)
        sendEmail(childEmail, "Cancellation OTP from guardian $guardianEmail", "Your OTP for cancellation is: $otp")
        return otp
    }

    suspend fun verifyCancellationOTP(
        guardianEmail: String,
        childEmail: String,
        inputOtp: String,
        fireStoreService: FireStoreService
    ): Boolean {
        val familyId = "$guardianEmail-$childEmail"
        val document = fireStoreService.getDocument("tempOtps", familyId)
        return if (document != null && document.exists()) {
            val storedOtp = document.getString("cancelOtp")
            val expiresAt = document.getLong("expiresAt") ?: 0
            if (System.currentTimeMillis() < expiresAt && storedOtp == inputOtp) {
                fireStoreService.deleteDocument("tempOtps", familyId)
                fireStoreService.deleteDocument("families", familyId)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    private suspend fun sendEmail(toEmail: String, subject: String, message: String) {
        withContext(Dispatchers.IO) {
            try {
                val props = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", EMAIL_HOST)
                    put("mail.smtp.port", EMAIL_PORT)
                }
                val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                        return javax.mail.PasswordAuthentication(EMAIL_USER, EMAIL_PASSWORD)
                    }
                })
                val mimeMessage = MimeMessage(session).apply {
                    setFrom(InternetAddress(EMAIL_USER))
                    addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
                    setSubject(subject)
                    setText(message)
                }
                Transport.send(mimeMessage)
            } catch (e: Exception) {
                // Log error
            }
        }
    }
}