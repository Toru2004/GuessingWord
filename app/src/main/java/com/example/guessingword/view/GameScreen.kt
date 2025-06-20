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
    onGameOver: (String) -> Unit,
    onExit: () -> Unit
) {
    val lives by viewModel.lives.collectAsState()
    val guessedLetters by viewModel.guessedLetters.collectAsState()
    val gameResult by viewModel.gameResult.collectAsState()
    val context = LocalContext.current
    val displayWord = viewModel.getDisplayWord()

    var lastGuessCorrect by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(gameResult) {
        gameResult?.let {
            val finalWord = viewModel.currentWordToGuess
            onGameOver("$it|$finalWord")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lives left: $lives", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            // 🟢 Word Display + Feedback
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(tonalElevation = 6.dp, shape = MaterialTheme.shapes.large) {
                    Text(
                        text = displayWord,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                // ✅ Icon giữ kích thước cố định để không đẩy chữ
                Box(
                    modifier = Modifier
                        .size(32.dp), // ⬅️ Đảm bảo kích thước cố định
                    contentAlignment = Alignment.Center
                ) {
                    when (lastGuessCorrect) {
                        true -> Text("✅", style = MaterialTheme.typography.headlineMedium)
                        false -> Text("❌", style = MaterialTheme.typography.headlineMedium)
                        null -> {} // để trống, vẫn chiếm không gian
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Pick a letter:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            AlphabetGrid(
                guessedLetters = guessedLetters,
                onLetterClick = { letter ->
                    val isCorrect = viewModel.currentWordToGuess.contains(letter)
                    viewModel.guessLetter(letter)

                    val soundId = if (isCorrect) R.raw.correct else R.raw.wrong
                    MediaPlayer.create(context, soundId).start()

                    lastGuessCorrect = isCorrect

                    // Reset sau 1 giây
                    scope.launch {
                        delay(500L)
                        lastGuessCorrect = null
                    }
                }
            )
        }

        Button(
            onClick = onExit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(WindowInsets.statusBars.asPaddingValues()),
                    colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Menu", style = MaterialTheme.typography.labelLarge)
        }
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

