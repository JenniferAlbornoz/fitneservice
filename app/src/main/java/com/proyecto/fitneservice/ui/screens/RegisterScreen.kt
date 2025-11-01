package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.data.UserPreferences
import com.proyecto.fitneservice.ui.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {

    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessPopup by remember { mutableStateOf(false) } // ✅ Estado del popup

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232323))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // 🔹 Título principal
            Text(
                text = "Crear Una Cuenta",
                color = Color(0xFF0DF20D),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(50.dp))

            // 🔹 Subtítulo
            Text(
                text = "¡Empecemos!",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(70.dp))

            // 🟣 Fondo morado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB3A0FF))
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico", color = Color.Black) },
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
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar Contraseña", color = Color.Black) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Términos de uso
            Text(
                text = "Al continuar, aceptas los Términos de uso y la\nPolítica de privacidad.",
                color = Color(0xFF0DF20D),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Botón principal con validaciones
            Button(
                onClick = {
                    when {
                        email.text.isBlank() || password.text.isBlank() || confirmPassword.text.isBlank() -> {
                            errorMessage = "Por favor, completa todos los campos."
                        }
                        password.text != confirmPassword.text -> {
                            errorMessage = "Las contraseñas no coinciden."
                        }
                        else -> {
                            errorMessage = ""
                            // ✅ Guardar usuario en DataStore
                            CoroutineScope(Dispatchers.IO).launch {
                                userPrefs.saveCredentials(email.text, password.text)
                            }
                            showSuccessPopup = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C3B3B)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(230.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Inscribirse",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // 🔴 Mensaje de error (si lo hay)
            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🟣 Iconos de redes sociales
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(45.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Enlace inferior
            Row {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF0DF20D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(NavRoute.Login.route)
                    }
                )
            }
        }

        // 🟢 Popup de confirmación
        if (showSuccessPopup) {
            AlertDialog(
                onDismissRequest = { showSuccessPopup = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessPopup = false
                            navController.navigate(NavRoute.ProfileSetup.route) {
                                popUpTo(NavRoute.Register.route) { inclusive = true }
                            }
                        }
                    ) {
                        Text(
                            "Continuar",
                            color = Color(0xFF0DF20D),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                title = {
                    Text(
                        "Registro Exitoso",
                        color = Color(0xFF0DF20D),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "Tu cuenta ha sido creada correctamente.\n¡Bienvenido a FitneService!",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                containerColor = Color(0xFF2C2C2C)
            )
        }
    }
}
