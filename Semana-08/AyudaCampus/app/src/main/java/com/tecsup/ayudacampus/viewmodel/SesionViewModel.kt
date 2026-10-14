package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.model.Rol
class SesionViewModel(private val repo: AyudaRepository, private val prefs: PreferenciasStore) : ViewModel() {
 private val _usuario=MutableStateFlow<Usuario?>(null); val usuarioActual = _usuario.asStateFlow()
 private val _error=MutableStateFlow<String?>(null);val error=_error.asStateFlow()
 private val _cargando=MutableStateFlow(true);val cargando=_cargando.asStateFlow()
 private val _ocupado=MutableStateFlow(false);val ocupado=_ocupado.asStateFlow()
 val modoOscuro=prefs.modoOscuro.stateIn(viewModelScope,SharingStarted.Eagerly,false)
 init { viewModelScope.launch {
  try {
   val (rol,id)=prefs.sesion.first(); val usuario=if(id>0) repo.obtenerUsuario(id) else null
   _usuario.value=usuario?.takeIf { it.rol == rol }
  } catch(e: Exception) { if(e is CancellationException) throw e;_error.value="No se pudo recuperar la sesión" }
  finally { _cargando.value=false }
 } }
 fun login(correo: String, clave: String, rolSeleccionado: Rol) {
  if(_ocupado.value) return
  if(correo.isBlank() || clave.isBlank()) { _error.value="Completa el correo y la contraseña";return }
  _ocupado.value=true;_error.value=null
  viewModelScope.launch {
   try {
    val usuario=repo.login(correo.trim(),clave)
    when {
     usuario==null -> _error.value="Correo o contraseña incorrectos"
     usuario.rol!=rolSeleccionado.name -> _error.value="Selecciona el perfil correspondiente a tu cuenta"
     else -> { prefs.guardarSesion(usuario.rol,usuario.id);_usuario.value=usuario }
    }
   } catch(e: Exception) { if(e is CancellationException) throw e;_error.value="No se pudo iniciar sesión. Intenta nuevamente." }
   finally { _ocupado.value=false }
  }
 }
 fun limpiarError() { _error.value=null }
}
