package com.example.womensafety.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.womensafety.MainActivity
import com.example.womensafety.R
import com.example.womensafety.model.IncidentReport
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object NotificationUtils {
    private const val CHANNEL_ID = "women_safety_channel"
    private const val CHANNEL_NAME = "Women Safety Notifications"
    private const val REQUEST_CODE_CHECK_IN = 100
    private const val REQUEST_CODE_LOCATION = 101

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for Women Safety App"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @Suppress("missingpermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showNotification(context: Context, title: String, message: String) {
        if (!PermissionUtils.isPermissionGranted(android.Manifest.permission.POST_NOTIFICATIONS)) {
            PermissionUtils.requestPermission(android.Manifest.permission.POST_NOTIFICATIONS)
            return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.parental_control_title_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    @Suppress("missingpermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showCheckInNotification(context: Context, guardianPhone: String, childPhone: String) {
        if (!PermissionUtils.isPermissionGranted(android.Manifest.permission.POST_NOTIFICATIONS)) {
            PermissionUtils.requestPermission(android.Manifest.permission.POST_NOTIFICATIONS)
            return
        }

        val yesIntent = Intent(context, MainActivity::class.java).apply {
            action = "ACTION_CHECK_IN_YES"
            putExtra("guardianPhone", guardianPhone)
            putExtra("childPhone", childPhone)
        }
        val yesPendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_CHECK_IN,
            yesIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.parental_control_title_logo)
            .setContentTitle("Check-In Request")
            .setContentText("Your guardian is checking in. Respond within 30 seconds.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                NotificationCompat.Action(
                    null,
                    "Yes, I'm Fine",
                    yesPendingIntent
                )
            )

        with(NotificationManagerCompat.from(context)) {
            notify(childPhone.hashCode(), builder.build())
        }
    }

    @Suppress("missingpermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showLocationRequestNotification(context: Context, guardianPhone: String, childPhone: String) {
        if (!PermissionUtils.isPermissionGranted(android.Manifest.permission.POST_NOTIFICATIONS)) {
            PermissionUtils.requestPermission(android.Manifest.permission.POST_NOTIFICATIONS)
            return
        }

        val shareIntent = Intent(context, MainActivity::class.java).apply {
            action = "ACTION_SHARE_LOCATION"
            putExtra("guardianPhone", guardianPhone)
            putExtra("childPhone", childPhone)
        }
        val sharePendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_LOCATION,
            shareIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.parental_control_title_logo)
            .setContentTitle("Location Request")
            .setContentText("Your guardian is requesting your location. Share now?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                NotificationCompat.Action(
                    null,
                    "Share Location",
                    sharePendingIntent
                )
            )

        with(NotificationManagerCompat.from(context)) {
            notify(childPhone.hashCode() + 1, builder.build())
        }
    }
//        private const val FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/womensafety/messages:send"
//        private const val SERVER_KEY = "82e71fc9ae39536c878d15c133295fa2f8ee6a69" // Replace with your FCM server key 172.18.4.58
private const val SERVER_ENDPOINT = "http://172.18.4.58:3000/send-notification" // Update to deployed URL later

    suspend fun sendCommunityNotification(context: Context, report: IncidentReport) {
        withContext(Dispatchers.IO) {
            try {
                FirebaseMessaging.getInstance().subscribeToTopic("community_reports").await()
                Log.d(TAG, "Subscribed to community_reports")

                val client = OkHttpClient()
                val requestBody = JSONObject()
                    .put("city", report.city ?: "your area")
                    .toString()
                    .toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url(SERVER_ENDPOINT)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d(TAG, "Notification sent via server")
                } else {
                    Log.e(TAG, "Server error: ${response.code} - ${response.body?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send notification: ${e.message}", e)
            }
        }
    }
}