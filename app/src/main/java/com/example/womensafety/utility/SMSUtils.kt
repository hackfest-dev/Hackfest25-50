package com.example.womensafety.utility

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SMSUtils {
    private const val TAG = "SMSUtils"

    /**
     * Sends an SMS to the specified phone number.
     * @param context Application context
     * @param phoneNumber Phone number to send the SMS to
     * @param message Message content (e.g., OTP)
     * @return True if SMS was sent successfully, false otherwise
     */
    suspend fun sendSMS(context: Context, phoneNumber: String, message: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (!PermissionUtils.isPermissionGranted(android.Manifest.permission.SEND_SMS)) {
                    PermissionUtils.requestPermission(android.Manifest.permission.SEND_SMS)
                    Log.d(TAG, "SEND_SMS permission not granted")
                    return@withContext false
                }

                val smsManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }

                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d(TAG, "SMS sent to $phoneNumber: $message")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send SMS to $phoneNumber: ${e.message}", e)
                false
            }
        }
    }

    /**
     * Sends an SMS to a list of phone numbers.
     * @param context Application context
     * @param phoneNumbers List of phone numbers to send the SMS to
     * @param message Message content
     * @return True if SMS was sent successfully to all numbers, false if any failed
     */
    suspend fun sendSMSToContacts(context: Context, phoneNumbers: List<String>, message: String): Boolean {
        return withContext(Dispatchers.IO) {
            var allSuccess = true
            phoneNumbers.forEach { phoneNumber ->
                val success = sendSMS(context, phoneNumber, message)
                if (!success) {
                    allSuccess = false
                    Log.e(TAG, "Failed to send SMS to $phoneNumber")
                }
            }
            allSuccess
        }
    }
}