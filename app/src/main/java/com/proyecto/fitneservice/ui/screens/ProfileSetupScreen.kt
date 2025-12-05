package com.proyecto.fitneservice.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border // <-- Importación para Modifier.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // <-- Importación para LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle // <-- Importación para TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.data.UserPreferences
import com.proyecto.fitneservice.ui.navigation.NavRoute
import com.proyecto.fitneservice.ui.theme.BackgroundDark
import com.proyecto.fitneservice.ui.theme.ButtonColor
import com.proyecto.fitneservice.ui.theme.PrimaryColor
import com.proyecto.fitneservice.ui.theme.SecondaryColor
import com.proyecto.fitneservice.ui.theme.TextPrimary
import kotlinx.coroutines.CoroutineScope // <-- Importación para CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Constantes para el flujo de setup
const val TOTAL_STEPS = 7

@Composable
fun ProfileSetupScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope() // Se utiliza para lanzar coroutines en Compose

    // 🚩 Estado de la navegación por pasos
    var currentStep by remember { mutableStateOf(1) }
    // 💾 Estados para guardar los datos del perfil
    var gender by remember { mutableStateOf("Hombre") }
    var age by remember { mutableStateOf(27) }
    var weight by remember { mutableStateOf(75) }
    var height by remember { mutableStateOf(165) }
    var goal by remember { mutableStateOf("Bajar De Peso") }
    var activityLevel by remember { mutableStateOf("Intermedio") }
    var fullName by remember { mutableStateOf(TextFieldValue("")) }
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var mobile by remember { mutableStateOf(TextFieldValue("")) }

    // Cargar email al iniciar
    LaunchedEffect(Unit) {
        scope.launch {
            val userData = userPrefs.getUserData.first()
            email = TextFieldValue(userData["email"] ?: "")
            gender = userData["gender"] ?: "Hombre"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header (Volver + Barra de Progreso)
            ProfileSetupHeader(
                currentStep = currentStep,
                totalSteps = TOTAL_STEPS,
                onBack = {
                    if (currentStep > 1) {
                        currentStep--
                    } else {
                        navController.popBackStack() // Si está en el primer paso, vuelve a la pantalla anterior
                    }
                }
            )

            // Contenido animado de cada paso
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.weight(1f),
                label = "ProfileSetupTransition"
            ) { targetStep ->
                when (targetStep) {
                    1 -> SetupWelcomeStep(onNext = { currentStep = 2 })
                    // Se pasa el scope en los pasos 3, 4 y 5 para las animaciones de scroll
                    2 -> GenderStep(gender) { gender = it; currentStep = 3 }
                    3 -> AgeStep(age, scope) { age = it }
                    4 -> WeightStep(weight, scope) { weight = it }
                    5 -> HeightStep(height, scope) { height = it }
                    6 -> GoalStep(goal) { goal = it; currentStep = 7 }
                    7 -> ActivityLevelStep(activityLevel) { activityLevel = it; currentStep = 8 } // Avanza al paso 8 (Completar Perfil)
                    8 -> CompleteProfileStep(
                        fullName = fullName,
                        onFullNameChange = { fullName = it },
                        nickname = nickname,
                        onNicknameChange = { nickname = it },
                        email = email,
                        onEmailChange = { email = it },
                        mobile = mobile,
                        onMobileChange = { mobile = it },
                        onComplete = {
                            scope.launch {
                                // 1. Guardar datos de perfil (nombre, email, género, etc.)
                                userPrefs.saveProfile(
                                    nombre = fullName.text,
                                    bio = "", // No hay campo bio aquí
                                    gender = gender
                                )
                                userPrefs.saveEmail(email.text)

                                // 2. Navegar a la pantalla de bienvenida al Home
                                navController.navigate(NavRoute.HomeWelcome.route) {
                                    popUpTo(NavRoute.ProfileSetup.route) { inclusive = true }
                                }
                            }
                        }
                    )
                    else -> SetupWelcomeStep(onNext = { currentStep = 2 })
                }
            }

            // Botón flotante 'Continuar' (solo si no es el paso 1 ni el último paso de perfil)
            // El botón de paso 8 ('comenzar') está dentro de CompleteProfileStep
            if (currentStep in 2..7) {
                Button(
                    onClick = {
                        if (currentStep < TOTAL_STEPS + 1) {
                            currentStep++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(230.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = if (currentStep == 7) "Completar Perfil" else "Continuar",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// =========================================================================================
// Componentes de la UI
// =========================================================================================

@Composable
fun ProfileSetupHeader(currentStep: Int, totalSteps: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentStep > 1) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = PrimaryColor,
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onBack)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        LinearProgressIndicator(
            progress = { currentStep.toFloat() / (totalSteps + 1) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = PrimaryColor,
            trackColor = SecondaryColor.copy(alpha = 0.5f)
        )
    }
}

// -----------------------------------------------------------------------------------------
// Paso 1: Bienvenida (4 - A - Set Up)
// -----------------------------------------------------------------------------------------
@Composable
fun SetupWelcomeStep(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de fondo (ajustada para el diseño)
        Image(
            painter = painterResource(id = R.drawable.onboarding1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Contenedor Morado (similar a las imágenes de Onboarding)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryColor)
                .padding(vertical = 30.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "La Constancia Es La Clave Del Progreso.\n¡No Te Rindas!",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor.copy(alpha = 0.9f),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_perfil),
                        contentDescription = "Ubicación",
                        tint = BackgroundDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ahora, configuremos tu experiencia en FitneService",
                        color = BackgroundDark,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        // Botón Siguiente
        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(230.dp)
                .height(50.dp)
        ) {
            Text("Siguiente", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// -----------------------------------------------------------------------------------------
// Paso 2: Género (4.1 - A - Género)
// -----------------------------------------------------------------------------------------
@Composable
fun GenderStep(selectedGender: String, onGenderSelect: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "¿Cuál Es Tu Género?",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Selecciona la opción que mejor te represente. Esto nos ayudará para que tengas una mejor experiencia.",
            color = TextPrimary.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Contenedor Morado para las opciones
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryColor, RoundedCornerShape(16.dp))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Usamos ic_perfil como placeholder visual (Hombre)
                GenderOption(
                    title = "Hombre",
                    icon = R.drawable.ic_perfil,
                    isSelected = selectedGender == "Hombre",
                    onClick = { onGenderSelect("Hombre") }
                )
                Spacer(modifier = Modifier.height(40.dp))
                // Usamos ic_perfil como placeholder visual (Mujer)
                GenderOption(
                    title = "Mujer",
                    icon = R.drawable.ic_perfil,
                    isSelected = selectedGender == "Mujer",
                    onClick = { onGenderSelect("Mujer") }
                )
            }
        }
    }
}

@Composable
fun GenderOption(title: String, icon: Int, isSelected: Boolean, onClick: () -> Unit) {
    val iconColor = if (isSelected) PrimaryColor else Color.White
    val containerColor = if (isSelected) PrimaryColor.copy(alpha = 0.15f) else Color.Transparent

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(containerColor, CircleShape)
                .border(
                    BorderStroke(2.dp, if (isSelected) PrimaryColor else Color.Transparent),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Usamos el símbolo de género en texto para mantener la distinción visual
            Text(
                text = if (title == "Hombre") "♂" else "♀",
                fontSize = 60.sp,
                color = iconColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = if (isSelected) BackgroundDark else BackgroundDark.copy(alpha = 0.8f),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

// -----------------------------------------------------------------------------------------
// Paso 3: Edad (4.2 - A - Edad) - Picker Horizontal
// -----------------------------------------------------------------------------------------
@Composable
fun AgeStep(age: Int, scope: CoroutineScope, onAgeChange: (Int) -> Unit) { // Firma corregida con CoroutineScope
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = age - 25)

    LaunchedEffect(Unit) {
        listState.scrollToItem(age - 25)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SetupTitleText(
            title = "¿Cuántos Años Tiene?",
            subtitle = "Selecciona tu edad para adaptar los entrenamientos y alimentaciones a tu etapa actual. Recuerda: cada edad tiene su propio ritmo, y estamos aquí para acompañarte en el tuyo."
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Display de Edad Actual
        Text(
            text = age.toString(),
            fontSize = 90.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )

        // Indicador de flecha verde
        Icon(
            painter = painterResource(id = R.drawable.ic_perfil), // Placeholder para ic_arrow_up
            contentDescription = "Indicador",
            tint = PrimaryColor,
            modifier = Modifier
                .size(30.dp)
                .offset(y = (-10).dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Carrusel Horizontal de Edades (Purple Band)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(SecondaryColor)
        ) {
            // Línea central verde
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(3.dp)
                    .fillMaxHeight(0.9f)
                    .background(PrimaryColor)
            )

            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items((18..60).toList()) { itemAge ->
                    val isSelected = itemAge == age
                    Text(
                        text = itemAge.toString(),
                        color = if (isSelected) BackgroundDark else Color.Gray,
                        fontSize = if (isSelected) 30.sp else 22.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                        modifier = Modifier.clickable {
                            onAgeChange(itemAge)
                            scope.launch {
                                listState.animateScrollToItem(itemAge - 25)
                            }
                        }
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Paso 4: Peso (4.3 - A - Peso) - Picker Horizontal con KG/LB
// -----------------------------------------------------------------------------------------
@Composable
fun WeightStep(weight: Int, scope: CoroutineScope, onWeightChange: (Int) -> Unit) { // Firma corregida con CoroutineScope
    var isKg by remember { mutableStateOf(true) }
    val maxWeight = if (isKg) 150 else 330
    val minWeight = if (isKg) 40 else 88
    val unitDisplay = if (isKg) "kg" else "lb"

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = weight - minWeight)

    LaunchedEffect(weight) {
        listState.scrollToItem(weight - minWeight)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SetupTitleText(
            title = "¿Cuál Es Tu Peso?",
            subtitle = "Ingresa tu peso para ayudarte a seguir tu progreso. No importa por dónde empieces, lo importante es avanzar paso a paso."
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Selector KG | LB
        Row(
            modifier = Modifier
                .width(200.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BackgroundDark)
                .border(1.dp, PrimaryColor, RoundedCornerShape(8.dp))
        ) {
            UnitToggleButton(
                text = "KG",
                isSelected = isKg,
                onClick = { isKg = true },
                isLeft = true
            )
            UnitToggleButton(
                text = "LB",
                isSelected = !isKg,
                onClick = { isKg = false },
                isLeft = false
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Picker de Peso (Purple Band)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(SecondaryColor)
        ) {
            // Línea central verde (picker line)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(3.dp)
                    .fillMaxHeight(0.9f)
                    .background(PrimaryColor)
            )

            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items((minWeight..maxWeight).toList()) { itemWeight ->
                    val isSelected = itemWeight == weight
                    Text(
                        text = itemWeight.toString(),
                        color = if (isSelected) PrimaryColor else Color.White.copy(alpha = 0.8f),
                        fontSize = if (isSelected) 30.sp else 22.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                        modifier = Modifier.clickable {
                            onWeightChange(itemWeight)
                            scope.launch {
                                listState.animateScrollToItem(itemWeight - minWeight)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display de Peso Actual con Unidad
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = weight.toString(),
                fontSize = 60.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = unitDisplay,
                fontSize = 24.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Indicador de flecha verde
        Icon(
            painter = painterResource(id = R.drawable.ic_perfil), // Placeholder para ic_arrow_up
            contentDescription = "Indicador",
            tint = PrimaryColor,
            modifier = Modifier
                .size(30.dp)
                .offset(y = (-10).dp)
        )
    }
}

@Composable
fun RowScope.UnitToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit, isLeft: Boolean) {
    val shape = if (isLeft) RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp) else RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
    val color = if (isSelected) PrimaryColor else BackgroundDark

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(color, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) BackgroundDark else TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

// -----------------------------------------------------------------------------------------
// Paso 5: Altura (4.4 - A - Altura) - Picker Vertical
// -----------------------------------------------------------------------------------------
@Composable
fun HeightStep(height: Int, scope: CoroutineScope, onHeightChange: (Int) -> Unit) { // Firma corregida con CoroutineScope
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 175 - height)

    LaunchedEffect(height) {
        listState.scrollToItem(175 - height)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SetupTitleText(
            title = "¿Cuál Es Tu Altura?",
            subtitle = "Indica tu altura para personalizar tus entrenamientos y cálculos físicos. Recuerda que cada cuerpo es único."
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Display de Altura Actual
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = height.toString(),
                fontSize = 90.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cm",
                fontSize = 30.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Picker Vertical (Purple Bar)
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SecondaryColor)
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(vertical = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items((155..190).toList().reversed()) { itemHeight ->
                    val isSelected = itemHeight == height
                    val heightDp = if (isSelected) 3.dp else 1.dp
                    val color = if (isSelected) PrimaryColor else Color.White.copy(alpha = 0.8f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clickable {
                                onHeightChange(itemHeight)
                                scope.launch {
                                    listState.animateScrollToItem(175 - itemHeight)
                                }
                            }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = itemHeight.toString(),
                            color = color,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        Box(
                            modifier = Modifier
                                .height(heightDp)
                                .width(if (isSelected) 24.dp else 16.dp)
                                .background(color)
                        )
                    }
                }
            }

            // Indicador de flecha verde (alineado a la derecha de la barra)
            Icon(
                painter = painterResource(id = R.drawable.ic_perfil), // Placeholder para ic_arrow_right
                contentDescription = "Indicador",
                tint = PrimaryColor,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
                    .offset(x = 10.dp)
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// Paso 6: Objetivo (4.5 - A - Objetivo) - Opciones de Radio
// -----------------------------------------------------------------------------------------
@Composable
fun GoalStep(selectedGoal: String, onGoalSelect: (String) -> Unit) {
    val goals = listOf("Bajar De Peso", "Mejorar Estilo De Vida", "Aumentar Masa Muscular", "Moldear El Cuerpo", "Otro")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SetupTitleText(
            title = "¿Cual Es Tu Objetivo?",
            subtitle = "Selecciona lo que más se ajuste a tus metas."
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Contenedor Morado para las opciones
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryColor, RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                goals.forEach { goalText ->
                    GoalOption(
                        text = goalText,
                        isSelected = selectedGoal == goalText,
                        onClick = { onGoalSelect(goalText) }
                    )
                    if (goalText != goals.last()) {
                        Divider(color = Color.White.copy(alpha = 0.4f), thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun GoalOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = BackgroundDark,
            fontSize = 18.sp
        )
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = BackgroundDark,
                unselectedColor = Color.White
            )
        )
    }
}

// -----------------------------------------------------------------------------------------
// Paso 7: Nivel de Actividad Física (4.6 - A - Nivel de Actividad Física) - Botones
// -----------------------------------------------------------------------------------------
@Composable
fun ActivityLevelStep(selectedLevel: String, onLevelSelect: (String) -> Unit) {
    val levels = listOf("Principiante", "Intermedio", "Avanzado")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SetupTitleText(
            title = "Nivel De Actividad Física",
            color = PrimaryColor,
            subtitle = "Selecciona la opción que mejor describa tu rutina actual",
            isSubtitled = false
        )
        // Descripción detallada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 30.dp)
        ) {
            LevelDescription(
                level = "Principiante:",
                description = "Recién empiezas o haces ejercicio ocasionalmente."
            )
            LevelDescription(
                level = "Intermedio:",
                description = "Realizas actividad física regular, 3-4 veces por semana."
            )
            LevelDescription(
                level = "Avanzado:",
                description = "Entrenas intensamente y con frecuencia, 5 o más veces por semana."
            )
        }

        levels.forEach { level ->
            LevelButton(
                text = level,
                isSelected = selectedLevel == level,
                onClick = { onLevelSelect(level) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LevelDescription(level: String, description: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp)) {
        Text(
            text = level,
            color = PrimaryColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = description,
            color = TextPrimary.copy(alpha = 0.7f),
            fontSize = 13.sp
        )
    }
}

@Composable
fun LevelButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) PrimaryColor else Color.White
    val textColor = if (isSelected) BackgroundDark else BackgroundDark

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(230.dp)
            .height(50.dp)
            .shadow(if (isSelected) 8.dp else 2.dp, RoundedCornerShape(20.dp))
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// -----------------------------------------------------------------------------------------
// Paso 8: Completar Perfil (4.7 - A - Completar Perfil)
// -----------------------------------------------------------------------------------------
@Composable
fun CompleteProfileStep(
    fullName: TextFieldValue,
    onFullNameChange: (TextFieldValue) -> Unit,
    nickname: TextFieldValue,
    onNicknameChange: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit,
    mobile: TextFieldValue,
    onMobileChange: (TextFieldValue) -> Unit,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Completa Tu Perfil",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Imagen de Perfil (Avatar)
        Image(
            painter = painterResource(id = R.drawable.ic_fotoperfil), // Reemplazar con el avatar real
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SecondaryColor)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Contenedor Morado para el Formulario
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryColor, RoundedCornerShape(16.dp))
                .padding(vertical = 24.dp, horizontal = 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileInputField(
                    value = fullName,
                    onValueChange = onFullNameChange,
                    label = "Nombre Completo"
                )
                ProfileInputField(
                    value = nickname,
                    onValueChange = onNicknameChange,
                    label = "Apodo"
                )
                ProfileInputField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    keyboardType = KeyboardType.Email
                )
                ProfileInputField(
                    value = mobile,
                    onValueChange = onMobileChange,
                    label = "Mobile Number",
                    keyboardType = KeyboardType.Phone
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botón "comenzar" (verde neón)
                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("comenzar", color = BackgroundDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// Componentes Reutilizables de Diseño
// -----------------------------------------------------------------------------------------

@Composable
fun SetupTitleText(title: String, subtitle: String, color: Color = TextPrimary, isSubtitled: Boolean = true) {
    Text(
        text = title,
        color = color,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    if (isSubtitled) {
        Text(
            text = subtitle,
            color = TextPrimary.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            // CORREGIDO: Se separan las llamadas de padding para evitar ambigüedad
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
fun ProfileInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, color = BackgroundDark, fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = PrimaryColor,
                unfocusedLabelColor = Color.Gray
            ),
            textStyle = TextStyle(color = BackgroundDark)
        )
    }
}