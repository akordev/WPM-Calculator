package com.example.typinganalyze

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.typinganalyze.login.LoginScreen
import com.example.typinganalyze.typing.TypingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel()
) {

    NavHost(navController = navController, startDestination = "username_screen") {
        composable("username_screen") {
            LoginScreen { username ->
                mainViewModel.userName.value = username
                navController.navigate("typing_screen")
            }
        }
        composable("typing_screen") { TypingScreen(mainViewModel.userName.value) }
    }
}