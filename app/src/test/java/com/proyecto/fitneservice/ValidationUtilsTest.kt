package com.proyecto.fitneservice

import com.proyecto.fitneservice.utils.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {


    @Test
    fun email_con_arroba_y_dominio_correcto_es_valido() {
        val email = "usuario@duoc.cl"
        val resultado = ValidationUtils.isEmailValid(email)
        assertTrue("El email debería ser válido", resultado)
    }


    @Test
    fun email_sin_arroba_o_dominio_incorrecto_es_invalido() {
        val emailMalo1 = "usuarioduoc.cl"
        val emailMalo2 = "usuario@duoc.xyz"

        assertFalse(ValidationUtils.isEmailValid(emailMalo1))
        assertFalse(ValidationUtils.isEmailValid(emailMalo2))
    }


    @Test
    fun password_alfanumerica_es_valida() {
        val pass = "Pass1234"
        val resultado = ValidationUtils.isPasswordValid(pass)
        assertTrue("La contraseña alfanumérica debería ser válida", resultado)
    }


    @Test
    fun password_solo_letras_o_solo_numeros_es_invalida() {
        val soloLetras = "password"
        val soloNumeros = "12345678"

        assertFalse("Solo letras no debe pasar", ValidationUtils.isPasswordValid(soloLetras))
        assertFalse("Solo números no debe pasar", ValidationUtils.isPasswordValid(soloNumeros))
    }


    @Test
    fun passwords_coinciden_correctamente() {
        val pass1 = "Segura123"
        val pass2 = "Segura123"

        assertTrue(ValidationUtils.doPasswordsMatch(pass1, pass2))
    }
}