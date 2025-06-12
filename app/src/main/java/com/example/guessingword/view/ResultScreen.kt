package com.example.guessingword.view

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.guessingword.R

@Composable
fun ResultScreen(result: String, onPlayAgain: () -> Unit) {
    val context = LocalContext.current
    val showEffect = remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        if (result == "win") {
            showEffect.value = true
            MediaPlayer.create(context, R.raw.win_sound).start()
        } else {
            showEffect.value = true
            MediaPlayer.create(context, R.raw.lose_sound).start()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            if (showEffect.value) {
                if (result == "win") {
                    WinTextEffect()
                } else {
                    LoseTextEffect()
                }
            }

            Button(onClick = onPlayAgain) {
                Text("Play Again")
            }
        }
    }
}
