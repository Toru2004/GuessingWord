package com.example.guessingword.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _lastGuessCorrect = MutableStateFlow<Boolean?>(null)
    val lastGuessCorrect: StateFlow<Boolean?> = _lastGuessCorrect.asStateFlow()

    // Từ hiện tại cần đoán
    var currentWordToGuess: String = wordList.random().uppercase()

    fun guessLetter(letter: Char) {
        if (letter in _guessedLetters.value ||
            _gameResult.value != null)
            return

        _guessedLetters.value += letter
        val isCorrect = letter in currentWordToGuess
        _lastGuessCorrect.value = isCorrect

        if (!isCorrect) {
            _lives.value -= 1
        }

        checkGameStatus()

        // Reset after 500ms
        viewModelScope.launch {
            kotlinx.coroutines.delay(500L)
            _lastGuessCorrect.value = null
        }
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
