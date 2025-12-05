package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.ui.navigation.NavRoute
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun PasswordResetSuccessScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232323)), // Fondo oscuro (BackgroundDark)
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Título: Contraseña Restablecida (Verde neón)
            Text(
                text = "Contraseña Restablecida",
                color = Color(0xFF0DF20D), // PrimaryColor
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Icono de Check (Confirmación)
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Check",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Descripción
            Text(
                text = "Tu contraseña se ha restablecido correctamente.\nHaz clic abajo para iniciar sesión automáticamente.",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Continue (Fondo ButtonColor)
            Button(
                onClick = {
                    // Navega a Login y elimina esta pantalla del back stack
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.PasswordResetSuccess.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C3B3B)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}