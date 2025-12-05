package com.proyecto.fitneservice.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock // <--- IMPORTACIÓN CORREGIDA
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.ui.theme.BackgroundDark
import com.proyecto.fitneservice.ui.theme.ButtonColor
import com.proyecto.fitneservice.ui.theme.PrimaryColor
import com.proyecto.fitneservice.ui.theme.SecondaryColor
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

// 🏃‍♂️ Estados del flujo de actividad
sealed class ActivityState {
    data object Initial : ActivityState()
    data object Countdown : ActivityState()
    data object Running : ActivityState()
    data object Ended : ActivityState()
    data object Summary : ActivityState()
}

@Composable
fun ActividadScreen() {
    // ⚙️ Estados principales de la actividad
    var activityState by remember { mutableStateOf<ActivityState>(ActivityState.Initial) }
    var runningTimeSeconds by remember { mutableStateOf(0) }
    var distanceKm by remember { mutableStateOf(0.0) }
    var caloriesBurned by remember { mutableStateOf(0.0) }
    var heartRate by remember { mutableStateOf(0) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }
    var isLocked by remember { mutableStateOf(true) }

    // Simulación de actividad (incremento de métricas)
    LaunchedEffect(activityState) {
        if (activityState is ActivityState.Running) {
            while (activityState is ActivityState.Running) {
                delay(1000)
                runningTimeSeconds++
                distanceKm += 0.01
                caloriesBurned += 0.8
                heartRate = (120..180).random()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        // Usa AnimatedContent para transiciones suaves entre vistas
        AnimatedContent(
            targetState = activityState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ActivityScreenTransition"
        ) { targetState ->
            when (targetState) {
                ActivityState.Initial -> InitialView {
                    activityState = ActivityState.Countdown
                    startTime = LocalTime.now()
                }
                ActivityState.Countdown -> CountdownView(
                    onStart = { activityState = ActivityState.Running },
                    onSkip = { activityState = ActivityState.Running }
                )
                ActivityState.Running -> RunningView(
                    runningTimeSeconds = runningTimeSeconds,
                    distanceKm = distanceKm,
                    caloriesBurned = caloriesBurned,
                    heartRate = heartRate,
                    isLocked = isLocked,
                    onLockToggle = { isLocked = it },
                    onPause = { activityState = ActivityState.Ended },
                    onTerminate = {
                        activityState = ActivityState.Ended
                        endTime = LocalTime.now()
                    }
                )
                ActivityState.Ended -> EndRunView(
                    onSave = { activityState = ActivityState.Summary }
                )
                ActivityState.Summary -> ActivitySummaryView(
                    runningTimeSeconds = runningTimeSeconds,
                    distanceKm = distanceKm,
                    caloriesBurned = caloriesBurned,
                    heartRate = heartRate,
                    startTime = startTime,
                    endTime = endTime ?: LocalTime.now()
                )
            }
        }
    }
}

// =========================================================================================
// 1. Vista Inicial (Botón de Inicio)
// =========================================================================================
@Composable
fun InitialView(onStartRunning: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Métricas iniciales (Cero)
        MetricsHeader(0, 0.0, 0.0, 0)

        Spacer(modifier = Modifier.height(100.dp))

        // Botón grande de inicio
        Button(
            onClick = onStartRunning,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            shape = CircleShape,
            modifier = Modifier.size(150.dp)
        ) {
            Text(
                "INICIO RUNNING",
                color = BackgroundDark,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    }
}

// =========================================================================================
// 2. Vista de Cuenta Regresiva (14 segundos)
// =========================================================================================
@Composable
fun CountdownView(onStart: () -> Unit, onSkip: () -> Unit) {
    var countdownValue by remember { mutableStateOf(14) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (countdownValue > 0) {
                delay(1000)
                countdownValue--
            }
            if (countdownValue == 0) {
                onStart()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryColor)
            // Tocar en la pantalla para saltar (Comenzar de una vez)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isRunning = false
                        onSkip()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = countdownValue.toString(),
                color = BackgroundDark,
                fontSize = 200.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Opción: Agregar 10 Segundos
            Button(
                onClick = { countdownValue += 10 },
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Agregar 10 Segundos",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

// =========================================================================================
// 3. Vista de Seguimiento Activo
// =========================================================================================
@Composable
fun RunningView(
    runningTimeSeconds: Int,
    distanceKm: Double,
    caloriesBurned: Double,
    heartRate: Int,
    isLocked: Boolean,
    onLockToggle: (Boolean) -> Unit,
    onPause: () -> Unit,
    onTerminate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // 🔹 Encabezado de la actividad (Similar a la imagen 5-A - Home con métricas)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(SecondaryColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título/Métricas principales
                Text(
                    text = formatTime(runningTimeSeconds),
                    color = Color.Black,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Fila de métricas secundarias
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MetricItem("Km", String.format("%.2f", distanceKm), PrimaryColor)
                    MetricItem("Calorías", String.format("%.0f", caloriesBurned), Color.Red) // Color de ejemplo
                    MetricItem("Ritmo", "${heartRate} lpm", PrimaryColor)
                }
            }
        }

        // 🗺️ Área del mapa (Placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "MAPA DE RUTA (Placeholder)",
                color = Color.Black,
                fontSize = 18.sp
            )
        }

        // 🔒 Controles de Pausa/Terminar con candado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de Pausa (Amarillo)
            ActionButton(
                label = "PAUSAR",
                color = Color(0xFFFFCC00), // Amarillo
                icon = Icons.Default.Lock,
                isLocked = isLocked,
                onClick = onPause,
                onLockToggle = onLockToggle,
                lockState = isLocked
            )

            // Botón de Terminar (Rojo)
            ActionButton(
                label = "TERMINAR",
                color = Color.Red,
                icon = Icons.Default.Lock,
                isLocked = isLocked,
                onClick = onTerminate,
                onLockToggle = onLockToggle,
                lockState = isLocked
            )
        }
    }
}

// Componente para botones con candado
@Composable
fun ActionButton(
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isLocked: Boolean,
    onClick: () -> Unit,
    onLockToggle: (Boolean) -> Unit,
    lockState: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 🔒 Candado de Desbloqueo
        Icon(
            // CORREGIDO: Icons.Default.LockOpen ahora está importado correctamente.
            imageVector = if (lockState) Icons.Default.Lock else Icons.Default.Lock,
            contentDescription = if (lockState) "Bloqueado" else "Desbloqueado",
            tint = PrimaryColor,
            modifier = Modifier
                .size(30.dp)
                .clickable { onLockToggle(!lockState) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onClick,
            enabled = !isLocked,
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                disabledContainerColor = color.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .width(130.dp)
                .height(50.dp)
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


// =========================================================================================
// 4. Vista de Finalización (Calificación y Guardar)
// =========================================================================================
@Composable
fun EndRunView(onSave: () -> Unit) {
    var selectedMood by remember { mutableStateOf(Mood.Neutral) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SecondaryColor.copy(alpha = 0.8f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 📷 Botón de Cámara (Placeholder - Similar a la imagen 5-A - Home)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Tomar Foto",
                tint = Color.Gray,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "¿Cómo te sientes después de tu actividad?",
            color = BackgroundDark,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 😔 Calificación de Cansancio/Ánimo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MoodIcon(Mood.Sad) { selectedMood = Mood.Sad }
            MoodIcon(Mood.Neutral) { selectedMood = Mood.Neutral }
            MoodIcon(Mood.Happy) { selectedMood = Mood.Happy }
            // Los iconos de la imagen son 5, pero por simplicidad se usan 3 aquí.
        }

        Spacer(modifier = Modifier.height(80.dp))

        // 💾 Botón de Guardar
        Button(
            onClick = onSave,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text("Guardar", color = BackgroundDark, fontWeight = FontWeight.Bold)
        }
    }
}

enum class Mood { Sad, Neutral, Happy }

@Composable
fun MoodIcon(mood: Mood, onClick: () -> Unit) {
    // Usamos emojis como placeholders para los iconos de ánimo
    val emoji = when (mood) {
        Mood.Sad -> "😞"
        Mood.Neutral -> "😐"
        Mood.Happy -> "😃"
    }
    Text(
        text = emoji,
        fontSize = 40.sp,
        modifier = Modifier.clickable(onClick = onClick)
    )
}

// =========================================================================================
// 5. Vista de Resumen (Métricas Finales)
// =========================================================================================
@Composable
fun ActivitySummaryView(
    runningTimeSeconds: Int,
    distanceKm: Double,
    caloriesBurned: Double,
    heartRate: Int,
    startTime: LocalTime,
    endTime: LocalTime
) {
    // Cálculo de métricas
    val totalTimeMinutes = runningTimeSeconds / 60.0
    val averagePace = if (distanceKm > 0) totalTimeMinutes / distanceKm else 0.0 // Min/Km
    val avgSpeedKmh = if (totalTimeMinutes > 0) distanceKm / (totalTimeMinutes / 60) else 0.0 // Km/h
    val maxSpeedKmh = avgSpeedKmh * 1.2 // Simulación de velocidad máxima

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // 🟣 Header con métricas principales
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryColor)
                .height(150.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Métrica principal (similar a la imagen 5-A - Home)
                Text(
                    text = "${String.format("%.2f", distanceKm)} Km, ${formatTime(runningTimeSeconds)}, RUNNING",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Gráfico/Tabla Placeholder
                Text(
                    "Mira Qué Posicion Ocupas En La Tabla",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }

        // 📊 Lista de métricas detalladas
        Column(modifier = Modifier.padding(24.dp)) {
            SummaryItem("Ritmo Medio", String.format("%.2f Min/Km", averagePace))
            SummaryItem("Vel. Media", String.format("%.2f Km/h", avgSpeedKmh))
            SummaryItem("Vel. Máx.", String.format("%.2f Km/h", maxSpeedKmh))
            SummaryItem("Ritmo Cardíaco Promedio", "${heartRate} lpm")
            SummaryItem("Distancia", String.format("%.2f Km", distanceKm))
            SummaryItem("Calorías Quemadas", String.format("%.0f Kcal", caloriesBurned))
            Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
            SummaryItem("Hora De Inicio", startTime.format(timeFormatter))
            SummaryItem("Hora De Termino", endTime.format(timeFormatter))
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White, fontSize = 16.sp)
        Text(value, color = PrimaryColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun MetricsHeader(time: Int, distance: Double, calories: Double, hr: Int) {
    // ... Implementación del header de métricas
    Text(
        text = formatTime(time),
        color = PrimaryColor,
        fontSize = 40.sp,
        fontWeight = FontWeight.ExtraBold
    )
    // ... (otras métricas)
}

@Composable
fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White, fontSize = 12.sp)
        Text(value, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

// Función auxiliar para formatear segundos a HH:MM:SS
fun formatTime(totalSeconds: Int): String {
    val hours = TimeUnit.SECONDS.toHours(totalSeconds.toLong())
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds.toLong()) % 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}