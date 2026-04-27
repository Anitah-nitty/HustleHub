package com.anitah.hustlehub.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val teal = Color(0xFF00E5C3)
    val navy = Color(0xFF0A0F1E)
    val navyMid = Color(0xFF0D1A35)
    val blue = Color(0xFF0066FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(navy)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Icon + Title
        Text(text = "💼", fontSize = 64.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "HustleHub",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = teal
        )

        Text(
            text = "Login to your account",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = teal) },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = teal)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = teal,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = teal,
                focusedContainerColor = navyMid,
                unfocusedContainerColor = navyMid
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = teal) },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = teal)
            },
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        color = teal,
                        fontSize = 12.sp
                    )
                }
            },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = teal,
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = teal,
                focusedContainerColor = navyMid,
                unfocusedContainerColor = navyMid
            ),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    Brush.horizontalGradient(colors = listOf(teal, blue)),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    // ── Firebase login logic ──────────────────────────
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = "Please fill in all fields"
                        return@Button
                    }
                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            isLoading = false
                            // Navigate to home on success
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                            errorMessage = e.message ?: "Login failed"
                        }
                },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = null
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = navy,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = navy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign up link
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 13.sp
            )
            TextButton(onClick = { navController.navigate("register") }) {
                Text(
                    text = "Sign Up",
                    color = teal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}