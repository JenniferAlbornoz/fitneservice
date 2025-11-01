package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.fitneservice.R

@Composable
fun HomeScreen(navController: NavHostController) {

    var selectedTab by remember { mutableStateOf(0) }

    // 🟣 Títulos e íconos del menú inferior
    val tabs = listOf("Noticias", "Comunidad", "Actividad", "Progreso", "Perfil")
    val icons = listOf(
        R.drawable.ic_noticias,   // Noticias
        R.drawable.ic_comunidad,  // Comunidad
        R.drawable.ic_actividad,  // Actividad
        R.drawable.ic_progreso,   // Progreso
        R.drawable.ic_perfil      // Perfil
    )

    Scaffold(
        containerColor = Color(0xFF232323),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF3C3B3B)) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = icons[index]),
                                contentDescription = title,
                                tint = if (selectedTab == index) Color(0xFF0DF20D) else Color.White
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color(0xFF0DF20D) else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->

        // 🔹 Contenido central según la pestaña seleccionada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF232323))
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> NoticiasScreen()
                1 -> ComunidadScreen()
                2 -> ActividadScreen()
                3 -> ProgresoScreen()
                4 -> PerfilScreen()
            }
        }
    }
}
