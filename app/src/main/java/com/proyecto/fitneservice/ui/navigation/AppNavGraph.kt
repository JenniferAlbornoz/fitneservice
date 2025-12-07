package com.proyecto.fitneservice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.fitneservice.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavRoute.Splash.route
    ) {

        // 🟣 Splash
        composable(route = NavRoute.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(NavRoute.Onboarding.route) {
                        popUpTo(NavRoute.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 🟢 Onboarding
        composable(route = NavRoute.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        // 🔹 Login
        composable(route = NavRoute.Login.route) {
            LoginScreen(navController = navController)
        }

        // 🔵 Registro
        composable(route = NavRoute.Register.route) {
            RegisterScreen(navController = navController)
        }

        // 🏠 Home con bottom bar
        composable(route = NavRoute.Home.route) {
            HomeScreen(navController = navController)
        }
        // 🔒 Recuperación de contraseña
        composable(NavRoute.ForgotPassword.route) {
            ForgotPasswordScreen(navController) }
        composable(NavRoute.ResetPassword.route) {
            ResetPasswordScreen(navController) }
        composable(NavRoute.PasswordResetSuccess.route) {
            PasswordResetSuccessScreen(navController) }
    }
}
