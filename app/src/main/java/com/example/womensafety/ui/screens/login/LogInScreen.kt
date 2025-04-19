package com.example.womensafety.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.R
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.impl.AccountServiceImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    openAndPopUp: (String, String) -> Unit,
    accountService: AccountService = AccountServiceImpl(),
    viewModel: LogInViewModel = viewModel(
        factory = LogInViewModelFactory(accountService, LocalContext.current)
    )
) {
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                text = "Welcome back! Glad to see you again!",
                fontSize = 32.sp,
                lineHeight = 32.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Gray
                    ),
                    value = email.value,
                    onValueChange = { viewModel.updateEmail(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text(text = "Enter your email") }
                )

                var passwordVisibility by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    value = password.value,
                    onValueChange = { viewModel.updatePassword(it) },
                    placeholder = { Text(text = "Enter your password") },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisibility) R.drawable.visibility_off else R.drawable.visibility_on
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.onLogInClick(openAndPopUp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Log In", fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Divider(modifier = Modifier.weight(1f).align(Alignment.CenterVertically))
                    Text(text = "Or Login with", modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterVertically))
                    Divider(modifier = Modifier.weight(1f).align(Alignment.CenterVertically))
                }

                // Social Login Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { /* TODO: Implement Facebook login */ }) {
                        Icon(
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 8.dp),
                            painter = painterResource(R.drawable.facebook),
                            contentDescription = "Facebook_logo",
                            tint = Color.Unspecified
                        )
                    }

                    IconButton(onClick = { /* TODO: Implement Apple login */ }) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp),
                            painter = painterResource(R.drawable.apple_logo),
                            contentDescription = "Apple_logo",
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            // Register Link
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp),
                    text = "Don't have an account?"
                )
                TextButton(
                    onClick = { viewModel.onSignUpClick(openAndPopUp) },
                    modifier = Modifier.padding()
                ) {
                    Text(text = "Register Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
