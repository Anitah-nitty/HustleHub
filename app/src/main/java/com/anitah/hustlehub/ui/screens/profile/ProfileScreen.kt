package com.anitah.hustlehub.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun ProfileScreen(navController: NavController) {

    val teal = Color(0xFF00E5C3)
    val navy = Color(0xFF0A0F1E)
    val navyMid = Color(0xFF0D1A35)
    val blue = Color(0xFF0066FF)

    val user = FirebaseAuth.getInstance().currentUser
    val userEmail = user?.email ?: "user@email.com"
    val userName = userEmail.substringBefore("@")
    val userInitial = userName.first().uppercaseChar().toString()

    var mySkills by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    // Dialog states
    var showEditProfile by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }

    // Edit profile states
    var displayName by remember { mutableStateOf(userName) }
    var savedDisplayName by remember { mutableStateOf(userName) }
    var editMessage by remember { mutableStateOf("") }

    // Change password states
    var newPassword by remember { mutableStateOf("") }
    var passwordMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val db = FirebaseDatabase.getInstance().reference
        val userUid = user?.uid ?: ""

        // Fetch saved display name
        if (userUid.isNotEmpty()) {
            db.child("users").child(userUid).child("displayName")
                .get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getValue(String::class.java)
                    if (!name.isNullOrEmpty()) {
                        savedDisplayName = name
                        displayName = name
                    }
                }
        }

        // Fetch skills posted by this user
        db.child("skills").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetched = mutableListOf<Map<String, Any>>()
                for (skillSnapshot in snapshot.children) {
                    val postedBy = skillSnapshot.child("postedBy").getValue(String::class.java) ?: ""
                    if (postedBy == userEmail) {
                        val title = skillSnapshot.child("title").getValue(String::class.java) ?: ""
                        val price = skillSnapshot.child("price").getValue(String::class.java) ?: ""
                        val category = skillSnapshot.child("category").getValue(String::class.java) ?: ""
                        fetched.add(mapOf("title" to title, "price" to price, "category" to category))
                    }
                }
                mySkills = fetched
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // ── Edit Profile Dialog ─────────────────────────────────────────────
    if (showEditProfile) {
        AlertDialog(
            onDismissRequest = { showEditProfile = false },
            containerColor = navyMid,
            title = {
                Text("✏️ Edit Profile", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Display Name", color = teal, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = teal,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = teal,
                            focusedContainerColor = navy,
                            unfocusedContainerColor = navy
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (editMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(editMessage, color = teal, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (displayName.isEmpty()) {
                        editMessage = "❌ Name cannot be empty"
                        return@TextButton
                    }
                    // Save to Firebase Realtime Database
                    val db = FirebaseDatabase.getInstance().reference
                    val userUid = user?.uid ?: return@TextButton
                    db.child("users").child(userUid).child("displayName")
                        .setValue(displayName)
                        .addOnSuccessListener {
                            savedDisplayName = displayName
                            editMessage = "✅ Profile updated!"
                        }
                        .addOnFailureListener {
                            editMessage = "❌ Update failed. Try again"
                        }
                }) {
                    Text("Save", color = teal, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEditProfile = false
                    editMessage = ""
                }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.5f))
                }
            }
        )
    }

    // ── Change Password Dialog ──────────────────────────────────────────
    if (showChangePassword) {
        AlertDialog(
            onDismissRequest = { showChangePassword = false },
            containerColor = navyMid,
            title = {
                Text("🔒 Change Password", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("New Password", color = teal, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Min 6 characters", color = Color.White.copy(alpha = 0.3f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = teal,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = teal,
                            focusedContainerColor = navy,
                            unfocusedContainerColor = navy
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    if (passwordMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(passwordMessage, color = teal, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPassword.length < 6) {
                        passwordMessage = "❌ Password too short"
                        return@TextButton
                    }
                    user?.updatePassword(newPassword)
                        ?.addOnSuccessListener {
                            passwordMessage = "✅ Password changed!"
                            newPassword = ""
                        }
                        ?.addOnFailureListener {
                            passwordMessage = "❌ Failed. Please re-login and try again"
                        }
                }) {
                    Text("Update", color = teal, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showChangePassword = false
                    passwordMessage = ""
                    newPassword = ""
                }) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.5f))
                }
            }
        )
    }

    // ── Notifications Dialog ────────────────────────────────────────────
    if (showNotifications) {
        AlertDialog(
            onDismissRequest = { showNotifications = false },
            containerColor = navyMid,
            title = {
                Text("🔔 Notifications", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "You have no new notifications.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showNotifications = false }) {
                    Text("OK", color = teal, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // ── About Dialog ────────────────────────────────────────────────────
    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            containerColor = navyMid,
            title = {
                Text("ℹ️ About HustleHub", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "HustleHub connects skilled individuals with people who need their services.",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Version 1.0.0", color = teal, fontSize = 12.sp)
                    Text("Built with Jetpack Compose & Firebase", color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("Close", color = teal, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(navy)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Hero header ─────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0D2550), navyMid)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .offset(x = (-40).dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(teal.copy(alpha = 0.07f))
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = 280.dp, y = 20.dp)
                    .clip(CircleShape)
                    .background(blue.copy(alpha = 0.08f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(teal, blue))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userInitial,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = navy
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Shows saved display name — updates after editing
                Text(
                    text = savedDisplayName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = userEmail,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.45f)
                )
            }
        }

        // ── Stats row ───────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard2(value = "${mySkills.size}", label = "Skills Posted", teal = teal, navyMid = navyMid, modifier = Modifier.weight(1f))
            StatCard2(value = "⭐ 5.0", label = "Rating", teal = teal, navyMid = navyMid, modifier = Modifier.weight(1f))
            StatCard2(value = "🟢", label = "Active", teal = teal, navyMid = navyMid, modifier = Modifier.weight(1f))
        }

        // ── My skills ───────────────────────────────────────────────────
        Text(
            text = "My Posted Skills",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (mySkills.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🛠️", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You haven't posted any skills yet",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            mySkills.forEach { skill ->
                val title = skill["title"] as? String ?: ""
                val price = skill["price"] as? String ?: ""
                val category = skill["category"] as? String ?: ""
                ProfileSkillItem(
                    icon = "🛠️",
                    title = title,
                    price = "Ksh $price • $category",
                    navyMid = navyMid,
                    teal = teal
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Account options ─────────────────────────────────────────────
        Text(
            text = "Account",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = navyMid)
        ) {
            Column {
                ProfileOptionItem("✏️", "Edit Profile", teal, onClick = { showEditProfile = true })
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                ProfileOptionItem("🔒", "Change Password", teal, onClick = { showChangePassword = true })
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                ProfileOptionItem("🔔", "Notifications", teal, onClick = { showNotifications = true })
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                ProfileOptionItem("ℹ️", "About", teal, onClick = { showAbout = true })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Logout button ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Red.copy(alpha = 0.15f))
                .clickable {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🚪 Logout",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red.copy(alpha = 0.9f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatCard2(value: String, label: String, teal: Color, navyMid: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = navyMid)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = teal)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 10.sp, color = Color.White.copy(alpha = 0.4f))
        }
    }
}

@Composable
fun ProfileSkillItem(icon: String, title: String, price: String, navyMid: Color, teal: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = navyMid)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(price, color = teal, fontSize = 12.sp)
            }
            Text("→", color = teal)
        }
    }
}

@Composable
fun ProfileOptionItem(icon: String, label: String, teal: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, modifier = Modifier.weight(1f), color = Color.White, fontSize = 14.sp)
        Text("→", color = teal, fontSize = 16.sp)
    }
}

@Composable
fun StatItem(value: String, label: String, teal: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = teal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1E)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}