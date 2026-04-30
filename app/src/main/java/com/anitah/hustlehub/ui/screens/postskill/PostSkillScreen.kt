package com.anitah.hustlehub.ui.screens.postskill

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun PostSkillScreen(navController: NavController) {

    var skillTitle by remember { mutableStateOf("") }
    var skillCategory by remember { mutableStateOf("") }
    var skillDescription by remember { mutableStateOf("") }
    var skillPrice by remember { mutableStateOf("") }
    var skillLocation by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val teal = Color(0xFF00E5C3)
    val navy = Color(0xFF0A0F1E)
    val navyMid = Color(0xFF0D1A35)
    val blue = Color(0xFF0066FF)

    // Reusable colors for all fields
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = teal,
        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = teal,
        focusedContainerColor = navyMid,
        unfocusedContainerColor = navyMid,
        focusedPlaceholderColor = Color.White.copy(alpha = 0.3f),
        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.3f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(navy)
            .verticalScroll(rememberScrollState())
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0D2550), navyMid)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "🛠️ Post a Skill",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Share what you can do and start earning",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            Text("Skill Title", color = teal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = skillTitle,
                onValueChange = { skillTitle = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Logo Design") },
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Category", color = teal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = skillCategory,
                onValueChange = { skillCategory = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Design, Tech") },
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Description", color = teal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = skillDescription,
                onValueChange = { skillDescription = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Describe your skill...") },
                maxLines = 4,
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Price (Ksh)", color = teal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = skillPrice,
                onValueChange = { skillPrice = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 500") },
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Location", color = teal, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = skillLocation,
                onValueChange = { skillLocation = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Nairobi") },
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
            }

            if (successMessage.isNotEmpty()) {
                Text(text = successMessage, color = teal)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(
                        Brush.horizontalGradient(listOf(teal, blue)),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (skillTitle.isEmpty() ||
                            skillCategory.isEmpty() ||
                            skillDescription.isEmpty() ||
                            skillPrice.isEmpty() ||
                            skillLocation.isEmpty()
                        ) {
                            errorMessage = "Please fill in all fields"
                            successMessage = ""
                            return@Button
                        }

                        isLoading = true
                        val db = FirebaseDatabase.getInstance().reference
                        val user = FirebaseAuth.getInstance().currentUser

                        val skill = hashMapOf(
                            "title" to skillTitle,
                            "category" to skillCategory,
                            "description" to skillDescription,
                            "price" to skillPrice,
                            "location" to skillLocation,
                            "postedBy" to (user?.email ?: "unknown")
                        )

                        db.child("skills").push().setValue(skill)
                            .addOnSuccessListener {
                                isLoading = false
                                successMessage = "✅ Skill posted successfully!"
                                errorMessage = ""
                                skillTitle = ""
                                skillCategory = ""
                                skillDescription = ""
                                skillPrice = ""
                                skillLocation = ""
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = e.message ?: "Failed to post skill"
                                successMessage = ""
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
                        Text("Post Skill", color = navy, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostSkillScreenPreview() {
    PostSkillScreen(rememberNavController())
}