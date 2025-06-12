package com.example.guessingword.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class WordGuessViewModel : ViewModel() {

    // Danh sách từ cần đoán (bạn có thể thêm nhiều hơn tùy thích)
    private val wordList = listOf(
        "compose", "kotlin", "android", "studio", "lifecycle",
        "viewmodel", "state", "recompose", "coroutine", "navigation"
    )

    private val maxLives = 6

    private val _lives = MutableStateFlow(maxLives)
    val lives: StateFlow<Int> = _lives.asStateFlow()

    private val _guessedLetters = MutableStateFlow<List<Char>>(emptyList())
    val guessedLetters: StateFlow<List<Char>> = _guessedLetters.asStateFlow()

    private val _gameResult = MutableStateFlow<String?>(null)
    val gameResult: StateFlow<String?> = _gameResult.asStateFlow()

    // Từ hiện tại cần đoán
    var currentWordToGuess: String = wordList.random().uppercase()

    fun guessLetter(letter: Char) {
        if (letter in _guessedLetters.value || _gameResult.value != null) return

        _guessedLetters.value += letter
        if (letter !in currentWordToGuess) {
            _lives.value -= 1
        }
        checkGameStatus()
    }

    fun getDisplayWord(): String =
        currentWordToGuess.map { if (_guessedLetters.value.contains(it)) it else '_' }
            .joinToString(" ")

    private fun checkGameStatus() {
        if (_lives.value <= 0) {
            _gameResult.value = "lose"
        } else if (currentWordToGuess.all { _guessedLetters.value.contains(it) }) {
            _gameResult.value = "win"
        }
    }

    fun resetGame() {
        _lives.value = maxLives
        _guessedLetters.value = emptyList()
        _gameResult.value = null
        currentWordToGuess = wordList.random().uppercase()
    }
}
