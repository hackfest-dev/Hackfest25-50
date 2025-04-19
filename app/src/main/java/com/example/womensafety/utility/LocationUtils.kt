package com.example.womensafety.utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import com.example.womensafety.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.AddressComponentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

object LocationUtils {
    private const val TAG = "LocationUtils"

    /**
     * Fetches the current device location using FusedLocationProviderClient.
     * @param context Application context
     * @return Location object with latitude and longitude, or null if unavailable
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Location? {
        return withContext(Dispatchers.IO) {
            try {
                if (!PermissionUtils.isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    PermissionUtils.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    Log.d(TAG, "ACCESS_FINE_LOCATION permission not granted")
                    return@withContext null
                }

                val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                val location = client.lastLocation.await()
                location?.let {
                    Log.d(TAG, "Location fetched: ${it.latitude}, ${it.longitude}")
                    Location(it.latitude, it.longitude)
                } ?: run {
                    Log.e(TAG, "Location unavailable")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch location: ${e.message}", e)
                null
            }
        }
    }

    /**
     * Reverse geocodes a location to a human-readable address using Android Geocoder.
     * @param context Application context
     * @param location Location object with latitude and longitude
     * @return Address string or coordinates if geocoding fails
     */
    suspend fun reverseGeocodeLocation(context: Context, location: Location): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressLine = address.getAddressLine(0) ?: ""
                    val city = address.locality ?: ""
                    val state = address.adminArea ?: ""
                    val country = address.countryName ?: ""
                    val postalCode = address.postalCode ?: ""

                    val fullAddress = buildString {
                        if (addressLine.isNotEmpty()) append(addressLine)
                        if (city.isNotEmpty()) append(", $city")
                        if (state.isNotEmpty()) append(", $state")
                        if (country.isNotEmpty()) append(", $country")
                        if (postalCode.isNotEmpty()) append(" $postalCode")
                    }.trim()

                    Log.d(TAG, "Geocoded address: $fullAddress")
                    fullAddress.ifEmpty { "${location.latitude}, ${location.longitude}" }
                } else {
                    Log.e(TAG, "No address found for coordinates: ${location.latitude}, ${location.longitude}")
                    "${location.latitude}, ${location.longitude}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Geocoding failed: ${e.message}", e)
                "${location.latitude}, ${location.longitude}"
            }
        }
    }

    /**
     * Fetches the city name for a location using Google Maps Geocoding API.
     * @param context Application context (for API key)
     * @param location Location object with latitude and longitude
     * @return City name or empty string if unavailable
     */
    suspend fun getCityFromLocation(context: Context, location: Location): String {
        return withContext(Dispatchers.IO) {
            try {
                // Fetch API key from manifest
                val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")
                    ?: throw IllegalStateException("Google Maps API key not found in manifest")

                val geoApiContext = GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build()

                val results = GeocodingApi.geocode(
                    geoApiContext,
                    com.google.maps.model.LatLng(location.latitude, location.longitude).toString()
                ).await()

                geoApiContext.shutdown()

                if (results.isNotEmpty()) {
                    val address = results[0]
                    val city = address.addressComponents.find { component: com.google.maps.model.AddressComponent ->
                        component.types.any { it == AddressComponentType.LOCALITY }
                    }?.longName
                    city?.uppercase(Locale.getDefault())?.trim() ?: run {
                        Log.e(TAG, "No city found for coordinates: ${location.latitude}, ${location.longitude}")
                        ""
                    }
                } else {
                    Log.e(TAG, "No geocoding results for coordinates: ${location.latitude}, ${location.longitude}")
                    ""
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google Maps geocoding failed: ${e.message}", e)
                ""
            }
        }
    }
}