package com.example.womensafety.ui.signout

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.womensafety.services.AccountService

class SignOutViewModel(
    private val context: Context,
    private val accountService: AccountService
) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun signOut() {
// Clear Firebase session
        accountService.signOut()
// Clear user_id from SharedPreferences
        sharedPreferences.edit().remove("user_id").apply()
    }
}