package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.data.UserPreferences
import com.proyecto.fitneservice.ui.navigation.NavRoute
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun HomeWelcomeScreen(navController: NavController) {

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    // Estados para los datos del usuario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

// app/src/main/java/com/proyecto/fitneservice/ui/screens/HomeWelcomeScreen.kt

// ... (imports)

    @Composable
    fun HomeWelcomeScreen(navController: NavController) {

        val context = LocalContext.current
        val userPrefs = remember { UserPreferences(context) }

        // Estados para los datos del usuario
        var nombre by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }

        // Cargar los datos desde DataStore
        LaunchedEffect(Unit) {
            // Corregido: Se accede a los valores del Map por clave
            userPrefs.getUserData.collectLatest { userDataMap ->
                nombre = userDataMap["nombre"] ?: ""
                email = userDataMap["email"] ?: ""
            }
        }

        // Mostrar nombre si existe, si no, mostrar correo
        // Se usa el email si el nombre está en blanco
        val saludo = if (nombre.isNotBlank()) nombre else email.takeIf { it.isNotBlank() } ?: "usuario"

        // ... (resto de la función sin cambios)

    }

    // Mostrar nombre si existe, si no, mostrar correo
    val saludo = if (nombre.isNotBlank()) nombre else email

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232323)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔹 Mensaje de bienvenida dinámico
            Text(
                text = "¡Hola, $saludo!",
                color = Color(0xFF0DF20D),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenido a FitneService 💪\n" +
                        "Prepárate para seguir superándote cada día.",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🔘 Botón para continuar
            Button(
                onClick = { navController.navigate(NavRoute.Home.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C3B3B)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Comenzar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
