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
            GameScreen(viewModel = viewModel) { result ->
                navController.navigate("result/$result")
            }
        }

        // Màn hình kết quả
        composable(
            "result/{outcome}",
            arguments = listOf(navArgument("outcome") { type = NavType.StringType })
        ) { backStackEntry ->
            val outcome = backStackEntry.arguments?.getString("outcome") ?: "lose"
            ResultScreen(result = outcome) {
                navController.popBackStack("start", inclusive = false)
                navController.navigate("start")
            }
        }
    }
}

