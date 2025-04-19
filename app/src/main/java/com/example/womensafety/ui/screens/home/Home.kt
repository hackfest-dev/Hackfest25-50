package com.example.womensafety.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.Screens
import com.example.womensafety.bottomNavItems
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.drawerNavItems
import com.example.womensafety.services.impl.AccountServiceImpl
import com.example.womensafety.services.impl.FireStoreServiceImpl
import com.example.womensafety.ui.screens.fakeCall.FakeCall
import com.example.womensafety.ui.screens.parental.ParentalControl
import com.example.womensafety.ui.screens.reportCrime.ReportCrime
import com.example.womensafety.ui.screens.news.News
import com.example.womensafety.ui.screens.parental.ParentalControlViewModel
import com.example.womensafety.ui.screens.parental.ParentalControlViewModelFactory
import com.example.womensafety.utility.NotificationUtils
import kotlinx.coroutines.launch
import com.example.womensafety.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    openAndPopUp: (String, String) -> Unit
) {
    val context = LocalContext.current
    val contactDao = AppDatabase.getDatabase(context).contactListDao()
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context, contactDao))


    val currentScreen by remember { viewModel.currentBottomScreen }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val parentalViewModel: ParentalControlViewModel = viewModel(
        factory = ParentalControlViewModelFactory(context,
            database = AppDatabase.getDatabase(context),
            fireStoreService = FireStoreServiceImpl(),
            accountService = AccountServiceImpl())
    )
    val user by parentalViewModel.user.collectAsState()

    // Initialize notification channel
    LaunchedEffect(Unit) {
        NotificationUtils.createNotificationChannel(context)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    drawerNavItems.forEach { item ->
                        DrawerItemBar(
                            selected = false,
                            screen = item,
                            onItemClick = {
                                openAndPopUp(item.dRoute, "home")
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            currentScreen.title,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        if (currentScreen.route in listOf("parental_control", "fake_call")) {
                            currentScreen.logo?.let { logoRes ->
                                Icon(
                                    painter = painterResource(logoRes),
                                    contentDescription = "${currentScreen.title} Logo",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    bottomNavItems.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            BottomAppBar(
                                selected = currentScreen.route == item.bRoute,
                                screen = item,
                                onItemClick = {
                                    Log.d("Navigation", "Navigating to: ${item.bRoute}")
                                    viewModel.navigateToBottomScreen(item)
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                when (currentScreen) {
                    Screens.BottomNavScreens.Home -> {

                        EmergencyCard(
                            title = "Are you in Emergency ?",
                            description = "Press the button below to get help reach soon to you."
                        )

                        Divider(Modifier.padding(horizontal = 8.dp, vertical = 8.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 16.dp)
                                .size(300.dp),
                            colors = CardDefaults.cardColors(Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFFFCDD2), shape = RoundedCornerShape(50)),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {viewModel.onSOSClickedButton()},
                                    modifier = Modifier.size(200.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.sos),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Cards(
                                text = "Track Me",
                                icon = painterResource(R.drawable.track),
                                onClick = {}
                            )
                            Cards(
                                text = "Community",
                                icon = painterResource(R.drawable.community),
                                onClick = {}
                            )
                            Cards(
                                text = "Helpline",
                                icon = painterResource(R.drawable.helpline),
                                onClick = {}
                            )
                        }
                        Spacer(Modifier.height(12.dp))

                    }
                    Screens.BottomNavScreens.parentalControl -> ParentalControl()
                    Screens.BottomNavScreens.fakeCall -> FakeCall()
                    Screens.BottomNavScreens.reportCrime -> ReportCrime()
                    Screens.BottomNavScreens.news -> News()
                }
            }
        }
    }

    BackHandler {
        if (currentScreen != Screens.BottomNavScreens.Home) {
            viewModel.navigateToBottomScreen(Screens.BottomNavScreens.Home)
        }
    }
}

@Composable
fun BottomAppBar(
    selected: Boolean,
    screen: Screens.BottomNavScreens,
    onItemClick: () -> Unit
) {
    val icon = if (selected) screen.bfilledIcon else screen.bIcon

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = screen.bTitle,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Text(screen.bTitle)
    }
}

@Composable
fun DrawerItemBar(
    selected: Boolean,
    screen: Screens.DrawerScreens,
    onItemClick: () -> Unit
) {
    val icon = screen.dIcon

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = screen.dTitle,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(screen.dTitle)
        }
    }
}

@Composable
fun Cards(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {

    Card(modifier = Modifier.size(100.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier.size(60.dp),
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(
                text = text,
                color = Color(0xFFF16D39),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

}


@Composable
fun EmergencyCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF16D39)
        ),
        shape = RoundedCornerShape(24.dp) // Rounded corners for the card
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Adds padding inside the card
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = title,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 20.sp,
                color = Color.White // Sets text color to white
            )
            Spacer(Modifier.height(8.dp)) // Adds spacing between title and description
            Text(
                modifier = Modifier.padding(4.dp),
                text = description,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
                color = Color.White // Sets text color to white
            )
        }
    }
}