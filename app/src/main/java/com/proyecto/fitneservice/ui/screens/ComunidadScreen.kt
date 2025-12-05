package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.ui.theme.BackgroundDark
import com.proyecto.fitneservice.ui.theme.PrimaryColor

@Composable
fun ComunidadScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // 🔹 Header Morado (Comunidad) - Reutiliza función de NoticiasScreen
        item {
            SetupHeaderCard(title = "Comunidad")
        }

        // 🔹 EVENTOS: Card principal (Carreras Virtuales)
        item {
            Text(
                text = "EVENTOS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 10.dp)
            )
            // Card de Evento (Placeholder visual de Adidas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding2), // Placeholder visual
                    contentDescription = "Carreras Virtuales",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Overlay de texto (Verde neón y Blanco)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "¡CARRERAS\nVIRTUALES!",
                        color = PrimaryColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = "TODO LO QUE\nNECESITAS SABER",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "CARRERA VIRTUAL",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // 🔹 SUGERENCIAS: Facebook y Contactos - Reutiliza función de NoticiasScreen
        item {
            Text(
                text = "SUGERENCIAS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SuggestionCard(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_facebook,
                    title = "FACEBOOK",
                    subtitle = "Sigue a tus contactos"
                )
                SuggestionCard(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.ic_perfil,
                    title = "CONTACTOS",
                    subtitle = "Sigue a tus Desde contactos"
                )
            }
        }

        // 🔹 Secciones de texto de Sugerencias
        item {
            Spacer(modifier = Modifier.height(20.dp))
            CommunitySuggestionText("Supers dps")
            CommunitySuggestionText("Sugre aunelites")
            CommunitySuggestionText("Spps ftorgis")
            CommunitySuggestionText("Comniaronidades")
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CommunitySuggestionText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .height(50.dp)
            .background(Color.Transparent)
            .border(1.dp, PrimaryColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}