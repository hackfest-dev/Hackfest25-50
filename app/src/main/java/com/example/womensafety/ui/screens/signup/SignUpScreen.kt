package com.example.womensafety.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.womensafety.services.AccountService
import com.example.womensafety.services.impl.AccountServiceImpl
import com.example.womensafety.R
import com.example.womensafety.services.impl.FireStoreServiceImpl

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
) {

    val viewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(
            accountService = AccountServiceImpl(), LocalContext.current,
            firestore = FireStoreServiceImpl())
    )

    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val username = viewModel.username.collectAsState()
    val confirmPassword = viewModel.confirmPassword.collectAsState()


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
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                text = "Hello! Register to get Started",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username.value,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email.value,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp)
                )

                var passwordVisibility by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password.value,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = { Text("Password") },
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

                var confirmPasswordVisibility by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword.value,
                    onValueChange = { viewModel.updateConfirmPassword(it) },
                    label = { Text("Confirm password") },
                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                            Icon(
                                painter = painterResource(
                                    if (confirmPasswordVisibility) R.drawable.visibility_off else R.drawable.visibility_on
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.onSignUpClick(openAndPopUp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Register")
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

                // Social Login Buttons (Facebook, Apple, etc.)
            }

            // Login redirection
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = "Already have an account?")
                TextButton(onClick = { viewModel.onLogInClick(openAndPopUp) }) {
                    Text(text = "Login Now")
                }
            }
        }
    }
}
