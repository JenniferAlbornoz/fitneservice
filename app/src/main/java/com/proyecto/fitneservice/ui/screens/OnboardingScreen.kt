package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.ui.navigation.NavRoute
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.shadow

@Composable
fun OnboardingScreen(navController: NavController) {

    val pages = listOf(
        OnboardPage(
            image = R.drawable.onboarding1,
            icon = R.drawable.ic_fitness,
            title = "Bienvenido a",
            subtitle = ""
        ),
        OnboardPage(
            image = R.drawable.onboarding2,
            icon = R.drawable.ic_wheelchair,
            title = "Comienza Tu Viaje Hacia Un Estilo De Vida Más Activo",
            subtitle = ""
        ),
        OnboardPage(
            image = R.drawable.onboarding3,
            icon = R.drawable.ic_family,
            title = "Una App Para Toda La Familia",
            subtitle = ""
        ),
        OnboardPage(
            image = R.drawable.onboarding4,
            icon = R.drawable.ic_heart,
            title = "No Importa La Edad, Desafíate A Ti Mismo",
            subtitle = ""
        )
    )

    var pageIndex by remember { mutableStateOf(0) }
    val currentPage = pages[pageIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = currentPage.image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón Skip arriba a la derecha (solo visible desde la 2° pantalla)
        if (pageIndex > 0) {
            TextButton(
                onClick = {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Onboarding.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp, end = 16.dp)
            ) {
                Text("Skip ▸", color = Color(0xFF0DF20D), fontWeight = FontWeight.Bold)
            }
        }

        // Contenido principal
        when (pageIndex) {
            0 -> { // Primera pantalla (icono + texto verde)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 350.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = currentPage.title,
                        color = Color(0xFF0DF20D),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Icon(
                        painter = painterResource(id = currentPage.icon),
                        contentDescription = "Logo FitneService",
                        tint = Color.Unspecified, // mantiene color original
                        modifier = Modifier
                            .size(250.dp)
                            .clickable { // 👈 al tocar pasa a la siguiente pantalla
                                pageIndex++
                            }
                    )
                }
            }

            else -> { // Pantallas 2, 3 y 4
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp), // 🔼 sube la caja morada un poco
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Caja violeta
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // 🔼 un poco más alta para balance visual
                            .background(Color(0xFFB3A0FF))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp) // 🔹 margen interior para que respire
                        ) {
                            Icon(
                                painter = painterResource(id = currentPage.icon),
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(60.dp) // 🔼 ícono más grande
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = currentPage.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp, // 🔼 texto más grande
                                lineHeight = 26.sp,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center // 🔹 centrado
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Indicadores de progreso
                            Row(horizontalArrangement = Arrangement.Center) {
                                repeat(pages.size) { index ->
                                    val color = if (index == pageIndex) Color.Black else Color.White.copy(alpha = 0.6f)
                                    Box(
                                        modifier = Modifier
                                            .padding(3.dp)
                                            .size(10.dp) // 🔼 un poco más visibles
                                            .background(color, shape = MaterialTheme.shapes.small)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔹 Botón “Siguiente” / “Empezar” transparente
                    Button(
                        onClick = {
                            if (pageIndex < pages.lastIndex) {
                                pageIndex++
                            } else {
                                navController.navigate(NavRoute.Login.route) {
                                    popUpTo(NavRoute.Onboarding.route) { inclusive = true }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent, // 🔹 Fondo transparente
                            contentColor = Color.White          // 🔹 Texto blanco
                        ),
                        border = BorderStroke(1.dp, Color.White), // 🔹 Borde blanco visible
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .height(50.dp)
                            .shadow(6.dp, RoundedCornerShape(30.dp)) // 🔹 (opcional) agrega una sombra sutil
                    ) {
                        Text(
                            text = if (pageIndex == pages.lastIndex) "Empezar" else "Siguiente",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                }
            }
        }
    }
}

data class OnboardPage(
    val image: Int,
    val icon: Int,
    val title: String,
    val subtitle: String
)
