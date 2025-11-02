// app/src/main/java/com/proyecto/fitneservice/data/UserPreferences.kt

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
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val NOMBRE_KEY = stringPreferencesKey("nombre")
        private val BIO_KEY = stringPreferencesKey("bio")
        private val GENDER_KEY = stringPreferencesKey("gender") // ✅ Nueva clave persistente
    }

    // ✅ Guardar credenciales (Registro / Login)
    suspend fun saveCredentials(email: String, password: String) {
        context.userDataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[PASSWORD_KEY] = password
        }
    }

    // ✅ Obtener credenciales
    val getCredentials: Flow<Pair<String, String>> = context.userDataStore.data.map { prefs ->
        Pair(
            prefs[EMAIL_KEY] ?: "",
            prefs[PASSWORD_KEY] ?: ""
        )
    }

    // ✅ Guardar perfil (nombre, bio y género)
    suspend fun saveProfile(nombre: String, bio: String, gender: String? = null) {
        context.userDataStore.edit { prefs ->
            prefs[NOMBRE_KEY] = nombre
            prefs[BIO_KEY] = bio
            gender?.let { prefs[GENDER_KEY] = it } // guarda si se pasa
        }
    }

    // ✅ Guardar email (si se edita)
    suspend fun saveEmail(email: String) {
        context.userDataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
        }
    }

    // ✅ Guardar género individualmente
    suspend fun saveGender(gender: String) {
        context.userDataStore.edit { prefs ->
            prefs[GENDER_KEY] = gender
        }
    }

    // ✅ Función para actualizar solo la contraseña (Fix ResetPasswordScreen)
    suspend fun updatePassword(password: String) {
        context.userDataStore.edit { prefs ->
            prefs[PASSWORD_KEY] = password
        }
    }

    // ✅ Obtener género
    val getGender: Flow<String> = context.userDataStore.data.map { prefs ->
        prefs[GENDER_KEY] ?: ""
    }

    // ✅ Obtener perfil completo (nombre, bio, email, género)
    val getUserData: Flow<Map<String, String>> = context.userDataStore.data.map { prefs ->
        mapOf(
            "nombre" to (prefs[NOMBRE_KEY] ?: ""),
            "bio" to (prefs[BIO_KEY] ?: ""),
            "email" to (prefs[EMAIL_KEY] ?: ""),
            "gender" to (prefs[GENDER_KEY] ?: "")
        )
    }
    // ✅ Limpiar todos los datos del usuario
    suspend fun clearUserData() {
        context.userDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}