package com.proyecto.fitneservice.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.ui.theme.BackgroundDark
import com.proyecto.fitneservice.ui.theme.ButtonColor
import com.proyecto.fitneservice.ui.theme.SecondaryColor

@Composable
fun NoticiasScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // 🔹 Header Morado (Noticias)
        item {
            SetupHeaderCard(title = "Noticias")
        }

        // 🔹 EVENTOS: Card principal (La ruta más larga)
        item {
            Text(
                text = "EVENTOS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 10.dp)
            )
            // Card de Evento (Placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding4), // Usando un placeholder visual
                    contentDescription = "La ruta más larga",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Overlay de texto
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LA RUTA MÁS LARGA",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 🔹 SUGERENCIAS: Facebook y Contactos
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
                    icon = R.drawable.ic_perfil, // Placeholder de icono de contactos
                    title = "CONTACTOS",
                    subtitle = "Sigue a tus Desde contactos"
                )
            }
        }

        // 🔹 Grupos y comunidades
        item {
            Text(
                text = "Grupos y comunidades",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 10.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.onboarding3), // Placeholder visual
                contentDescription = "Grupos y comunidades",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ===========================================================================
// FUNCIONES COMPARTIDAS
// ===========================================================================

@Composable
fun SetupHeaderCard(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(SecondaryColor, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun SuggestionCard(modifier: Modifier = Modifier, icon: Int, title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, color = BackgroundDark, fontSize = 14.sp)
            Text(text = subtitle, textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* TODO: Implement connection logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
            ) {
                Text("CONECTAR", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}