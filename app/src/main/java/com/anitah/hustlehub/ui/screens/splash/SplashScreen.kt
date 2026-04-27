package com.anitah.hustlehub.ui.screens.splash

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anitah.hustlehub.navigation.ROUT_ONBOARDING
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavController){

    val coroutinecope = rememberCoroutineScope()

    coroutinecope.launch{
        delay(2000)
        navController.navigate(ROUT_ONBOARDING)
    }

    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0D1A35), Color(0xFF0A0F1E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Top-left glow blob
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = (-160).dp)
                .clip(CircleShape)
                .background(Color(0x2200E5C3))
                .align(Alignment.Center)
        )

        // Bottom-right glow blob
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = 80.dp, y = 160.dp)
                .clip(CircleShape)
                .background(Color(0x220066FF))
                .align(Alignment.Center)
        )

        // Main content
        Column(
            modifier = Modifier
                .alpha(alpha)
                .scale(scale),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Outer gradient ring
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF00E5C3), Color(0xFF0066FF))
                            )
                        )
                )
                // Inner dark circle with icon
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0A0F1E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💼", fontSize = 40.sp)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App name
            Text(
                text = "HustleHub",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF00E5C3),
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Divider line
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00E5C3), Color(0xFF0066FF))
                        )
                    )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "Own Your Craft.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.65f),
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(rememberNavController())
}