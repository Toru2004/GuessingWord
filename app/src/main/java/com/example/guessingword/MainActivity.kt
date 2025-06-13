package com.example.guessingword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.guessingword.ui.theme.GuessingWordTheme
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guessingword.view.GameScreen
import com.example.guessingword.view.ResultScreen
import com.example.guessingword.view.StartScreen
import com.example.guessingword.viewModel.WordGuessViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuessingWordTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                WordGuessApp()
            }
        }
    }
}

@Composable
fun WordGuessApp() {
    val navController = rememberNavController()
    val viewModel: WordGuessViewModel = viewModel()

    NavHost(navController, startDestination = "start") {
        // Màn hình bắt đầu
        composable("start") {
            StartScreen {
                viewModel.resetGame()
                navController.navigate("game")
            }
        }

        // Màn hình chơi game
        composable("game") {
            GameScreen(
                viewModel = viewModel,
                onGameOver = { result ->
                    navController.navigate("result/$result")
                },
                onExit = {
                    navController.popBackStack("start", inclusive = false)
                    navController.navigate("start")
                }
            )
        }


        // Màn hình kết quả
        composable(
            "result/{outcome}",
            arguments = listOf(navArgument("outcome") { type = NavType.StringType })
        ) { backStackEntry ->
            val fullResult = backStackEntry.arguments?.getString("outcome") ?: "lose|unknown"
            val (outcome, word) = fullResult.split("|").let {
                Pair(it.getOrElse(0) { "lose" }, it.getOrElse(1) { "unknown" })
            }

            ResultScreen(
                result = outcome,
                finalWord = word,
                onNavigateToGame = {
                    viewModel.resetGame()
                    navController.navigate("game") {
                        popUpTo("start") { inclusive = false }
                    }
                },
                onNavigateToMenu = {
                    navController.popBackStack("start", inclusive = false)
                    navController.navigate("start")
                }
            )
        }


    }
}

