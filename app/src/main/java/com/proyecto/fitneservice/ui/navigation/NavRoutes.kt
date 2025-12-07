package com.proyecto.fitneservice.ui.navigation

sealed class NavRoute(val route: String) {

    data object Splash : NavRoute("splash")
    data object Onboarding : NavRoute("onboarding")
    data object Login : NavRoute("login")
    data object Register : NavRoute("register")


    data object Home : NavRoute("home")

    data object ForgotPassword : NavRoute("forgot_password")
    data object ResetPassword : NavRoute("reset_password")
    data object PasswordResetSuccess : NavRoute("password_reset_success")

}
