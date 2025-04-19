package com.example.womensafety

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.womensafety.services.AccountService
import com.example.womensafety.ui.service.Services
import com.example.womensafety.ui.screens.DevelopersScreen
import com.example.womensafety.ui.screens.SettingsScreen
import com.example.womensafety.ui.screens.contactList.ContactList
import com.example.womensafety.ui.screens.home.Home
import com.example.womensafety.ui.screens.login.LogInScreen
import com.example.womensafety.ui.screens.parental.ParentalControl
import com.example.womensafety.ui.screens.signup.SignUpScreen
import com.example.womensafety.ui.screens.splash.SplashScreen
import com.example.womensafety.ui.signout.SignOut
import com.example.womensafety.ui.theme.WomenSafetyTheme
import com.example.womensafety.utility.PermissionUtils

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun WomenSafetyApp(accountService: AccountService) {
    WomenSafetyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()


            NavHost(
                navController = appState.navController,
                startDestination = SPLASH
            ) {
                womenSafetyAppGraph(
                    appState,
                    accountService =  accountService,
                )
            }
        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        NavigationState(navController)
    }

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.womenSafetyAppGraph(
    appState: NavigationState,
    accountService: AccountService
) {
    composable(route = HOME) {
        Home(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(route = SETTINGS) {
        SettingsScreen()
    }
    composable(route = DEVELOPERS) {
        DevelopersScreen()
    }
    composable(route = CONTACTS_LIST) {
        ContactList(
            permissionUtils = PermissionUtils,
            openScreen = { route -> appState.navigate(route) },
            popUpScreen = { appState.popUp() }
        )
    }
    composable(route = PARENTAL_CONTROL) {
        ParentalControl()
    }

    composable(route = LOG_IN) {
        LogInScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            accountService = accountService
        )
    }
    composable(route = SIGN_UP) {
        SignUpScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
//            accountService = accountService
        )
    }

    composable(route = SPLASH) {
        SplashScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            accountService = accountService
        )
    }

    composable(route = SIGN_OUT) {
        SignOut(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            accountService = accountService
        )
    }

    composable(route = SERVICES) {
        Services(permission = PermissionUtils)
    }


}