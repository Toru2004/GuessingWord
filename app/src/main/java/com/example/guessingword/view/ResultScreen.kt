package com.example.guessingword.view

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.guessingword.R

@Composable
fun ResultScreen(
    result: String,
    finalWord: String,
    onNavigateToGame: () -> Unit,
    onNavigateToMenu: () -> Unit // 👈 Thêm callback
) {
    val context = LocalContext.current
    val showEffect = remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        showEffect.value = true
        val sound = if (result == "win") R.raw.win_sound else R.raw.lose_sound
        MediaPlayer.create(context, sound).start()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách giữa các thành phần
        ) {
            if (showEffect.value) {
                if (result == "win") {
                    WinTextEffect()
                } else {
                    LoseTextEffect()
                }
            }

            Text(
                text = "The word was: \"$finalWord\"",
                style = MaterialTheme.typography.titleLarge
            )

            // Nút "Play Again"
            Button(
                onClick = {
                    MediaPlayer.create(context, R.raw.touch).start()
                    onNavigateToGame()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 10.dp
                )
            ) {
                Text(
                    text = "Play Again",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White
                    )
                )
            }

            // ✅ Nút "Menu" ngay bên dưới
            OutlinedButton(
                onClick = {
                    MediaPlayer.create(context, R.raw.touch).start()
                    onNavigateToMenu()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
