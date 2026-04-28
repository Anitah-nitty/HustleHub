package com.anitah.hustlehub.ui.screens.profile

import androidx.compose.foundation.background
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

@Composable
fun ProfileScreen(navController: NavController) {

    val teal = Color(0xFF00E5C3)
    val navy = Color(0xFF0A0F1E)
    val navyMid = Color(0xFF0D1A35)
    val blue = Color(0xFF0066FF)

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
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(teal, blue))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = navy
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Anitah",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "anitah@email.com",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    StatItem("12", "Skills", teal)
                    StatItem("4.9", "Rating", teal)
                    StatItem("32", "Reviews", teal)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "My Posted Skills",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileSkillItem("🎨", "Logo Design", "Ksh 500", navyMid, teal)
        ProfileSkillItem("💻", "Web Development", "Ksh 2000", navyMid, teal)
        ProfileSkillItem("📸", "Photography", "Ksh 1500", navyMid, teal)

        Spacer(modifier = Modifier.height(24.dp))

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
                ProfileOptionItem("✏️", "Edit Profile", teal)
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                ProfileOptionItem("🔒", "Change Password", teal)
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                ProfileOptionItem("🔔", "Notifications", teal)
                HorizontalDivider(color = Color.White.copy(alpha = 0.06f))
                ProfileOptionItem("ℹ️", "About", teal)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("🚪 Logout", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/* ───────────── REUSABLE COMPONENTS (RESTORED) ───────────── */

@Composable
fun StatItem(value: String, label: String, teal: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = teal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
    }
}

@Composable
fun ProfileSkillItem(
    icon: String,
    title: String,
    price: String,
    navyMid: Color,
    teal: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = navyMid)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
fun ProfileOptionItem(icon: String, label: String, teal: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, modifier = Modifier.weight(1f), color = Color.White)
        Text("→", color = teal)
    }
}

/* ───────────── PREVIEW RESTORED ───────────── */

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1E)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}