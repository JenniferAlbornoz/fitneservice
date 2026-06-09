# Proyecto FitneService | Evaluación Parcial 2

**Aplicación de Bienestar y Fitness Personal**

Este proyecto corresponde a la  evaluación parcial de la asignatura de Programación de Aplicaciones Móviles. Fue desarrollado implementando una arquitectura moderna nativa en Android utilizando **Kotlin** y **Jetpack Compose**.

---

## 🎓 Información del Equipo y Docente

| Rol | Detalles |
| :--- | :--- |
| **Institución** | Duoc UC, Sede Antonio Varas |
| **Carrera** | Ingeniería en Informática, 4° Semestre |
| **Docente a Cargo** | Diego Cares |
| **Integrantes** | Batman• Benjamín Monasterio |

---

## 🏗️ Arquitectura y Cumplimiento de Requisitos

El proyecto FitneService fue desarrollado bajo el enfoque **MVVM (Model–View–ViewModel)**, utilizando **Jetpack Compose** y **Material Design 3**.

### Componentes de la Arquitectura
* **Vista (View):** Pantallas desarrolladas en `ui/screens` utilizando `@Composable` y `remember` para manejar el estado de la interfaz de forma reactiva.
* **Modelo y Datos (Data):**
    * **Persistencia Local:** `UserPreferences.kt` implementa **DataStore Preferences** para guardar credenciales y perfil (Nombre, Biografía, Género, Correo).
    * **Red (Network):** `NewsApiService.kt` define la interfaz para el consumo de datos externos.
* **ViewModel:**
    * `UserViewModel.kt` y `NoticiasViewModel.kt`: Separan la lógica de negocio de la interfaz, gestionando estados de carga (`isLoading`), éxito y error.

---

## 🌐 Integración de API (Servicios Externos)

La aplicación implementa el consumo de una API REST para ofrecer contenido dinámico relacionado con la salud.

* **API Proveedor:** [NewsAPI](https://newsapi.org/)
* **Endpoint:** `v2/top-headlines`
* **Configuración:**
    * Categoría: **Health** (Salud y Bienestar).
    * Librería de Cliente HTTP: **Retrofit 2.9.0**.
    * Conversor JSON: **GSON**.
* **Manejo de Asincronía:** Uso de **Kotlin Coroutines** (`viewModelScope.launch`) para realizar peticiones en segundo plano sin bloquear el hilo principal (UI).

---

## 🚀 Funcionalidades Implementadas

La aplicación cuenta con un flujo eficiente y validado:

* **🎨 Diseño Visual (Material 3):** Paleta de colores personalizada (Verde, Negro, Blanco y Lila).
* **✅ Formularios Validados:** Comprobación en tiempo real de campos vacíos, formato de correo y contraseñas.
* **💾 Almacenamiento Local:** Persistencia de sesión y perfil mediante **DataStore**.
* **📰 Noticias en Tiempo Real:** Visualización de titulares de salud obtenidos desde internet.
* **🔄 Navegación Funcional:** Definida con `NavController`. Flujo: *Login → Registro → Perfil → Home → Contraseña Olvidada.*

### ✨ Animaciones y Feedback (Requisito Clave)
Se implementaron animaciones para mejorar la UX:
1.  **Pop-up Animado:** Feedback visual al completar un registro exitoso.
2.  **Confirmación de Cambios:** Alerta al guardar cambios en el perfil.
3.  **Transiciones:** Navegación fluida entre pantallas.

---

## 🧪 Guía de Prueba Rápida (Para el Docente)

Para verificar **Gestión de Estado**, **Persistencia**, **API** y **Animaciones**:

### 🔐 Credenciales de Acceso
* **Email:** `je.albornoz@duocuc.cl`
* **Contraseña:** `12345`

### 🔄 Flujo de Verificación Sugerido

1.  **Validar Login:** Ingrese con las credenciales para probar DataStore y validación.
2.  **Prueba de Registro (Animación):** Registre un usuario para ver el **popup de éxito**.
3.  **API de Noticias:** Al ingresar al Home, la aplicación realizará una petición HTTP (GET) para traer noticias de salud (verificar en Logcat o pantalla si está activa).
4.  **Editar Perfil:**
    * Navegue a la pestaña **Perfil**.
    * Edite el Nombre o Biografía y guarde.
    * Observe el popup de confirmación.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetbrains/compose)
* **Red:** Retrofit + GSON
* **Persistencia:** Jetpack DataStore
* **Imágenes:** Coil (Carga asíncrona de imágenes y GIFs)

## 📂 Estructura del Proyecto

```text
com.proyecto.fitneservice
├── data
│   ├── network              # Configuración API (NewsApiService)
│   ├── model                # Data Classes (NewsResponse, Article)
│   └── UserPreferences.kt   # Persistencia DataStore
├── ui
│   ├── screens              # Pantallas (Login, Home, Perfil...)
│   └── navigation           # NavGraph y Rutas
├── viewmodel
│   ├── NoticiasViewModel.kt # Lógica de conexión a API
│   └── UserViewModel.kt     # Lógica de usuario
└── MainActivity.kt          # Punto de entrada
