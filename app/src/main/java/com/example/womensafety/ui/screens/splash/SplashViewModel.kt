package com.example.womensafety.ui.screens.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.womensafety.HOME
import com.example.womensafety.LOG_IN
import com.example.womensafety.SPLASH
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.model.PoliceHelpline
import com.example.womensafety.services.AccountService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val context: Context,
    private val accountService: AccountService
) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun checkAuthState(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            delay(2000) // 2-second delay for splash screen
            if (isUserLoggedIn()) {
                // User is signed in, navigate to Home
                openAndPopUp(HOME, SPLASH)
            } else {
                // No user signed in, sign out and navigate to Login
                signOut()
                openAndPopUp(LOG_IN, SPLASH)
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val userId = sharedPreferences.getString("user_id", null)
        val currentUser = auth.currentUser
        Log.d("SplashViewModel", "userId: $userId, currentUser: ${currentUser?.uid}")
        return userId != null && currentUser != null && currentUser.uid == userId
    }

    private fun signOut() {
        // Clear Firebase session
        accountService.signOut()
        // Clear user_id from SharedPreferences
        sharedPreferences.edit().remove("user_id").apply()
    }

    fun populatePoliceHelplines() {
        viewModelScope.launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val helplineCount = db.policeHelplineDao().getHelplineCount()
                if (helplineCount == 0) {
                    val contacts = listOf(
                        PoliceHelpline(
                            city = "BENGALURU",
                            mobileNumber = "9480802400",
                            email = "dcblore@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "BELAGAVI",
                            mobileNumber = "9480804000",
                            email = "dcbgm@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "MYSURU",
                            mobileNumber = "9480805000",
                            email = "dcmys@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "UDUPI",
                            mobileNumber = "9480805400",
                            email = "dcudp@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "KALABURAGI",
                            mobileNumber = "9480803500",
                            email = "dcgba@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "DAVANAGERE",
                            mobileNumber = "9480803200",
                            email = "dcdvg@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "SHIVAMOGGA",
                            mobileNumber = "9480803300",
                            email = "dcshi@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "TUMKUR",
                            mobileNumber = "9480802900",
                            email = "dctkr@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "CHITRADURGA",
                            mobileNumber = "9480803100",
                            email = "dcctr@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "BALLARI",
                            mobileNumber = "9480803000",
                            email = "dcblr@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "VIJAYAPURA",
                            mobileNumber = "9480804200",
                            email = "dcbjp@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "KOPPAL",
                            mobileNumber = "9480803700",
                            email = "dckpl@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "RAICHUR",
                            mobileNumber = "9480803800",
                            email = "dcrcr@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "RAMANAGARA",
                            mobileNumber = "9480802800",
                            email = "dcrmn@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "HASSAN",
                            mobileNumber = "9480804700",
                            email = "dchsn@ksp.gov.in"
                        ),
                        PoliceHelpline(
                            city = "CHIKKAMAGALUR",
                            mobileNumber = "9480805100",
                            email = "dcckm@ksp.gov.in"
                        )
                    )
                    db.policeHelplineDao().insertAll(contacts)
                    Log.d("SplashViewModel", "Populated ${contacts.size} police helplines")
                } else {
                    Log.d("SplashViewModel", "Helplines already populated, count: $helplineCount")
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Failed to populate police helplines: ${e.message}", e)
            }
        }
    }
}