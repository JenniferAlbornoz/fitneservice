package com.proyecto.fitneservice.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.proyecto.fitneservice.R
import com.proyecto.fitneservice.data.UserPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PerfilScreen() {

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    // Estados de datos
    var nombre by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Hombre") }

    // Estados de imagen
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // Cargar datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        userPrefs.getUserData.collectLatest { userDataMap ->
            nombre = userDataMap["nombre"] ?: ""
            bio = userDataMap["bio"] ?: ""
            email = userDataMap["email"] ?: ""
            selectedGender = userDataMap["gender"] ?: "Hombre"

            // Cargar foto si existe
            val photoStr = userDataMap["photo"]
            if (!photoStr.isNullOrEmpty()) {
                imageUri = Uri.parse(photoStr)
            }
        }
    }

    // --------------------------------------------------------
    // 🔗 CONFIGURACIÓN DE LA CÁMARA Y GALERÍA
    // --------------------------------------------------------

    // 1. Launcher para obtener imagen de GALERÍA
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            // Auto-guardar la foto de galería
            scope.launch { userPrefs.savePhoto(it.toString()) }
        }
    }

    // 2. Launcher para tomar foto con CÁMARA
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            imageUri = tempPhotoUri
            // Auto-guardar la foto de cámara
            scope.launch { userPrefs.savePhoto(tempPhotoUri.toString()) }
        }
    }

    // 3. 🛡️ Launcher para pedir PERMISO DE CÁMARA (Evita el crash)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si el usuario dice SÍ, lanzamos la cámara
            val uri = createTempPictureUri(context)
            tempPhotoUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // --------------------------------------------------------
    // UI
    // --------------------------------------------------------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB3A0FF))
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("PERFIL", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(110.dp))

            // 📷 Foto de perfil
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (imageUri != null)
                        rememberAsyncImagePainter(imageUri)
                    else
                        painterResource(id = R.drawable.ic_fotoperfil),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { showImageSourceDialog = true },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column(Modifier.weight(1f)) {
                    Text("NOMBRE", color = Color.Gray, fontSize = 12.sp)
                    // Auto-guardado al escribir
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            scope.launch { userPrefs.saveName(it) }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 📝 Biografía (Auto-guardado)
            OutlinedTextField(
                value = bio,
                onValueChange = {
                    if (it.length <= 100) {
                        bio = it
                        scope.launch { userPrefs.saveBio(it) }
                    }
                },
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
            Spacer(modifier = Modifier.height(6.dp))
            Text("${100 - bio.length} Caracteres Restantes", color = Color.Gray, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // 🚻 Género (Auto-guardado)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Mujer", "Hombre", "Prefiero No Decirlo").forEach { gender ->
                    Button(
                        onClick = {
                            selectedGender = gender
                            scope.launch { userPrefs.saveGender(gender) }
                        },
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

            Spacer(modifier = Modifier.height(20.dp))

            // 📧 Email (Auto-guardado)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("EMAIL", color = Color.Gray, fontSize = 12.sp)
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        scope.launch { userPrefs.saveEmail(it) }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // Ya no es necesario el botón "Guardar Cambios" obligatorio,
            // pero podemos dejarlo como confirmación visual.
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* Acción visual opcional o Toast de "Todo al día" */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) {
                Text("Datos Actualizados", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // 🔘 DIÁLOGO DE SELECCIÓN
        if (showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = { showImageSourceDialog = false },
                title = { Text("Cambiar Foto de Perfil") },
                text = { Text("Elige una opción:") },
                confirmButton = {
                    TextButton(onClick = {
                        showImageSourceDialog = false

                        // 🔍 Verificar Permiso antes de abrir cámara
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        )
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            val uri = createTempPictureUri(context)
                            tempPhotoUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            // Solicitar permiso
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                        Text("📷 Cámara")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showImageSourceDialog = false
                        galleryLauncher.launch("image/*")
                    }) {
                        Text("🖼️ Galería")
                    }
                }
            )
        }
    }
}

// Función auxiliar para crear la URI temporal
fun createTempPictureUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir(null)

    val imageFile = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}