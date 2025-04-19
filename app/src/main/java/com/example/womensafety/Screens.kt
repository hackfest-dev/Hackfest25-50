package com.example.womensafety

import androidx.annotation.DrawableRes

sealed class Screens(val title: String, val route: String) {

    sealed class BottomNavScreens(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val bIcon: Int,
        @DrawableRes val bfilledIcon: Int,
        @DrawableRes val logo: Int? = null
    ) : Screens(bTitle, bRoute) {

        object Home : BottomNavScreens(
            bTitle = "Home",
            bRoute = "home",
            bIcon = R.drawable.home,
            bfilledIcon = R.drawable.home_filled
        )

        object parentalControl : BottomNavScreens(
            bTitle = "Guardian",
            bRoute = "parental_control",
            bIcon = R.drawable.parental_control,
            bfilledIcon = R.drawable.parental_control_filled,
            logo = R.drawable.parental_control_title_logo
        )

        object fakeCall : BottomNavScreens(
            bTitle = "Fake Call",
            bRoute = "fake_call",
            bIcon = R.drawable.fak_call,
            bfilledIcon = R.drawable.fake_call_filled,
            logo = R.drawable.fake_call_title_logo
        )

        object reportCrime : BottomNavScreens(
            bTitle = "Report",
            bRoute = "report_crime",
            bIcon = R.drawable.reprt_crime,
            bfilledIcon = R.drawable.report_crime_filled
        )

        object news : BottomNavScreens(
            bTitle = "News",
            bRoute = "news",
            bIcon = R.drawable.news,
            bfilledIcon = R.drawable.news_filled
        )
    }

    sealed class DrawerScreens(
        val dTitle: String,
        val dRoute: String,
        @DrawableRes val dIcon: Int
    ) : Screens(dTitle, dRoute) {

        object Settings : DrawerScreens(
            dTitle = "Settings",
            dRoute = "settings",
            dIcon = R.drawable.settings
        )

        object Developers : DrawerScreens(
            dTitle = "Developers",
            dRoute = "developers",
            dIcon = R.drawable.developers
        )

        object ContactList : DrawerScreens(
            dTitle = "Contacts",
            dRoute = "contacts_list",
            dIcon = R.drawable.contacts
        )

        object SignOut : DrawerScreens(
            dTitle = "Sign Out",
            dRoute = "sign_out",
            dIcon = R.drawable.sign_out
        )

        object Services: DrawerScreens(
            dTitle = "Services",
            dRoute = "services",
            dIcon = R.drawable.developers
        )
    }
}

val bottomNavItems = listOf(
    Screens.BottomNavScreens.Home,
    Screens.BottomNavScreens.parentalControl,
    Screens.BottomNavScreens.fakeCall,
    Screens.BottomNavScreens.reportCrime,
    Screens.BottomNavScreens.news
)

val drawerNavItems = listOf(
    Screens.DrawerScreens.Settings,
    Screens.DrawerScreens.Developers,
    Screens.DrawerScreens.ContactList,
    Screens.DrawerScreens.SignOut,
    Screens.DrawerScreens.Services
)