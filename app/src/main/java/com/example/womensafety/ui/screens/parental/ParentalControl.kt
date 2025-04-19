package com.example.womensafety.ui.screens.parental

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.model.ContactNumbers
import com.example.womensafety.model.User
import com.example.womensafety.services.impl.AccountServiceImpl
import com.example.womensafety.services.impl.FireStoreServiceImpl
import com.example.womensafety.utility.GuardianCommUtils
import com.example.womensafety.utility.NotificationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@SuppressLint("NewApi")
@Composable
fun ParentalControl(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: ParentalControlViewModel = viewModel(
        factory = ParentalControlViewModelFactory(context, AppDatabase.getDatabase(context),
            FireStoreServiceImpl(),
            AccountServiceImpl())
    )

    // State Collection
    val user by viewModel.user.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val selectedContact by viewModel.selectedContact.collectAsState()
    val otpSent by viewModel.otpSent.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val checkInStatus by viewModel.checkInStatus.collectAsState()
    val checkInRequest by viewModel.checkInRequest.collectAsState()
    val bargeInRequest by viewModel.bargeInRequest.collectAsState()
    val bargeInStatus by viewModel.bargeInStatus.collectAsState()
    val cancellationPending by viewModel.cancellationPending.collectAsState()
    val isLinking by viewModel.isLinking.collectAsState()
    val role by viewModel.role.collectAsState()
    val linkEstablished by viewModel.linkEstablished.collectAsState()
    val showSuccessMessage by viewModel.showSuccessMessage.collectAsState()
    val timerSeconds by viewModel.timerSeconds.collectAsState()
    var otpInput by remember { mutableStateOf("") }

    // Initialize Notification Channel
    LaunchedEffect(Unit) {
        NotificationUtils.createNotificationChannel(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Device Compatibility Warning
        if (!viewModel.isFeatureSupported()) {
            Text(
                text = "This feature is not supported on your device. Please update your Android version.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Role Selection Slider (Hidden when link is established)
        AnimatedVisibility(visible = !linkEstablished) {
            RoleSelector(
                role = role,
                onRoleSelected = {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        viewModel.switchRole(it)
//                    }
                }
            )
        }

        // Link Status Badge
        AnimatedVisibility(visible = linkEstablished) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Link Active",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        RolePanel(
            role = role,
            user = user,
            contacts = contacts,
            selectedContact = selectedContact,
            checkInRequest = checkInRequest,
            checkInStatus = checkInStatus,
            bargeInRequest = bargeInRequest,
            bargeInStatus = bargeInStatus,
            cancellationPending = cancellationPending,
            timerSeconds = timerSeconds,
            isLinking = isLinking,
            linkEstablished = linkEstablished,
            onSelectContact = { viewModel.selectContact(it) },
            onRequestOTP = {/* viewModel.requestOTP()*/ },
            onRequestLinkCancellation = {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    viewModel.requestLinkCancellation()
//                }
            },
            onRespondToCheckIn = { isOkay -> viewModel.respondToCheckIn(isOkay) },
            onSendCheckInRequest = { viewModel.sendCheckInRequest() },
            onRequestLocation = { viewModel.requestChildLocation() },
            onRespondToLocationRequest = { viewModel.respondToLocationRequest() },
            onSendBargeInRequest = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    viewModel.sendBargeInRequest()
                }
            },
            onRespondToBargeIn = { authorized -> viewModel.respondToBargeIn(authorized) },
            onEscalateBargeIn = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    viewModel.escalateBargeIn()
                }
            },
            context = context
        )

        OtpFlow(
            otpSent = otpSent,
            selectedContact = selectedContact,
            linkEstablished = linkEstablished,
            otpInput = otpInput,
            onOtpInputChange = { otpInput = it },
            onVerify = {
//                if (linkEstablished) viewModel.verifyCancellationOTP(otpInput)
//                else viewModel.verifyOTP(otpInput)
            }
        )

        LinkEstablishedAnimation(showSuccessMessage, role, selectedContact, viewModel::hideSuccessMessage)
        ErrorDisplay(errorMessage, viewModel::clearError)
    }
}

@Composable
private fun RoleSelector(role: Int, onRoleSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onRoleSelected(0) },
            colors = CardDefaults.cardColors(
                containerColor = if (role == 0) Color(0xFF1976D2) else MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = "Child",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (role == 0) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onRoleSelected(1) },
            colors = CardDefaults.cardColors(
                containerColor = if (role == 1) Color(0xFFC2185B) else MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = "Guardian",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (role == 1) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RolePanel(
    role: Int,
    user: User?,
    contacts: List<ContactNumbers>,
    selectedContact: ContactNumbers?,
    checkInRequest: Boolean,
    checkInStatus: String?,
    bargeInRequest: Boolean,
    bargeInStatus: String?,
    cancellationPending: Boolean,
    timerSeconds: Int?,
    isLinking: Boolean,
    linkEstablished: Boolean,
    onSelectContact: (ContactNumbers) -> Unit,
    onRequestOTP: () -> Unit,
    onRequestLinkCancellation: () -> Unit,
    onRespondToCheckIn: (Boolean) -> Unit,
    onSendCheckInRequest: () -> Unit,
    onRequestLocation: () -> Unit,
    onRespondToLocationRequest: () -> Unit,
    onSendBargeInRequest: () -> Unit,
    onRespondToBargeIn: (Boolean) -> Unit,
    onEscalateBargeIn: () -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (linkEstablished) {
            val linkedPhone = if (role == 0) user?.guardianPhoneNumber else runBlocking {
                GuardianCommUtils.getLinkedChild(context, user?.phoneNumber ?: "", FireStoreServiceImpl())
            }
            val contactName = contacts.find { it.phoneNumber == linkedPhone }?.name ?: if (role == 0) "Guardian" else "Child"
            LinkedContactCard(contactName, if (role == 0) "Child" else "Guardian")

            if (role == 0) {
                // Child Panel (Linked)
                checkInStatus?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                // Barge-In Request Dialog
                if (bargeInRequest) {
                    AlertDialog(
                        onDismissRequest = { onRespondToBargeIn(false) },
                        title = { Text("Urgent: Barge-In Request") },
                        text = { Text("Guardian is requesting immediate access to your location. Authorize?") },
                        confirmButton = {
                            Button(onClick = { onRespondToBargeIn(true) }) {
                                Text("Authorize")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { onRespondToBargeIn(false) }) {
                                Text("Deny")
                            }
                        }
                    )
                }
                bargeInStatus?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                // Cancel Link Button
                Button(
                    onClick = onRequestLinkCancellation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    enabled = !cancellationPending
                ) {
                    Text(if (cancellationPending) "Cancellation Pending" else "Cancel Link")
                }
            } else {
                // Guardian Panel (Linked)
                Button(
                    onClick = onSendCheckInRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Request Check-In")
                }
                Button(
                    onClick = onRequestLocation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Request Child Location")
                }
                Button(
                    onClick = onSendBargeInRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Barge In (Emergency)")
                }
                if (bargeInStatus == "No response. Escalate to barge-in?") {
                    Button(
                        onClick = onEscalateBargeIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("Escalate Barge-In")
                    }
                }
                Button(
                    onClick = onRequestLinkCancellation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    enabled = !cancellationPending
                ) {
                    Text(if (cancellationPending) "Cancellation Pending" else "Cancel Link")
                }
                checkInStatus?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                bargeInStatus?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else if (isLinking) {
            selectedContact?.let { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = contact.name,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = contact.phoneNumber)
                    }
                }
            }
        } else {
            // Unlinked State
            if (role == 0) {
                // Child Panel (Unlinked)
                if (contacts.isEmpty()) {
                    Text(
                        text = "No contacts available. Add contacts first.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn {
                        items(contacts) { contact ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { onSelectContact(contact) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (contact == selectedContact)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = contact.name,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = contact.phoneNumber)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    selectedContact?.let {
                        Button(onClick = onRequestOTP) {
                            Text("Send OTP to ${it.name}")
                        }
                    }
                }
            } else {
                // Guardian Panel (Unlinked)
                Text(
                    text = "No child linked. Set a child first.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun OtpFlow(
    otpSent: Boolean,
    selectedContact: ContactNumbers?,
    linkEstablished: Boolean,
    otpInput: String,
    onOtpInputChange: (String) -> Unit,
    onVerify: () -> Unit
) {
    if (otpSent) {
        selectedContact?.let { contact ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "OTP sent to ${contact.name} (${contact.phoneNumber})",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = otpInput,
                    onValueChange = onOtpInputChange,
                    label = { Text(if (linkEstablished) "Enter Cancellation OTP" else "Enter OTP") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onVerify) {
                    Text(if (linkEstablished) "Verify Cancellation" else "Verify OTP")
                }
            }
        }
    }
}

@Composable
private fun LinkEstablishedAnimation(
    showSuccessMessage: Boolean,
    role: Int,
    selectedContact: ContactNumbers?,
    hideSuccessMessage: () -> Unit
) {
    AnimatedVisibility(
        visible = showSuccessMessage,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LaunchedEffect(showSuccessMessage) {
            delay(2000)
            hideSuccessMessage()
        }
        val message = if (role == 0) {
            "Hurray! Now you are a child of ${selectedContact?.name}!"
        } else {
            "Hurray! Now you are a guardian of ${selectedContact?.name}!"
        }
        Text(
            text = message,
            color = if (role == 0) Color(0xFF1976D2) else Color(0xFFC2185B),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ErrorDisplay(errorMessage: String?, clearError: () -> Unit) {
    errorMessage?.let {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = clearError) {
                Text("Dismiss")
            }
        }
    }
}

@Composable
private fun LinkedContactCard(contactName: String, role: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (role == "Child") Color(0xFF1976D2) else Color(0xFFC2185B)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (role == "Child") "Guardian: $contactName" else "Child: $contactName",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (role == "Child") "You are under their protection" else "You are responsible for their safety",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}