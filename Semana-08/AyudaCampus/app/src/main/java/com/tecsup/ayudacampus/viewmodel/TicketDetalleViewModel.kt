package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import com.tecsup.ayudacampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
class TicketDetalleViewModel(private val repo: AyudaRepository, private val id: Int, private val usuario: Usuario) : ViewModel() {
 val ticket=repo.observarTicket(id).map { it?.takeIf { d -> d.ticket.clienteId==usuario.id && usuario.rol=="CLIENTE" } }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
 val sla=combine(ticket,observarReloj()) { detalle,ahora -> detalle?.let { calcularSla(it.ticket,ahora) } }
  .stateIn(viewModelScope,SharingStarted.WhileSubscribed(0),null)
 private val _error=MutableStateFlow<String?>(null);val error=_error.asStateFlow()
 private val _ocupado=MutableStateFlow(false);val ocupado=_ocupado.asStateFlow()
 private val _comentarioEnviado=MutableStateFlow<String?>(null);val comentarioEnviado=_comentarioEnviado.asStateFlow()
 fun confirmarEnvioComentario() { _comentarioEnviado.value=null }
 private fun accion(bloque: suspend (TicketConDetalle) -> Unit) {
  if(_ocupado.value) return
  _ocupado.value=true;_error.value=null
  viewModelScope.launch {
   try {
    val actual=repo.observarTicket(id).first()
    require(actual!=null && actual.ticket.clienteId==usuario.id && usuario.rol=="CLIENTE") { "No tienes acceso a este ticket" }
    bloque(actual)
   } catch(e: Exception) { if(e is CancellationException) throw e;_error.value=if(e is IllegalArgumentException) e.message else "No se pudo guardar el cambio" }
   finally { _ocupado.value=false }
  }
 }
 fun comentar(texto: String) {
  if(texto.isBlank()) return
  accion { d -> require(d.ticket.estado!="CERRADO") { "El ticket está cerrado" };repo.agregarComentario(id,usuario,texto.trim());_comentarioEnviado.value=texto }
 }
 fun cerrar(calificacion: Int, comentarioFinal: String = "") = accion { d ->
  require(d.ticket.estado=="RESUELTO") { "Solo puedes cerrar tickets resueltos" }
  require(calificacion in 1..5) { "Selecciona una calificación de 1 a 5" }
  if(comentarioFinal.isNotBlank()) repo.agregarComentario(id,usuario,comentarioFinal.trim())
  repo.cerrarConCalificacion(id,calificacion)
 }
 fun reabrir() = accion { d -> require(d.ticket.estado=="RESUELTO") { "Solo puedes reabrir tickets resueltos" };repo.reabrir(id) }
}
