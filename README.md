# Proyecto FitneService | Evaluación Parcial 2

**Aplicación de Bienestar y Fitness Personal**

Este proyecto corresponde a la evaluación parcial de la asignatura de Programación de Aplicaciones Móviles. Fue desarrollado implementando una arquitectura moderna nativa en Android utilizando **Kotlin** y **Jetpack Compose**.

---

## 🎓 Información del Equipo y Docente

| Rol | Detalles |
| :--- | :--- |
| **Institución** | Duoc UC, Sede Antonio Varas |
| **Carrera** | Ingeniería en Informática, 4° Semestre |
| **Docente a Cargo** | Diego Cares |
| **Integrantes** | • Jennifer Albornoz<br>• Monsserratt Ampuero<br>• Benjamín Monasterio |

---

## 🏗️ Arquitectura y Cumplimiento de Requisitos

El proyecto FitneService fue desarrollado bajo el enfoque **MVVM (Model–View–ViewModel)**, utilizando **Jetpack Compose** y **Material Design 3**.

### Componentes de la Arquitectura
* **Vista (View):** Pantallas desarrolladas en `ui/screens` utilizando `@Composable` y `remember` para manejar el estado de la interfaz de forma reactiva.
* **Modelo y Repositorio (Data):**
    * `UserPreferences.kt` (ubicado en `data/`): Implementa **DataStore Preferences**, gestionando el almacenamiento persistente de credenciales, perfil y configuración (Nombre, Biografía, Género, Correo).
* **ViewModel:**
    * `UserViewModel.kt`: Se encarga de delegar las operaciones de DataStore y reforzar la arquitectura MVVM. Aunque la gestión de estado de Compose es potente, esta clase asegura la separación de lógica y UI.

---

## 🚀 Funcionalidades Implementadas

La aplicación cuenta con un flujo simplificado y eficiente, destacando las siguientes características:

* **🎨 Diseño Visual (Material 3):** Paleta de colores personalizada (Verde, Negro, Blanco y Lila).
* **✅ Formularios Validados:** Comprobación en tiempo real de campos vacíos, formato de correo válido y coincidencia de contraseñas.
* **💾 Almacenamiento Local:** Persistencia de datos mediante **DataStore**.
* **🖼️ Recursos Nativos:** Selección de imagen de perfil directamente desde la galería del dispositivo.
* **🔄 Navegación Funcional:** Definida con `NavController` y `NavRoutes`.
    * Flujo: *Login → Registro → Perfil → Home (Actividad/Perfil) → Contraseña Olvidada → Restablecer.*

### ✨ Animaciones y Feedback (Requisito Clave)
Se implementaron animaciones y alertas visuales para mejorar la UX:
1.  **Pop-up Animado:** Se despliega al completar un registro exitoso.
2.  **Confirmación de Cambios:** Alerta visual al guardar cambios en el perfil.
3.  **Alerta Animada:** Feedback visual en el flujo de restablecimiento de contraseña.

---

## 🧪 Guía de Prueba Rápida (Para el Docente)

Para verificar el funcionamiento de los requisitos de **Gestión de Estado**, **Persistencia** y **Animaciones**, utilice los siguientes datos:

### 🔐 Credenciales de Acceso
* **Email:** `je.albornoz@duocuc.cl`
* **Contraseña:** `12345`

### 🔄 Flujo de Verificación Sugerido

1.  **Validar Login:** Ingrese con las credenciales registradas para probar la persistencia y autenticación.
2.  **Prueba de Registro (Animación):** Intente registrar un nuevo usuario para visualizar el **popup de éxito animado**.
3.  **Restablecer Contraseña:**
    * Vaya a "Olvidé mi contraseña".
    * Ingrese una nueva clave y confirme.
    * Verifique el popup: *"Contraseña actualizada correctamente"*.
4.  **Editar Perfil (Persistencia):**
    * Navegue a la pestaña **Perfil** en el menú inferior.
    * Edite el Nombre, Biografía, Correo o Género.
    * Observe el popup de confirmación.
5.  **Navegación:** Utilice las flechas verdes (`ArrowBack`) para verificar el correcto retorno en la pila de navegación.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetbrains/compose)
* **Persistencia:** Jetpack DataStore
* **Navegación:** Navigation Compose
* **IDE:** Android Studio Ladybug o superior.

## 📂 Estructura del Proyecto

El código está organizado modularmente para facilitar la evaluación:

```text
com.proyecto.fitneservice
├── data
│   ├── UserPreferences.kt   # Gestión de DataStore (Persistencia)
│   └── ...
├── ui
│   ├── components           # Componentes UI reutilizables
│   ├── navigation           # Configuración de NavRoutes y NavGraph
│   ├── screens              # Pantallas (Login, Actividad, Perfil, etc.)
│   └── theme                # Tema Material 3 (Colores, Tipografía)
├── viewmodel
│   └── UserViewModel.kt     # Lógica de negocio
└── MainActivity.kt          # Punto de entrada

