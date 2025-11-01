package com.proyecto.fitneservice.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import com.proyecto.fitneservice.ui.theme.Shapes

// Esquema de color oscuro (modo dark)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,       // Verde neón FitneService
    secondary = SecondaryColor,   // Lavanda suave
    background = BackgroundDark,  // Fondo oscuro
    surface = ButtonColor,        // Botones / superficies
    onPrimary = TextPrimary,      // Texto sobre primario
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

// Esquema de color claro (modo light)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = Color.White,
    surface = SecondaryColor,
    onPrimary = BackgroundDark,
    onSecondary = BackgroundDark,
    onBackground = BackgroundDark,
    onSurface = BackgroundDark
)

@Composable
fun FitneServiceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Mantendremos los colores dinámicos (solo Android 12+)
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
