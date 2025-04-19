package com.example.womensafety.ui.screens.home

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.Screens
import com.example.womensafety.data.ContactDao
import com.example.womensafety.utility.LocationUtils
import com.example.womensafety.utility.PermissionUtils
import com.example.womensafety.utility.SMSUtils
import kotlinx.coroutines.launch

class HomeViewModel(
    private val context: Context,
    private val contactDao: ContactDao
) : ViewModel() {

    private val _currentBottomScreen: MutableState<Screens.BottomNavScreens> = mutableStateOf(Screens.BottomNavScreens.Home)
    val currentBottomScreen: MutableState<Screens.BottomNavScreens> get() = _currentBottomScreen

    private val _currentDrawerScreen: MutableState<Screens.DrawerScreens> = mutableStateOf(Screens.DrawerScreens.Developers)
    val currentDrawerScreen: MutableState<Screens.DrawerScreens> get() = _currentDrawerScreen

    fun navigateToBottomScreen(screen: Screens.BottomNavScreens) {
        _currentBottomScreen.value = screen
    }

    fun onSOSClickedButton() {
        viewModelScope.launch {
            // Log permission status
            val locationPermission = PermissionUtils.isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)
            val smsPermission = PermissionUtils.isPermissionGranted(android.Manifest.permission.SEND_SMS)
            android.util.Log.d("HomeViewModel", "Permissions - Location: $locationPermission, SMS: $smsPermission")

            if (locationPermission && smsPermission) {
                // Fetch current location
                val location = LocationUtils.getCurrentLocation(context)
                if (location != null) {
                    // Convert to geocoded address
                    val address = LocationUtils.reverseGeocodeLocation(context, location)
                    android.util.Log.d("HomeViewModel", "Location: $location, Address: $address")

                    // Fetch all contacts from the database
                    val contacts = contactDao.getAllContacts()
                    android.util.Log.d("HomeViewModel", "Contacts: ${contacts.map { it.phoneNumber }}")

                    if (contacts.isNotEmpty()) {
                        // Prepare the message
                        val message = "Help! My location: https://www.google.com/maps?q=${location.latitude},${location.longitude}\n"
                        android.util.Log.d("HomeViewModel", "Message: $message, Length: ${message.length}")

                        // Send SMS to all contacts
                        val success = SMSUtils.sendSMSToContacts(context, contacts.map { it.phoneNumber }, message)
                        if (success) {
                            android.util.Log.d("HomeViewModel", "SMS sent to all contacts")
                            android.widget.Toast.makeText(context, "SMS sent", android.widget.Toast.LENGTH_SHORT).show()
                        } else {
                            android.util.Log.e("HomeViewModel", "Failed to send SMS to some or all contacts")
                            android.widget.Toast.makeText(context, "Failed to send SMS", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        android.util.Log.d("HomeViewModel", "No contacts available")
                        android.widget.Toast.makeText(context, "No contacts available", android.widget.Toast.LENGTH_SHORT).show()
                    }
                } else {
                    android.util.Log.e("HomeViewModel", "Unable to fetch location")
                    android.widget.Toast.makeText(context, "Unable to fetch location", android.widget.Toast.LENGTH_SHORT).show()
                }
            } else {
                // Request missing permissions
                PermissionUtils.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                PermissionUtils.requestPermission(android.Manifest.permission.SEND_SMS)
                android.util.Log.d("HomeViewModel", "Required permissions not granted")
                android.widget.Toast.makeText(context, "Permissions required", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}