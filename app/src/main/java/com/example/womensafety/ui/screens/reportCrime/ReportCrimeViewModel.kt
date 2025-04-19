package com.example.womensafety.ui.screens.reportCrime

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.model.IncidentReport
import com.example.womensafety.model.Location
import com.example.womensafety.utility.LocationUtils
import com.example.womensafety.utility.NotificationUtils
import com.example.womensafety.utility.PermissionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportCrimeViewModel(
    private val context: Context,
    private val permissionUtils: PermissionUtils
) : ViewModel() {

    private val TAG = "ReportCrimeViewModel"

    // UI state
    val location = mutableStateOf<Location?>(null)
    val city = mutableStateOf<String?>(null)
    val category = mutableStateOf<String>("")
    val description = mutableStateOf<String>("")
    val mediaUri = mutableStateOf<Uri?>(null)
    val isSubmitting = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val submissionStatus = mutableStateOf<String?>(null)

    /**
     * Fetches the current location and city.
     */
    fun fetchLocation() {
        viewModelScope.launch {
            try {
                if (!permissionUtils.isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissionUtils.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) { granted ->
                        if (granted) fetchLocation()
                    }
                    showToast("Location permission required")
                    errorMessage.value = "Location permission required"
                    return@launch
                }

                val loc = LocationUtils.getCurrentLocation(context)
                if (loc == null) {
                    showToast("Failed to fetch location")
                    errorMessage.value = "Failed to fetch location"
                    return@launch
                }

                location.value = loc
                city.value = LocationUtils.getCityFromLocation(context, loc)
                errorMessage.value = null
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Failed to fetch location: ${e.message}", e)
                showToast("Error fetching location")
                errorMessage.value = "Error fetching location"
            }
        }
    }

    /**
     * Sets a manually adjusted location (simulated).
     */
    fun adjustLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val loc = Location(latitude, longitude)
                location.value = loc
                city.value = LocationUtils.getCityFromLocation(context, loc)
                errorMessage.value = null
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Failed to adjust location: ${e.message}", e)
                showToast("Error adjusting location")
                errorMessage.value = "Error adjusting location"
            }
        }
    }

    /**
     * Sets the selected media URI.
     */
    fun setMediaUri(uri: Uri?) {
        viewModelScope.launch {
            mediaUri.value = uri
            errorMessage.value = if (uri == null) {
                showToast("Failed to select media")
                "Failed to select media"
            } else {
                null
            }
        }
    }

    /**
     * Submits the incident report.
     */
    fun submitReport() {
        viewModelScope.launch {
            if (category.value.isEmpty()) {
                showToast("Please select a category")
                errorMessage.value = "Please select a category"
                return@launch
            }

            if (location.value == null) {
                showToast("Please set a location")
                errorMessage.value = "Please set a location"
                return@launch
            }

            isSubmitting.value = true
            try {
                val report = IncidentReport(
                    latitude = location.value!!.latitude,
                    longitude = location.value!!.longitude,
                    city = city.value,
                    category = category.value,
                    description = description.value.takeIf { it.isNotBlank() },
                    mediaPath = mediaUri.value?.toString()
                )

                // Simulate sending to women’s empowerment organization
                sendToOrganization(report)

                // Send community notification
                NotificationUtils.sendCommunityNotification(context, report)

                submissionStatus.value = "Report submitted successfully"
                showToast("Report submitted successfully")

                // Reset form
                location.value = null
                city.value = null
                category.value = ""
                description.value = ""
                mediaUri.value = null
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Failed to submit report: ${e.message}", e)
                showToast("Failed to submit report")
                errorMessage.value = "Failed to submit report"
            } finally {
                isSubmitting.value = false
            }
        }
    }

    /**
     * Simulates sending the report to an organization.
     */
    private suspend fun sendToOrganization(report: IncidentReport) {
        withContext(Dispatchers.IO) {
            android.util.Log.d(TAG, "Sending report to organization: $report")
        }
    }

    /**
     * Shows a Toast message on the main thread.
     */
    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}