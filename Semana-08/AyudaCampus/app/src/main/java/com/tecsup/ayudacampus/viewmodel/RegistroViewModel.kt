package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoRegistro(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmacion: String = "",
    val errores: Map<String, String> = emptyMap(),
    val registrando: Boolean = false,
    val correoCreado: String? = null
)

class RegistroViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoRegistro())
    val estado = _estado.asStateFlow()

    fun cambiarNombre(nombre: String) = actualizarFormulario {
        copy(nombre = nombre, errores = errores - "nombre")
    }

    fun cambiarCorreo(correo: String) = actualizarFormulario {
        copy(correo = correo, errores = errores - "correo")
    }

    fun cambiarClave(clave: String) = actualizarFormulario {
        copy(clave = clave, errores = errores - "clave")
    }

    fun cambiarConfirmacion(confirmacion: String) = actualizarFormulario {
        copy(confirmacion = confirmacion, errores = errores - "confirmacion")
    }

    private fun actualizarFormulario(cambio: EstadoRegistro.() -> EstadoRegistro) {
        if (!_estado.value.registrando) {
            _estado.update(cambio)
        }
    }

    fun registrar() {
        val formulario = _estado.value
        if (formulario.registrando) return

        val correoNormalizado = formulario.correo.trim().lowercase(Locale.ROOT)
        val errores = validar(formulario, correoNormalizado)
        if (errores.isNotEmpty()) {
            _estado.update { it.copy(errores = errores) }
            return
        }

        _estado.update { it.copy(registrando = true, errores = emptyMap()) }
        viewModelScope.launch {
            try {
                val creado = repository.registrarEstudiante(
                    nombre = formulario.nombre.trim(),
                    correo = correoNormalizado,
                    clave = formulario.clave
                )
                _estado.update {
                    if (creado) {
                        it.copy(registrando = false, correoCreado = correoNormalizado)
                    } else {
                        it.copy(
                            registrando = false,
                            errores = mapOf("correo" to "Este correo ya está registrado")
                        )
                    }
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _estado.update {
                    it.copy(
                        registrando = false,
                        errores = mapOf("general" to "No se pudo crear la cuenta. Intenta nuevamente.")
                    )
                }
            }
        }
    }

    private fun validar(
        formulario: EstadoRegistro,
        correoNormalizado: String
    ): Map<String, String> = buildMap {
        if (formulario.nombre.trim().length < 3) {
            put("nombre", "Escribe un nombre de al menos 3 caracteres")
        }
        if (!CORREO_VALIDO.matches(correoNormalizado)) {
            put("correo", "Escribe un correo válido")
        }
        if (formulario.clave.length < 4) {
            put("clave", "La contraseña debe tener al menos 4 caracteres")
        }
        if (formulario.confirmacion != formulario.clave) {
            put("confirmacion", "Las contraseñas no coinciden")
        }
    }

    fun confirmarNavegacion() {
        _estado.value = EstadoRegistro()
    }

    private companion object {
        val CORREO_VALIDO = Regex(
            pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
    }
}
