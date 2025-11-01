package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.data.UserPreferences
import com.proyecto.fitneservice.ui.navigation.NavRoute

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val userData by userPrefs.getCredentials.collectAsState(initial = Pair("", ""))

    // Campos
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    // Mensaje de error
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232323))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---------- Encabezado ----------
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Iniciar Sesión",
                color = Color(0xFF0DF20D),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Bienvenido",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "¡Bienvenido de vuelta a FitneService!\nNos alegra verte nuevamente. " +
                        "Inicia sesión para seguir avanzando en tus objetivos de salud y bienestar.\n" +
                        "¡Tu progreso te está esperando!"+ "\n",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 25.dp)
            )

            // ---------- Franja morada (formulario) ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB3A0FF))
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color.Black) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", color = Color.Black) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Text(
                        text = "¿Has olvidado tu contraseña?",
                        color = Color(0xFFB3A0FF),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                navController.navigate(NavRoute.ForgotPassword.route)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ---------- Botón Iniciar ----------
            Button(
                onClick = {
                    val storedEmail = userData.first
                    val storedPassword = userData.second

                    when {
                        storedEmail.isNullOrEmpty() -> {
                            errorMessage = "No hay ninguna cuenta registrada."
                        }
                        email.text != storedEmail || password.text != storedPassword -> {
                            errorMessage = "Correo o contraseña incorrectos."
                        }
                        else -> {
                            errorMessage = ""
                            navController.navigate(NavRoute.Home.route)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C3B3B)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Iniciar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // 🔹 Mensaje de error (si hay)
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ---------- Texto inferior ----------
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "¿No tienes cuenta?", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Regístrate",
                    color = Color(0xFF0DF20D),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(NavRoute.Register.route)
                    }
                )
            }
        }
    }
}

