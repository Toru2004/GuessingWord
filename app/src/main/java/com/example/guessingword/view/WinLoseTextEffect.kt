package com.example.guessingword.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import kotlin.math.sin

@Composable
fun WinTextEffect() {
    val scale = rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            tween(700, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    val alpha = rememberInfiniteTransition().animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(500),
            RepeatMode.Reverse
        )
    )

    Text(
        text = "🎉 YOU WIN! 🎉",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF4CAF50),
        modifier = Modifier
            .scale(scale.value)
            .graphicsLayer { this.alpha = alpha.value }
    )
}

@Composable
fun LoseTextEffect() {
    var time by remember { mutableFloatStateOf(0f) }

    val offsetX by animateFloatAsState(
        targetValue = sin(time * 10f) * 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(16),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {
        while (true) {
            time += 0.1f
            kotlinx.coroutines.delay(16L)
        }
    }

    Text(
        text = "💀 YOU LOSE! 💀",
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFE53935),
        modifier = Modifier.offset { IntOffset(offsetX.toInt(), 0) }
    )
}
