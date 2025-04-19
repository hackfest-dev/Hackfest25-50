package com.example.womensafety.ui.screens.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.HOME
import com.example.womensafety.LOG_IN
import com.example.womensafety.SIGN_UP
import com.example.womensafety.services.AccountService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogInViewModel(
    private val accountService: AccountService,
    private val context: Context
) : ViewModel() {

    //–– UI state
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun onLogInClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            try {
                accountService.signIn(email.value, password.value)

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid.isNullOrBlank()) {
                    _errorMsg.value = "Login succeeded but no UID found."
                } else {
                    prefs.edit().putString("user_id", uid).apply()
                    openAndPopUp(HOME, LOG_IN)
                }
            } catch (e: Exception) {
                _errorMsg.value = "Login failed: ${e.message}"
            } finally {
                _isLoading.value = false
                _errorMsg.value?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(SIGN_UP, LOG_IN)
    }
}
