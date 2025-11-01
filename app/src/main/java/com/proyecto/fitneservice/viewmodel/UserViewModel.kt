package com.proyecto.fitneservice.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.fitneservice.data.UserPreferences
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de usuario para el proyecto FitneService.
 * Permite guardar y recuperar datos utilizando UserPreferences (DataStore).
 */
class UserViewModel(private val userPrefs: UserPreferences) : ViewModel() {

    // Guarda correo y contraseña
    fun saveCredentials(email: String, password: String) {
        viewModelScope.launch {
            userPrefs.saveCredentials(email, password)
        }
    }

    // Guarda nombre y biografía del perfil
    fun saveProfile(nombre: String, bio: String) {
        viewModelScope.launch {
            userPrefs.saveProfile(nombre, bio)
        }
    }

    // Guarda correo actualizado
    fun saveEmail(email: String) {
        viewModelScope.launch {
            userPrefs.saveEmail(email)
        }
    }

    // Guarda el género seleccionado
    fun saveGender(gender: String) {
        viewModelScope.launch {
            userPrefs.saveGender(gender)
        }
    }

    // Limpia los datos del usuario (logout o reset)
    fun clearUserData() {
        viewModelScope.launch {
            userPrefs.clearUserData()
        }
    }
}
