package com.example.womensafety.ui.screens.reportCrime

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.utility.PermissionUtils

@Composable
fun ReportCrime(
    modifier: Modifier = Modifier,
    viewModel: ReportCrimeViewModel = viewModel(factory = ReportCrimeViewModelFactory(LocalContext.current, PermissionUtils))
) {
    val context = LocalContext.current
    val categories = listOf("Domestic Violence", "Dowry", "Harassment", "Other")
    val scrollState = rememberScrollState()

    // Media picker
    val mediaPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.setMediaUri(uri)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Report an Incident",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF6200EA)
        )

        // Location Section
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Location",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = viewModel.location.value?.let {
                        "${it.latitude}, ${it.longitude}${viewModel.city.value?.let { city -> " ($city)" } ?: ""}"
                    } ?: "No location set",
                    fontSize = 16.sp,
                    color = if (viewModel.location.value == null) Color(0xFF757575) else Color.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.fetchLocation() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
                    ) {
                        Text("Auto-Detect Location", fontSize = 14.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.adjustLocation(12.9716, 77.5946) // Example: Bengaluru
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Adjust Manually", fontSize = 14.sp)
                    }
                }
            }
        }

        // Category Section
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Incident Category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = viewModel.category.value == cat
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFFE91E63) else Color(0xFFB0BEC5),
                            animationSpec = tween(300)
                        )
                        val backgroundColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFFFCE4EC) else Color.White,
                            animationSpec = tween(300)
                        )
                        Card(
                            modifier = Modifier
                                .clickable { viewModel.category.value = cat }
                                .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = backgroundColor)
                        ) {
                            Text(
                                text = cat,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                color = Color(0xFF1A1A1A)
                            )
                        }
                    }
                }
            }
        }

        // Description Section
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Description (Optional)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                BasicTextField(
                    value = viewModel.description.value,
                    onValueChange = { viewModel.description.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        Box {
                            if (viewModel.description.value.isEmpty()) {
                                Text(
                                    text = "Enter details...",
                                    color = Color(0xFF757575),
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // Media Section
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Photo/Video (Optional)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    IconButton(
                        onClick = {
                            if (!PermissionUtils.isPermissionGranted(android.Manifest.permission.READ_MEDIA_IMAGES)) {
                                PermissionUtils.requestPermission(android.Manifest.permission.READ_MEDIA_IMAGES) { granted ->
                                    if (granted) mediaPicker.launch("image/* video/*")
                                }
                            } else {
                                mediaPicker.launch("image/* video/*")
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Media",
                            tint = Color(0xFF6200EA)
                        )
                    }
                }
                viewModel.mediaUri.value?.let {
                    Text(
                        text = "Media selected",
                        fontSize = 16.sp,
                        color = Color(0xFF388E3C),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        // Submit Button
        Button(
            onClick = { viewModel.submitReport() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !viewModel.isSubmitting.value,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            if (viewModel.isSubmitting.value) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Submit Report",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Spacer to ensure bottom content is visible
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ReportCrimeViewModelFactory(context: Context, permissionUtils: PermissionUtils): androidx.lifecycle.ViewModelProvider.Factory {
    return object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportCrimeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReportCrimeViewModel(context, permissionUtils) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}