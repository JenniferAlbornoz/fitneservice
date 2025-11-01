package com.proyecto.fitneservice.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.proyecto.fitneservice.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen() {
    // Estados del perfil
    var nombre by remember { mutableStateOf("BENJAMIN") }
    var apellido by remember { mutableStateOf("MONASTERIO") }
    var bio by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Hombre") }
    val userEmail = "example@example.com"

    // Imagen
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
    }

    // Popup animado
    var showPopup by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Fondo blanco
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 🔹 Barra morada superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFFB3A0FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PERFIL",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Foto + Nombre / Apellido
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen de perfil
                Image(
                    painter = if (imageUri.value != null)
                        rememberAsyncImagePainter(imageUri.value)
                    else
                        painterResource(id = R.drawable.ic_fotoperfil),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { launcher.launch("image/*") },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text("NOMBRE", color = Color.Gray, fontSize = 14.sp)
                    Text(nombre, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("APELLIDOS", color = Color.Gray, fontSize = 14.sp)
                    Text(apellido, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 🔹 Biografía
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biografía") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${100 - bio.length} Caracteres Restantes",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Botones de género
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Mujer", "Hombre", "Prefiero No Decirlo").forEach { gender ->
                    Button(
                        onClick = { selectedGender = gender },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedGender == gender) Color.Black else Color.LightGray,
                            contentColor = if (selectedGender == gender) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(text = gender, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Email
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("EMAIL", color = Color.Gray, fontSize = 14.sp)
                Text(userEmail, color = Color.Black, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Botón Guardar Cambios
            Button(
                onClick = {
                    showPopup = true
                    scope.launch {
                        delay(2000)
                        showPopup = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp)
            ) {
                Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // 🔹 Popup animado
            AnimatedVisibility(
                visible = showPopup,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .background(Color(0xFF0DF20D), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✅ Cambios guardados correctamente",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
