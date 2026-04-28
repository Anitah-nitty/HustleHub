package com.anitah.hustlehub.ui.screens.home

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
import com.anitah.hustlehub.navigation.ROUT_POSTSKILL
import com.anitah.hustlehub.navigation.ROUT_SKILLDETAIL

@Composable
fun HomeScreen(navController: NavController) {

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

        // ── Top header ──────────────────────────────────────────────────
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
                    text = "👋 Welcome Back!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "What are you hustling today?",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Section title ───────────────────────────────────────────────
        Text(
            text = "Popular Services",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Service cards ───────────────────────────────────────────────
        ServiceCard(
            icon = "🎨",
            title = "Graphic Design",
            description = "Logos, posters, branding & more",
            teal = teal,
            navyMid = navyMid,
            onClick = { navController.navigate(ROUT_SKILLDETAIL) }
        )
        ServiceCard(
            icon = "💻",
            title = "Web Development",
            description = "Websites, apps & dashboards",
            teal = teal,
            navyMid = navyMid,
            onClick = { navController.navigate(ROUT_SKILLDETAIL) }
        )
        ServiceCard(
            icon = "📸",
            title = "Photography",
            description = "Events, portraits & products",
            teal = teal,
            navyMid = navyMid,
            onClick = { navController.navigate(ROUT_SKILLDETAIL) }
        )
        ServiceCard(
            icon = "✍️",
            title = "Content Writing",
            description = "Articles, blogs & copywriting",
            teal = teal,
            navyMid = navyMid,
            onClick = { navController.navigate(ROUT_SKILLDETAIL) }
        )
        ServiceCard(
            icon = "📱",
            title = "Social Media",
            description = "Management & strategy",
            teal = teal,
            navyMid = navyMid,
            onClick = { navController.navigate(ROUT_SKILLDETAIL) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Post a service button ───────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(54.dp)
                .background(
                    Brush.horizontalGradient(colors = listOf(teal, blue)),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    navController.navigate(ROUT_POSTSKILL)
                },
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = null
            ) {
                Text(
                    text = "➕  Post Your Service",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = navy
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ── Reusable service card ───────────────────────────────────────────────────
@Composable
fun ServiceCard(
    icon: String,
    title: String,
    description: String,
    teal: Color,
    navyMid: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = navyMid)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(teal.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title and description
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Arrow
            Text(text = "→", color = teal, fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1E)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}