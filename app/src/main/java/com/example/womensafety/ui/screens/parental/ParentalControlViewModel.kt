package com.example.womensafety.ui.screens.parental

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.model.ContactNumbers
import com.example.womensafety.model.Guardian
import com.example.womensafety.model.User
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.FireStoreService
import com.example.womensafety.utility.GuardianCommUtils
import com.example.womensafety.utility.LocationUtils
import com.example.womensafety.utility.OTPUtils
import com.example.womensafety.utility.PermissionUtils
import com.example.womensafety.utility.SMSUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ParentalControlViewModel(
    private val context: Context,
    private val database: AppDatabase,
    private val fireStoreService: FireStoreService,
    private val accountService: AccountService
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _contacts = MutableStateFlow<List<ContactNumbers>>(emptyList())
    val contacts: StateFlow<List<ContactNumbers>> = _contacts

    private val _selectedContact = MutableStateFlow<ContactNumbers?>(null)
    val selectedContact: StateFlow<ContactNumbers?> = _selectedContact

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    private val _otpCode = MutableStateFlow("")
    val otpCode: StateFlow<String> = _otpCode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _checkInStatus = MutableStateFlow<String?>(null)
    val checkInStatus: StateFlow<String?> = _checkInStatus

    private val _checkInRequest = MutableStateFlow(false)
    val checkInRequest: StateFlow<Boolean> = _checkInRequest

    private val _timerSeconds = MutableStateFlow<Int?>(null)
    val timerSeconds: StateFlow<Int?> = _timerSeconds

    private val _role = MutableStateFlow(0) // 0 = child, 1 = guardian
    val role: StateFlow<Int> = _role

    private val _linkEstablished = MutableStateFlow(false)
    val linkEstablished: StateFlow<Boolean> = _linkEstablished

    private val _isLinking = MutableStateFlow(false)
    val isLinking: StateFlow<Boolean> = _isLinking

    private val _bargeInRequest = MutableStateFlow(false)
    val bargeInRequest: StateFlow<Boolean> = _bargeInRequest

    private val _bargeInStatus = MutableStateFlow<String?>(null)
    val bargeInStatus: StateFlow<String?> = _bargeInStatus

    private val _cancellationPending = MutableStateFlow(false)
    val cancellationPending: StateFlow<Boolean> = _cancellationPending

    private val _showSuccessMessage = MutableStateFlow(false)
    val showSuccessMessage: StateFlow<Boolean> = _showSuccessMessage

    init {
        loadUser()
        loadContacts()
    }

    fun hideSuccessMessage() {
        _showSuccessMessage.value = false
    }

    private fun loadUser() {
        viewModelScope.launch {
            val email = accountService.getCurrentUserEmail() ?: run {
                _errorMessage.value = "User not signed in"
                Log.e("ParentalControlViewModel", "No user signed in")
                return@launch
            }
            Log.d("ParentalControlViewModel", "Signed-in user email: $email")
            val phoneNumber = emailToPhoneNumber(email) // Map email to phoneNumber
            var user = database.userDao().getUser(phoneNumber)
            if (user == null) {
                user = User(
                    phoneNumber = phoneNumber, name = "User",
                    email = email, password = null, isGuardian = false, guardianPhoneNumber = null
                )
                database.userDao().insertUser(user)
            }
            _user.value = user
            _role.value = if (user.isGuardian == true) 1 else 0
            checkLinkStatus(email)
        }
    }

    private fun checkLinkStatus(email: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = fireStoreService.queryDocuments("families", "guardianEmail", email)
                val isGuardianLinked = !querySnapshot.isEmpty
                if (isGuardianLinked) {
                    _linkEstablished.value = true
                    Log.d("ParentalControlViewModel", "Guardian link found for $email")
                    return@launch
                }
                val querySnapshotChild = fireStoreService.queryDocuments("families", "childEmail", email)
                _linkEstablished.value = !querySnapshotChild.isEmpty
                if (_linkEstablished.value) {
                    Log.d("ParentalControlViewModel", "Child link found for $email")
                } else {
                    Log.d("ParentalControlViewModel", "No link found for $email")
                }
            } catch (e: Exception) {
                Log.e("ParentalControlViewModel", "checkLinkStatus failed: ${e.message}", e)
                _errorMessage.value = "Failed to check link status: ${e.message}"
            }
        }
    }

    private fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = database.contactListDao().getAllContacts()
        }
    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    fun switchRole(newRole: Int) {
//        viewModelScope.launch {
//            _role.value = newRole
//            val user = _user.value ?: return@launch
//            if (newRole == 1 && user.guardianPhoneNumber != null) {
//                requestLinkCancellation()
//            }
//            database.userDao().updateUser(user.copy(isGuardian = newRole == 1))
//            val email = accountService.getCurrentUserEmail() ?: return@launch
//            checkLinkStatus(email)
//        }
//    }

    fun selectContact(contact: ContactNumbers) {
        _selectedContact.value = contact
        _otpSent.value = false
        _otpCode.value = ""
        _errorMessage.value = null
    }

//    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
//    fun requestOTP() {
//        val contact = _selectedContact.value ?: run {
//            _errorMessage.value = "Please select a contact"
//            return
//        }
//        viewModelScope.launch {
//            val phoneNumber = formatPhoneNumber(contact.phoneNumber)
//            if (phoneNumber == null) {
//                _errorMessage.value = "Invalid phone number format"
//                return@launch
//            }
//            if (!PermissionUtils.isPermissionGranted(Manifest.permission.SEND_SMS)) {
//                PermissionUtils.requestPermission(Manifest.permission.SEND_SMS) { granted ->
//                    if (granted) {
//                        viewModelScope.launch {
//                            sendOTP(phoneNumber, contact.name, contact.email)
//                        }
//                    } else {
//                        _errorMessage.value = "SMS permission required"
//                    }
//                }
//                return@launch
//            }
//            sendOTP(phoneNumber, contact.name, contact.email)
//        }
//    }

   /* @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private suspend fun sendOTP(phoneNumber: String, contactName: String, contactEmail: String?) {
        val email = accountService.getCurrentUserEmail() ?: run {
            _errorMessage.value = "User not signed in"
            return
        }
        val childEmail = contactEmail ?: run {
            _errorMessage.value = "Contact email required"
            return
        }
        try {
            _otpCode.value = GuardianCommUtils.generateAndSendOTP(context, email, childEmail, phoneNumber, fireStoreService)
            if (_otpCode.value.isNotEmpty()) {
                _otpSent.value = true
                _errorMessage.value = "OTP sent to $contactName ($phoneNumber)."
                _isLinking.value = true
            } else {
                _errorMessage.value = "Failed to send OTP."
            }
        } catch (e: Exception) {
            Log.e("ParentalControlViewModel", "sendOTP failed: ${e.message}", e)
            _errorMessage.value = "Failed to send OTP: ${e.message}"
        }
    }

    fun verifyOTP(inputCode: String) {
        val contact = _selectedContact.value ?: return
        viewModelScope.launch {
            val phoneNumber = formatPhoneNumber(contact.phoneNumber) ?: run {
                _errorMessage.value = "Invalid phone number format"
                return@launch
            }
            val email = accountService.getCurrentUserEmail() ?: run {
                _errorMessage.value = "User not signed in"
                return@launch
            }
            val childEmail = contact.email ?: run {
                _errorMessage.value = "Contact email required"
                return@launch
            }
            try {
                val isValid = GuardianCommUtils.verifyOTP(context, email, childEmail, phoneNumber, inputCode, fireStoreService)
                if (isValid) {
                    saveGuardian(contact)
                    _otpSent.value = false
                    _isLinking.value = false
                    _linkEstablished.value = true
                    _showSuccessMessage.value = true
                    _errorMessage.value = "Link established!"
                    val user = _user.value ?: return@launch
                    val childPhone = phoneNumber
                    val familyId = "$email-$childEmail"
                    fireStoreService.addDocument("families", familyId, mapOf(
                        "familyId" to familyId,
                        "guardianEmail" to if (_role.value == 0) childEmail else email,
                        "childEmail" to if (_role.value == 0) email else childEmail,
                        "childPhone" to childPhone,
                        "linked" to true,
                        "createdAt" to System.currentTimeMillis()
                    ))
                    if (_role.value == 0) { // Child
                        database.userDao().updateUser(user.copy(guardianPhoneNumber = childPhone))
                    } else { // Guardian
                        database.userDao().updateUser(user.copy(isGuardian = true))
                    }
                } else {
                    _errorMessage.value = "Invalid or expired OTP"
                }
            } catch (e: Exception) {
                Log.e("ParentalControlViewModel", "verifyOTP failed: ${e.message}", e)
                _errorMessage.value = "Failed to verify OTP: ${e.message}"
            }
        }
    }*/

    private suspend fun saveGuardian(contact: ContactNumbers) {
        val user = _user.value ?: return
        val phoneNumber = formatPhoneNumber(contact.phoneNumber) ?: return
        val guardian = Guardian(phoneNumber = phoneNumber, name = contact.name, verified = true)
        database.guardianDao().clearGuardian()
        database.guardianDao().insertGuardian(guardian)
        database.userDao().updateUser(user.copy(guardianPhoneNumber = phoneNumber))

        var guardianUser = database.userDao().getUser(phoneNumber)
        if (guardianUser == null) {
            guardianUser = User(
                phoneNumber = phoneNumber, name = contact.name, isGuardian = true,
                email = null,
                password = null,
                guardianPhoneNumber = null
            )
            database.userDao().insertUser(guardianUser)
        } else {
            database.userDao().updateUser(guardianUser.copy(isGuardian = true))
        }
        if (_user.value?.phoneNumber == phoneNumber) {
            _user.value = guardianUser
        }

        val email = accountService.getCurrentUserEmail() ?: return
        val childEmail = contact.email ?: return
        if (_role.value == 0) {
            val message = "You are now the guardian of $email."
            SMSUtils.sendSMS(context, phoneNumber, message)
        } else {
            val message = "You are now linked to guardian $email."
            SMSUtils.sendSMS(context, phoneNumber, message)
        }
    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    fun requestLinkCancellation() {
//        viewModelScope.launch {
//            val user = _user.value ?: return@launch
//            val email = accountService.getCurrentUserEmail() ?: run {
//                _errorMessage.value = "User not signed in"
//                return@launch
//            }
//            if (!PermissionUtils.isPermissionGranted(Manifest.permission.SEND_SMS)) {
//                PermissionUtils.requestPermission(Manifest.permission.SEND_SMS) { granted ->
//                    if (granted) {
//                        viewModelScope.launch {
//                            sendCancellationOTP(email, user)
//                        }
//                    } else {
//                        _errorMessage.value = "SMS permission required"
//                    }
//                }
//                return@launch
//            }
//            sendCancellationOTP(email, user)
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    private suspend fun sendCancellationOTP(email: String, user: User) {
//        _cancellationPending.value = true
//        val linkedPhone = user.guardianPhoneNumber ?: GuardianCommUtils.getLinkedChild(context, email, fireStoreService)
//        if (linkedPhone != null) {
//            val linkedEmail = getLinkedEmail(email) // Get childEmail or guardianEmail
//            if (linkedEmail != null) {
//                try {
//                    GuardianCommUtils.requestLinkCancellation(context, email, linkedEmail, linkedPhone, fireStoreService)
//                    _otpSent.value = true
//                    _errorMessage.value = "Cancellation OTP sent."
//                } catch (e: Exception) {
//                    Log.e("ParentalControlViewModel", "sendCancellationOTP failed: ${e.message}", e)
//                    _errorMessage.value = "Failed to send cancellation OTP: ${e.message}"
//                }
//            } else {
//                _errorMessage.value = "No linked email found."
//            }
//        } else {
//            _errorMessage.value = "No linked phone found."
//        }
//    }

//    fun verifyCancellationOTP(inputCode: String) {
//        val user = _user.value ?: return
//        viewModelScope.launch {
//            val email = accountService.getCurrentUserEmail() ?: run {
//                _errorMessage.value = "User not signed in"
//                return@launch
//            }
//            val linkedPhone = user.guardianPhoneNumber ?: GuardianCommUtils.getLinkedChild(context, email, fireStoreService)
//            if (linkedPhone != null) {
//                val linkedEmail = getLinkedEmail(email)
//                if (linkedEmail != null) {
//                    try {
//                        val isValid = GuardianCommUtils.verifyCancellationOTP(context, email, linkedEmail, linkedPhone, inputCode, fireStoreService)
//                        if (isValid) {
//                            _otpSent.value = false
//                            _linkEstablished.value = false
//                            _showSuccessMessage.value = false
//                            _cancellationPending.value = false
//                            _errorMessage.value = "Link cancelled successfully"
//                            database.userDao().updateUser(user.copy(guardianPhoneNumber = null, isGuardian = false))
//                            database.guardianDao().clearGuardian()
//                            SMSUtils.sendSMS(context, linkedPhone, "Guardian-child link with $email has been cancelled.")
//                        } else {
//                            _errorMessage.value = "Invalid or expired cancellation OTP"
//                        }
//                    } catch (e: Exception) {
//                        Log.e("ParentalControlViewModel", "verifyCancellationOTP failed: ${e.message}", e)
//                        _errorMessage.value = "Failed to verify cancellation OTP: ${e.message}"
//                    }
//                } else {
//                    _errorMessage.value = "No linked email found."
//                }
//            } else {
//                _errorMessage.value = "No linked phone found."
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendCheckInRequest() {
        val email = accountService.getCurrentUserEmail() ?: run {
            _errorMessage.value = "User not signed in"
            return
        }
        val childPhone = GuardianCommUtils.getLinkedChild(context, email, fireStoreService) ?: run {
            _errorMessage.value = "No child linked"
            return
        }
        viewModelScope.launch {
            if (!PermissionUtils.isPermissionGranted(Manifest.permission.SEND_SMS)) {
                PermissionUtils.requestPermission(Manifest.permission.SEND_SMS) { granted ->
                    if (granted) {
                        viewModelScope.launch {
                            sendCheckInRequestInternal(email, childPhone)
                        }
                    } else {
                        _errorMessage.value = "SMS permission required"
                    }
                }
                return@launch
            }
            sendCheckInRequestInternal(email, childPhone)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun sendCheckInRequestInternal(email: String, childPhone: String) {
        val success = GuardianCommUtils.sendCheckInRequest(email, childPhone, context)
        _checkInStatus.value = if (success) "Check-in request sent" else "Failed to send request"
        GuardianCommUtils.observeCheckInResponse(childPhone) { response ->
            _checkInStatus.value = when (response) {
                true -> "Child confirmed safe"
                false -> "Child needs help"
                null -> "Awaiting response"
            }
            _checkInRequest.value = false
            _timerSeconds.value = null
        }
        _checkInRequest.value = true
        startTimer(30, childPhone, isCheckIn = true)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestChildLocation() {
        val email = accountService.getCurrentUserEmail() ?: run {
            _errorMessage.value = "User not signed in"
            return
        }
        val childPhone = GuardianCommUtils.getLinkedChild(context, email, fireStoreService) ?: run {
            _errorMessage.value = "No child linked"
            return
        }
        viewModelScope.launch {
            if (!PermissionUtils.isPermissionGranted(Manifest.permission.SEND_SMS)) {
                PermissionUtils.requestPermission(Manifest.permission.SEND_SMS) { granted ->
                    if (granted) {
                        viewModelScope.launch {
                            sendLocationRequestInternal(email, childPhone)
                        }
                    } else {
                        _errorMessage.value = "SMS permission required"
                    }
                }
                return@launch
            }
            sendLocationRequestInternal(email, childPhone)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun sendLocationRequestInternal(email: String, childPhone: String) {
        val success = GuardianCommUtils.sendLocationRequest(email, childPhone, context)
        _checkInStatus.value = if (success) "Location request sent" else "Failed to send request"
        GuardianCommUtils.observeLocationResponse(childPhone) { response ->
            _checkInStatus.value = when (response) {
                null -> "Awaiting location response"
                else -> "Child location: $response"
            }
            _checkInRequest.value = false
            _timerSeconds.value = null
        }
        _checkInRequest.value = true
        startTimer(30, childPhone, isCheckIn = false)
    }

    private fun startTimer(seconds: Int, childPhone: String, isCheckIn: Boolean, isBargeIn: Boolean = false) {
        viewModelScope.launch {
            _timerSeconds.value = seconds
            repeat(seconds) {
                delay(1000)
                _timerSeconds.value = _timerSeconds.value?.minus(1)
                if (_timerSeconds.value == 0) {
                    _timerSeconds.value = null
                    if (isCheckIn) {
                        GuardianCommUtils.sendCheckInResponse(childPhone, false)
                    } else if (isBargeIn) {
                        _bargeInStatus.value = "No response. Escalate to barge-in?"
                    } else {
                        GuardianCommUtils.sendLocationResponse(childPhone, null)
                    }
                }
            }
        }
    }

    fun respondToCheckIn(isOkay: Boolean) {
        viewModelScope.launch {
            val user = _user.value ?: return@launch
            val guardianPhone = user.guardianPhoneNumber ?: return@launch
            val childEmail = accountService.getCurrentUserEmail() ?: return@launch
            GuardianCommUtils.sendCheckInResponse(childEmail, isOkay)
            _checkInRequest.value = false
            _timerSeconds.value = null
            _checkInStatus.value = if (isOkay) "User confirmed safe" else "User needs help"
            val message = if (isOkay) {
                "Child ($childEmail) confirmed safe."
            } else {
                "Child ($childEmail) needs help."
            }
            SMSUtils.sendSMS(context, guardianPhone, message)
        }
    }

    fun respondToLocationRequest() {
        viewModelScope.launch {
            val user = _user.value ?: return@launch
            val guardianPhone = user.guardianPhoneNumber ?: return@launch
            val childEmail = accountService.getCurrentUserEmail() ?: return@launch
            if (!PermissionUtils.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                PermissionUtils.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) { granted ->
                    if (granted) {
                        viewModelScope.launch {
                            sendLocationResponse(guardianPhone, childEmail)
                        }
                    } else {
                        _errorMessage.value = "Location permission required"
                    }
                }
                return@launch
            }
            sendLocationResponse(guardianPhone, childEmail)
        }
    }

    private suspend fun sendLocationResponse(guardianPhone: String, childEmail: String) {
        val location = LocationUtils.getCurrentLocation(context)
        val locationString = location?.let {
            "https://maps.google.com/?q=${it.latitude},${it.longitude}"
        } ?: "Location unavailable"
        GuardianCommUtils.sendLocationResponse(childEmail, locationString)
        _checkInRequest.value = false
        _timerSeconds.value = null
        _checkInStatus.value = "Location shared"
        val message = "Child ($childEmail) location: $locationString"
        SMSUtils.sendSMS(context, guardianPhone, message)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendBargeInRequest() {
        val email = accountService.getCurrentUserEmail() ?: run {
            _errorMessage.value = "User not signed in"
            return
        }
        val childPhone = GuardianCommUtils.getLinkedChild(context, email, fireStoreService) ?: run {
            _errorMessage.value = "No child linked"
            return
        }
        viewModelScope.launch {
            val success = GuardianCommUtils.sendBargeInRequest(email, childPhone, context)
            _bargeInStatus.value = if (success) "Barge-in request sent" else "Failed to send request"
            _bargeInRequest.value = true
            GuardianCommUtils.observeBargeInResponse(childPhone) { authorized ->
                _bargeInStatus.value = when (authorized) {
                    true -> "Access authorized"
                    false -> "Access denied"
                    null -> "Awaiting response"
                }
                _bargeInRequest.value = false
            }
            startTimer(30, childPhone, isCheckIn = false, isBargeIn = true)
        }
    }

    fun respondToBargeIn(authorized: Boolean) {
        viewModelScope.launch {
            val guardianPhone = _user.value?.guardianPhoneNumber ?: return@launch
            val childEmail = accountService.getCurrentUserEmail() ?: return@launch
            GuardianCommUtils.sendBargeInResponse(childEmail, authorized)
            _bargeInRequest.value = false
            _bargeInStatus.value = if (authorized) "Access granted" else "Access denied"
            if (authorized) {
                val location = LocationUtils.getCurrentLocation(context)
                val locationString = location?.let {
                    "https://maps.google.com/?q=${it.latitude},${it.longitude}"
                } ?: "Location unavailable"
                val message = "Barge-in authorized. Location: $locationString"
                SMSUtils.sendSMS(context, guardianPhone, message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun escalateBargeIn() {
        val email = accountService.getCurrentUserEmail() ?: run {
            _errorMessage.value = "User not signed in"
            return
        }
        val childPhone = GuardianCommUtils.getLinkedChild(context, email, fireStoreService) ?: run {
            _errorMessage.value = "No child linked"
            return
        }
        viewModelScope.launch {
            val message = "Emergency barge-in: Guardian ($email) requests your location."
            val sent = SMSUtils.sendSMS(context, childPhone, message)
            if (sent) {
                _bargeInStatus.value = "Escalation requested."
            } else {
                _bargeInStatus.value = "Failed to escalate."
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun formatPhoneNumber(phoneNumber: String): String? {
        val cleaned = phoneNumber.replace("[^0-9+]".toRegex(), "")
        return if (cleaned.isNotEmpty() && cleaned.length >= 10) {
            if (cleaned.startsWith("+")) cleaned else "+91$cleaned"
        } else {
            null
        }
    }

    private fun emailToPhoneNumber(email: String): String {
        // TODO: Implement mapping (e.g., query Firestore 'users' collection or Room)
        // Placeholder: Replace with actual logic to map email to phoneNumber
        return "phoneNumber_$email"
    }

    private suspend fun getLinkedEmail(email: String): String? {
        try {
            val querySnapshot = fireStoreService.queryDocuments("families", "guardianEmail", email)
            if (!querySnapshot.isEmpty) {
                return querySnapshot.documents.first().getString("childEmail")
            }
            val querySnapshotChild = fireStoreService.queryDocuments("families", "childEmail", email)
            if (!querySnapshotChild.isEmpty) {
                return querySnapshotChild.documents.first().getString("guardianEmail")
            }
            return null
        } catch (e: Exception) {
            Log.e("ParentalControlViewModel", "getLinkedEmail failed: ${e.message}", e)
            _errorMessage.value = "Failed to get linked email: ${e.message}"
            return null
        }
    }

    fun isFeatureSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }
}