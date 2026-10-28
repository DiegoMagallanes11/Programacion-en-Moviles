package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.Rol
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: AyudaRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuarioActual = _usuario.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando = _cargando.asStateFlow()

    private val _ocupado = MutableStateFlow(false)
    val ocupado = _ocupado.asStateFlow()

    val modoOscuro = preferencias.modoOscuro.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    val notificaciones = preferencias.notificaciones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )

    val recibirTickets = preferencias.recibirTickets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )

    init {
        recuperarSesionGuardada()
    }

    private fun recuperarSesionGuardada() {
        viewModelScope.launch {
            try {
                val (rolGuardado, usuarioId) = preferencias.sesion.first()
                val usuario = if (usuarioId > 0) {
                    repository.obtenerUsuario(usuarioId)
                } else {
                    null
                }
                _usuario.value = usuario?.takeIf { it.rol == rolGuardado }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _error.value = "No se pudo recuperar la sesión"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (_ocupado.value) return

        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }

        _ocupado.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val usuario = repository.login(correo.trim(), clave)
                when {
                    usuario == null -> {
                        _error.value = "Correo o contraseña incorrectos"
                    }

                    usuario.rol != rolSeleccionado.name -> {
                        _error.value = "Selecciona el perfil correspondiente a tu cuenta"
                    }

                    else -> iniciarSesion(usuario)
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _error.value = "No se pudo iniciar sesión. Intenta nuevamente."
            } finally {
                _ocupado.value = false
            }
        }
    }

    private suspend fun iniciarSesion(usuario: Usuario) {
        preferencias.guardarSesion(usuario.rol, usuario.id)
        _usuario.value = usuario
    }

    fun modoOscuro(valor: Boolean) {
        guardarPreferencia { preferencias.guardarModoOscuro(valor) }
    }

    fun notificaciones(valor: Boolean) {
        guardarPreferencia { preferencias.guardarNotificaciones(valor) }
    }

    fun recibirTickets(valor: Boolean) {
        guardarPreferencia { preferencias.guardarRecibirTickets(valor) }
    }

    fun cerrarSesion() {
        guardarPreferencia {
            preferencias.cerrarSesion()
            _usuario.value = null
            _error.value = null
        }
    }

    fun cambiarPerfil() {
        if (_ocupado.value) return

        _ocupado.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val nuevoRol = if (_usuario.value?.rol == Rol.ADMIN.name) {
                    Rol.CLIENTE
                } else {
                    Rol.ADMIN
                }
                val ultimoUsuarioId = preferencias.obtenerUltimoUsuarioId(nuevoRol.name)
                val ultimoUsuario = if (ultimoUsuarioId > 0) {
                    repository.obtenerUsuario(ultimoUsuarioId)?.takeIf { it.rol == nuevoRol.name }
                } else {
                    null
                }
                val usuario = requireNotNull(
                    ultimoUsuario ?: repository.obtenerUsuarioPorRol(nuevoRol)
                ) {
                    "El perfil de práctica no está disponible"
                }
                iniciarSesion(usuario)
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _error.value = "No se pudo cambiar de perfil"
            } finally {
                _ocupado.value = false
            }
        }
    }

    private fun guardarPreferencia(accion: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                accion()
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                _error.value = "No se pudo guardar la preferencia"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
