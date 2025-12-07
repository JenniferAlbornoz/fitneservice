package com.proyecto.fitneservice.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val NOMBRE_KEY = stringPreferencesKey("nombre")
        private val BIO_KEY = stringPreferencesKey("bio")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val PHOTO_URI_KEY = stringPreferencesKey("photo_uri") // 📸 Nueva clave para la foto
    }

    // --- Credenciales ---
    suspend fun saveCredentials(email: String, password: String) {
        context.userDataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[PASSWORD_KEY] = password
        }
    }

    val getCredentials: Flow<Pair<String, String>> = context.userDataStore.data.map { prefs ->
        Pair(prefs[EMAIL_KEY] ?: "", prefs[PASSWORD_KEY] ?: "")
    }

    // --- Perfil (Auto-guardado individual) ---
    suspend fun saveName(nombre: String) {
        context.userDataStore.edit { it[NOMBRE_KEY] = nombre }
    }

    suspend fun saveBio(bio: String) {
        context.userDataStore.edit { it[BIO_KEY] = bio }
    }

    suspend fun saveEmail(email: String) {
        context.userDataStore.edit { it[EMAIL_KEY] = email }
    }

    suspend fun saveGender(gender: String) {
        context.userDataStore.edit { it[GENDER_KEY] = gender }
    }

    suspend fun savePhoto(uri: String) { // 📸 Guardar Foto
        context.userDataStore.edit { it[PHOTO_URI_KEY] = uri }
    }

    suspend fun updatePassword(password: String) {
        context.userDataStore.edit { prefs -> prefs[PASSWORD_KEY] = password }
    }

    // --- Obtener datos ---
    val getGender: Flow<String> = context.userDataStore.data.map { prefs ->
        prefs[GENDER_KEY] ?: ""
    }

    val getUserData: Flow<Map<String, String>> = context.userDataStore.data.map { prefs ->
        mapOf(
            "nombre" to (prefs[NOMBRE_KEY] ?: ""),
            "bio" to (prefs[BIO_KEY] ?: ""),
            "email" to (prefs[EMAIL_KEY] ?: ""),
            "gender" to (prefs[GENDER_KEY] ?: "Hombre"),
            "photo" to (prefs[PHOTO_URI_KEY] ?: "") // 📸 Recuperar Foto
        )
    }

    suspend fun clearUserData() {
        context.userDataStore.edit { prefs -> prefs.clear() }
    }

    // Función legacy para compatibilidad (puedes mantenerla o borrarla si actualizas todo)
    suspend fun saveProfile(nombre: String, bio: String, gender: String? = null) {
        saveName(nombre)
        saveBio(bio)
        gender?.let { saveGender(it) }
    }
}