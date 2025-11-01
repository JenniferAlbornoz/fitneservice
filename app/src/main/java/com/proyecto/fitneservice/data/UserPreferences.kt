package com.proyecto.fitneservice.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Instancia única de DataStore
val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        // Claves para los datos del usuario
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val NOMBRE_KEY = stringPreferencesKey("nombre")
        private val BIO_KEY = stringPreferencesKey("bio")
    }

    // Guardar datos de usuario (correo, nombre, bio)
    suspend fun saveUserData(email: String, nombre: String, bio: String) {
        context.userDataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[NOMBRE_KEY] = nombre
            prefs[BIO_KEY] = bio
        }
    }

    // Guardar credenciales (opcional, si quieres recordar login)
    suspend fun saveCredentials(email: String, password: String) {
        context.userDataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[PASSWORD_KEY] = password
        }
    }

    // Obtener datos de perfil
    val getUserData: Flow<Triple<String, String, String>> =
        context.userDataStore.data.map { prefs ->
            Triple(
                prefs[EMAIL_KEY] ?: "",
                prefs[NOMBRE_KEY] ?: "",
                prefs[BIO_KEY] ?: ""
            )
        }

    // Obtener credenciales (si las necesitas)
    val getCredentials: Flow<Pair<String?, String?>> =
        context.userDataStore.data.map { prefs ->
            Pair(prefs[EMAIL_KEY], prefs[PASSWORD_KEY])
        }

    // Borrar todos los datos
    suspend fun clearUserData() {
        context.userDataStore.edit { it.clear() }
    }
}
