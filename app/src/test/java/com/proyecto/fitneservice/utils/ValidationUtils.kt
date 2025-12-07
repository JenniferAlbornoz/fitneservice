package com.proyecto.fitneservice.utils

object ValidationUtils {


    fun isEmailValid(email: String): Boolean {
        if (email.isBlank()) return false
        return email.contains("@") && (email.endsWith(".cl") || email.endsWith(".com"))
    }


    fun isPasswordValid(password: String): Boolean {
        if (password.isBlank()) return false
        return password.any { it.isLetter() } && password.any { it.isDigit() }
    }


    fun doPasswordsMatch(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }
}