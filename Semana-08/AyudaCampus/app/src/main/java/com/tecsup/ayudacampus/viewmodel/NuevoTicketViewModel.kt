package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.model.Prioridad

data class FormularioTicket(val categoriaId: Int = 0, val titulo: String = "", val descripcion: String = "", val ubicacion: String = "", val prioridad: Prioridad = Prioridad.MEDIA)
class NuevoTicketViewModel(private val repo: AyudaRepository, private val prefs: PreferenciasStore, private val usuarioId: Int) : ViewModel() {
 val formulario=MutableStateFlow(FormularioTicket())
 private val _errores=MutableStateFlow<Map<String,String>>(emptyMap());val errores=_errores.asStateFlow()
 private val _enviado=MutableStateFlow(false);val enviado=_enviado.asStateFlow()
 private val _enviando=MutableStateFlow(false);val enviando=_enviando.asStateFlow()
 val categorias=repo.observarCategoriasActivas().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 val recibirTickets=prefs.recibirTickets.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),true)
 fun confirmarEnvio() {
  formulario.value=FormularioTicket()
  _errores.value=emptyMap()
  _enviado.value=false
 }
 fun enviar() {
  if(_enviando.value || _enviado.value) return
  val f=formulario.value
  val errores=buildMap {
   if(f.titulo.trim().length<5) put("titulo","Escribe al menos 5 caracteres")
   if(f.descripcion.trim().length<10) put("descripcion","Escribe al menos 10 caracteres")
   if(f.categoriaId==0) put("categoria","Selecciona una categoría activa")
  }
  _errores.value=errores;if(errores.isNotEmpty()) return
  _enviando.value=true
  viewModelScope.launch {
   try {
    when {
     !prefs.recibirTickets.first() -> _errores.value=mapOf("general" to "Mesa de ayuda cerrada")
     repo.observarCategoriasActivas().first().none { it.id==f.categoriaId } -> _errores.value=mapOf("categoria" to "La categoría ya no está activa")
     else -> { repo.crearTicket(usuarioId,f.categoriaId,f.titulo.trim(),f.descripcion.trim(),f.ubicacion.trim(),f.prioridad);_enviado.value=true }
    }
   } catch(e: Exception) { if(e is CancellationException) throw e;_errores.value=mapOf("general" to "No se pudo crear el ticket. Intenta nuevamente.") }
   finally { _enviando.value=false }
  }
 }
}
