package com.example.guessingword.view

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.guessingword.R

@Composable
fun StartScreen(onStartClick: () -> Unit) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(160.dp)
            )

            Text(
                text = "Guessing Word",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )

            Text(
                text = "Can you guess the word before you run out of lives?",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )

            Button(
                onClick = {
                    MediaPlayer.create(context, R.raw.touch).start()
                    onStartClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge, // Bo tròn hơn
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
                    text = "Start Game",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White
                    )
                )
            }

        }
    }
}
