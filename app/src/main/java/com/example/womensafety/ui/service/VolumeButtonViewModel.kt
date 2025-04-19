package com.example.womensafety.ui.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.data.ContactDao
import com.example.womensafety.data.PoliceHelplineDao
import com.example.womensafety.utility.LocationUtils
import com.example.womensafety.utility.PermissionUtils
import com.example.womensafety.utility.SMSUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VolumeButtonViewModel(
    private val context: Context,
    private val permissionUtils: PermissionUtils,
    private val contactDao: ContactDao,
    private val policeHelplineDao: PoliceHelplineDao
) : ViewModel() {

    private val TAG = "VolumeButtonViewModel"

    fun onVolumeUpDoublePress() {
        viewModelScope.launch {
            try {
                // Check permissions
                if (!permissionUtils.isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissionUtils.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) { granted ->
                        if (granted) onVolumeUpDoublePress() // Retry if granted
                    }
                    showToast("Location permission required")
                    return@launch
                }
                if (!permissionUtils.isPermissionGranted(android.Manifest.permission.SEND_SMS)) {
                    permissionUtils.requestPermission(android.Manifest.permission.SEND_SMS) { granted ->
                        if (granted) onVolumeUpDoublePress()
                    }
                    showToast("SMS permission required")
                    return@launch
                }

                // Fetch location
                val location = LocationUtils.getCurrentLocation(context)
                if (location == null) {
                    showToast("Failed to fetch location")
                    Log.e(TAG, "Location unavailable")
                    return@launch
                }

                // Fetch contacts
                val contacts = contactDao.getAllContacts()
                if (contacts.isEmpty()) {
                    showToast("No emergency contacts found")
                    Log.e(TAG, "No contacts in database")
                    return@launch
                }

                // Send SMS
                val message = "This is my current location: https://www.google.com/maps?q=${location.latitude},${location.longitude}"
                val phoneNumbers = contacts.map { it.phoneNumber }
                val success = SMSUtils.sendSMSToContacts(context, phoneNumbers, message)
                if (success) {
                    showToast("Message sent")
                    Log.d(TAG, "SMS sent to ${phoneNumbers.size} contacts")
                } else {
                    showToast("Failed to send message")
                    Log.e(TAG, "Failed to send SMS")
                }
            } catch (e: Exception) {
                showToast("Error sending message")
                Log.e(TAG, "Error in onVolumeUpDoublePress: ${e.message}", e)
            }
        }
    }

    fun onVolumeDownHold() {
        viewModelScope.launch {
            try {
                // Check permissions
                if (!permissionUtils.isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissionUtils.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) { granted ->
                        if (granted) onVolumeDownHold()
                    }
                    showToast("Location permission required")
                    return@launch
                }
                if (!permissionUtils.isPermissionGranted(android.Manifest.permission.CALL_PHONE)) {
                    permissionUtils.requestPermission(android.Manifest.permission.CALL_PHONE) { granted ->
                        if (granted) onVolumeDownHold()
                    }
                    showToast("Call permission required")
                    return@launch
                }

                // Fetch location
                val location = LocationUtils.getCurrentLocation(context)
                if (location == null) {
                    showToast("Failed to fetch location")
                    Log.e(TAG, "Location unavailable")
                    return@launch
                }

                // Fetch city
                val city = LocationUtils.getCityFromLocation(context, location)
                if (city.isEmpty()) {
                    showToast("Failed to determine city")
                    Log.e(TAG, "City unavailable")
                    return@launch
                }

                // Fetch helpline
                val helpline = policeHelplineDao.getContactByCity(city)
                if (helpline == null) {
                    showToast("No helpline found for $city")
                    Log.e(TAG, "No helpline for city: $city")
                    return@launch
                } else {

                }

                // Make call
                try {
                    val callIntent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${helpline.mobileNumber}")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(callIntent)
                    showToast("Calling police helpline")
                    Log.d(TAG, "Calling helpline: ${helpline.mobileNumber}")
                } catch (e: Exception) {
                    showToast("Failed to make call")
                    Log.e(TAG, "Call failed: ${e.message}", e)
                }
            } catch (e: Exception) {
                showToast("Error making call")
                Log.e(TAG, "Error in onVolumeDownHold: ${e.message}", e)
            }
        }
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}