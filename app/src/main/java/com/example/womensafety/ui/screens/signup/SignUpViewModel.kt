package com.example.womensafety.ui.screens.signup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.HOME
import com.example.womensafety.LOG_IN
import com.example.womensafety.SIGN_UP
import com.example.womensafety.model.User
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.FireStoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val accountService: AccountService,
    private val context: Context,
    private val firestore: FireStoreService
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    val username = MutableStateFlow("")

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun updateUsername(newUsername: String) {
        username.value = newUsername
    }

    fun updateEmail(emailInput: String) {
        email.value = emailInput
    }

    fun updatePassword(passwordInput: String) {
        password.value = passwordInput
    }

    fun updateConfirmPassword(confirmPasswordInput: String) {
        confirmPassword.value = confirmPasswordInput
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            try {
                if (password.value != confirmPassword.value) {
                    Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // 1. Create auth account
                accountService.signUp(email.value, password.value)
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User creation failed")


                saveUserId(userId)
                openAndPopUp(HOME, SIGN_UP)
                Toast.makeText(context, "Sign-up successful", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Sign-up failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onLogInClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(LOG_IN, SIGN_UP)
    }

    private fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("user_id", userId).apply()
    }
}