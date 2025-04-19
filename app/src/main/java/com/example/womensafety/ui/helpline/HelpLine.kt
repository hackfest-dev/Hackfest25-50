import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.model.PoliceHelpline
import com.example.womensafety.ui.helpline.HelpLineViewModel
import com.example.womensafety.ui.helpline.HelpLineViewModelFactory
import com.example.womensafety.utility.PermissionUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpLine(
    permissionUtils: PermissionUtils,
    openScreen: (String) -> Unit,
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // TODO: Initialize HelpLineViewModel with factory, e.g., HelpLineViewModelFactory
    val viewModel: HelpLineViewModel = viewModel(
        factory = HelpLineViewModelFactory(
            context,
            permissionUtils,
            AppDatabase.getDatabase(context).policeHelplineDao()
        )
    )

    // TODO: Implement contact picker launcher for adding helplines
    val helplinePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // TODO: Handle helpline picked from contact picker
            viewModel.handleHelplinePicked(result.data)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(8.dp),
                title = {
                    Text(
                        "Police Helpline List",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // TODO: Implement back navigation logic
                        viewModel.onBackClicked(popUpScreen)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Launch helpline picker or input form to add a new helpline
                    viewModel.onAddHelplineClicked(helplinePickerLauncher::launch)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .shadow(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Helpline"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // TODO: Replace with actual helpline list from ViewModel
            if (viewModel.helplineList.isEmpty()) {
                Text(
                    "No helplines added yet!",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                viewModel.helplineList.forEach { helpline ->
                    HelplineCard(helpline = helpline, viewModel = viewModel)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HelplineCard(helpline: PoliceHelpline, viewModel: HelpLineViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    var editDialog by remember { mutableStateOf(false) }
    var newCity by remember { mutableStateOf(helpline.city) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                clip = true
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF16D39)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Helpline",
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = helpline.city, fontWeight = FontWeight.Bold)
                Text(text = helpline.mobileNumber, style = MaterialTheme.typography.bodySmall)
                Text(text = helpline.email, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { showMenu = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options")
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        showMenu = false
                        editDialog = true
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        showMenu = false
                        // TODO: Implement delete helpline logic
                        viewModel.deleteHelpline(helpline)
                    }
                )
            }
        }
    }

    if (editDialog) {
        AlertDialog(
            onDismissRequest = { editDialog = false },
            title = { Text("Edit Helpline City") },
            text = {
                TextField(
                    value = newCity,
                    onValueChange = { newCity = it },
                    label = { Text("City") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    // TODO: Implement update helpline logic
                    viewModel.updateHelpline(helpline, newCity)
                    editDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { editDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}