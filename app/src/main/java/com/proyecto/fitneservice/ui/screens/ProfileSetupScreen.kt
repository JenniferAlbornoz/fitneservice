package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.ui.navigation.NavRoute

@Composable
fun ProfileSetupScreen(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Configuración de Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0DF20D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // ✅ Cuando el usuario termina la configuración,
                    // lo enviamos a la pantalla de bienvenida
                    navController.navigate(NavRoute.HomeWelcome.route) {
                        popUpTo(NavRoute.ProfileSetup.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C3B3B)),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text("Guardar", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

