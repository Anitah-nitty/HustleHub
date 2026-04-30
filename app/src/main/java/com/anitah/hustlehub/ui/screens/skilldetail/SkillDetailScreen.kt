package com.anitah.hustlehub.ui.screens.skilldetail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SkillDetailScreen(
    navController: NavController,
    title: String = "Logo Design",
    category: String = "Design",
    description: String = "Professional logo design service",
    price: String = "500",
    location: String = "Nairobi",
    postedBy: String = "user@email.com"
) {

    val teal = Color(0xFF00E5C3)
    val navy = Color(0xFF0A0F1E)
    val navyMid = Color(0xFF0D1A35)
    val blue = Color(0xFF0066FF)
    val context = LocalContext.current

    // Get seller name from email
    val sellerName = postedBy.substringBefore("@")
    val sellerInitial = if (sellerName.isNotEmpty()) sellerName.first().uppercaseChar().toString() else "U"

    // Get current logged in user
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserEmail = currentUser?.email ?: ""

    // Check if this skill belongs to the current user
    val isOwner = currentUserEmail == postedBy

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
                // Back button
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(text = "← Back", color = teal, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🛠️ Skill Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Everything you need to know",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Skill info card ─────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = navyMid)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(teal.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛠️", fontSize = 26.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            color = teal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "PRICE",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.4f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Ksh $price",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = teal
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "LOCATION",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.4f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "📍 $location",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Description card ────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = navyMid)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "About this Skill",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = teal
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Seller info card ────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = navyMid)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Seller avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(colors = listOf(teal, blue))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sellerInitial,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = navy
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = sellerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = postedBy,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Email button — opens email app
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(teal.copy(alpha = 0.15f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$postedBy")
                                putExtra(Intent.EXTRA_SUBJECT, "Interested in your skill: $title")
                                putExtra(Intent.EXTRA_TEXT, "Hi $sellerName, I am interested in your $title service listed on HustleHub.")
                            }
                            context.startActivity(intent)
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "✉ Email",
                            fontSize = 13.sp,
                            color = teal,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Hire Now button (only show if not the owner) ─────────────────
        if (!isOwner) {
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
                        // Opens email app with pre-filled hiring message
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$postedBy")
                            putExtra(Intent.EXTRA_SUBJECT, "I want to hire you for: $title")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Hi $sellerName,\n\nI found your \"$title\" service on HustleHub and I would like to hire you.\n\nPlease let me know your availability.\n\nThank you!"
                            )
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxSize(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = null
                ) {
                    Text(
                        text = "🤝 Hire Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = navy
                    )
                }
            }
        } else {
            // Show this if the skill belongs to the current user
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✅ This is your skill listing",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1E)
@Composable
fun SkillDetailScreenPreview() {
    SkillDetailScreen(rememberNavController())
}