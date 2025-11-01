package com.proyecto.fitneservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.proyecto.fitneservice.ui.navigation.AppNavGraph
import com.proyecto.fitneservice.ui.theme.FitneServiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplicamos tu tema global
            FitneServiceTheme {
                // Creamos el controlador de navegación
                val navController = rememberNavController()
                // Cargamos el gráfico de navegación
                AppNavGraph(navController)
            }
        }
    }
}
