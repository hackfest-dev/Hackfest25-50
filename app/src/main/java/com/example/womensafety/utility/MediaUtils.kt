package com.example.womensafety.utility


import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts

object MediaUtils {
    private const val TAG = "MediaUtils"

    /**
     * Launches an intent to pick a photo or video.
     * @param activity The activity to register the result callback
     * @param onResult Callback with the selected media URI or null if canceled
     */
//    fun pickMedia(activity: AppCompatActivity, onResult: (Uri?) -> Unit) {
//        val launcher = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            onResult(uri)
//        }
//
//
//        try {
//            launcher.launch("image/* video/*")
//        } catch (e: Exception) {
//            android.util.Log.e(TAG, "Failed to launch media picker: ${e.message}", e)
//            onResult(null)
//        }
//    }
}