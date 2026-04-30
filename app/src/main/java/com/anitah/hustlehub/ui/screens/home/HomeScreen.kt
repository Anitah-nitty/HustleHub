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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anitah.hustlehub.navigation.ROUT_POSTSKILL
import com.anitah.hustlehub.navigation.ROUT_PROFILE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun HomeScreen(navController: NavController) {

    val teal = Color(0xFF00E5C3)
    val navy = Color(0xFF0A0F1E)
    val navyMid = Color(0xFF0D1A35)
    val blue = Color(0xFF0066FF)

    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.email?.substringBefore("@") ?: "User"

    var skillsList by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("skills").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetched = mutableListOf<Map<String, Any>>()
                for (skillSnapshot in snapshot.children) {
                    val title = skillSnapshot.child("title").getValue(String::class.java) ?: ""
                    val category = skillSnapshot.child("category").getValue(String::class.java) ?: ""
                    val description = skillSnapshot.child("description").getValue(String::class.java) ?: ""
                    val price = skillSnapshot.child("price").getValue(String::class.java) ?: ""
                    val location = skillSnapshot.child("location").getValue(String::class.java) ?: ""
                    val postedBy = skillSnapshot.child("postedBy").getValue(String::class.java) ?: ""
                    fetched.add(mapOf("title" to title, "category" to category, "description" to description, "price" to price, "location" to location, "postedBy" to postedBy))
                }
                skillsList = fetched
                isLoading = false
            }
            override fun onCancelled(error: DatabaseError) { isLoading = false }
        })
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
                .height(200.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0D2550), Color(0xFF0A1628), navy)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .offset(x = (-30).dp, y = (-30).dp)
                    .clip(CircleShape)
                    .background(teal.copy(alpha = 0.07f))
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(x = 270.dp, y = 10.dp)
                    .clip(CircleShape)
                    .background(blue.copy(alpha = 0.08f))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(colors = listOf(teal, blue))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💼", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HustleHub",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = teal
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(colors = listOf(teal, blue))
                        )
                        .clickable { navController.navigate(ROUT_PROFILE) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.first().uppercaseChar().toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = navy
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    text = "👋 Hey, $userName!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Find a skill or share yours today",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.5f)
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
            StatCard(
                icon = "🛠️",
                value = "${skillsList.size}",
                label = "Skills",
                teal = teal,
                navyMid = navyMid,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = "👥",
                value = "Active",
                label = "Community",
                teal = teal,
                navyMid = navyMid,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = "💰",
                value = "Earn",
                label = "Your Way",
                teal = teal,
                navyMid = navyMid,
                modifier = Modifier.weight(1f)
            )
        }

        // ── Post skill banner ───────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(colors = listOf(teal.copy(alpha = 0.2f), blue.copy(alpha = 0.2f)))
                )
                .clickable { navController.navigate(ROUT_POSTSKILL) }
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Got a skill? 💡",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Post it and start earning today",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(teal)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Post",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = navy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Section title ───────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Available Skills",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${skillsList.size} listed",
                fontSize = 12.sp,
                color = teal
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Loading spinner ─────────────────────────────────────────────
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = teal)
            }
        }

        // ── Empty state ─────────────────────────────────────────────────
        if (!isLoading && skillsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No skills posted yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "Be the first to post one!",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.3f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ── Skills list ─────────────────────────────────────────────────
        skillsList.forEach { skill ->
            val title = skill["title"] as? String ?: "Untitled"
            val category = skill["category"] as? String ?: "General"
            val price = skill["price"] as? String ?: "N/A"
            val location = skill["location"] as? String ?: "Unknown"
            val postedBy = skill["postedBy"] as? String ?: "Unknown"
            val description = skill["description"] as? String ?: ""

            ServiceCard(
                icon = "🛠️",
                title = title,
                description = "$category • 📍 $location • 💰 Ksh $price • by $postedBy",
                teal = teal,
                navyMid = navyMid,
                onClick = {
                    // Pass all skill data to SkillDetailScreen
                    navController.navigate(
                        "skilldetail/$title/$category/$description/$price/$location/$postedBy"
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ── Stat card ───────────────────────────────────────────────────────────────
@Composable
fun StatCard(
    icon: String,
    value: String,
    label: String,
    teal: Color,
    navyMid: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = navyMid)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = teal
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.4f)
            )
        }
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
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(teal.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
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

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(teal.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = "View", fontSize = 12.sp, color = teal, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F1E)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}