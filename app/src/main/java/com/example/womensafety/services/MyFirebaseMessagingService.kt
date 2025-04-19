package com.example.womensafety.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")
        // Send token to your server if needed
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "Message received: ${message.notification?.body}")
        // Handle notification (e.g., show in notification tray)
        message.notification?.let {
            // Display notification using NotificationCompat
            // For simplicity, log it
            Log.d(TAG, "Notification: ${it.title} - ${it.body}")
        }
    }
}