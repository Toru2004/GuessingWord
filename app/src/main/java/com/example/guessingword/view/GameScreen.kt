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
import com.example.guessingword.viewModel.WordGuessViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    viewModel: WordGuessViewModel,
    onGameOver: (String) -> Unit
) {
    val lives by viewModel.lives.collectAsState()
    val guessedLetters by viewModel.guessedLetters.collectAsState()
    val gameResult by viewModel.gameResult.collectAsState()
    val context = LocalContext.current
    val displayWord = viewModel.getDisplayWord()

    LaunchedEffect(gameResult) {
        gameResult?.let { onGameOver(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Đưa toàn bộ vào giữa
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Lives left: $lives", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        Surface(tonalElevation = 6.dp, shape = MaterialTheme.shapes.large) {
            Text(
                text = displayWord,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text("Pick a letter:", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        AlphabetGrid(
            guessedLetters = guessedLetters,
            onLetterClick = { letter ->
                viewModel.guessLetter(letter)
                val soundId = if (viewModel.currentWordToGuess.contains(letter)) R.raw.correct else R.raw.wrong
                MediaPlayer.create(context, soundId).start()
            }
        )

//        if (showConfetti.value) {
//            ConfettiAnimation()
//        }
    }
}

@Composable
fun AlphabetGrid(
    guessedLetters: List<Char>,
    onLetterClick: (Char) -> Unit
) {
    val alphabet = ('A'..'Z').toList()
    val rows = alphabet.chunked(6)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { letter ->
                    val alreadyGuessed = guessedLetters.contains(letter)

                    Button(
                        onClick = { onLetterClick(letter) },
                        enabled = !alreadyGuessed,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(2.dp), // padding nhẹ giữa các nút
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = letter.toString(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

