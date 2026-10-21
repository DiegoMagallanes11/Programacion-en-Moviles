package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.model.*
class AtenderViewModel(private val repo: AyudaRepository, private val id: Int, private val usuario: Usuario) : ViewModel() {
 val ticket=repo.observarTicket(id).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
 val estadoElegido=MutableStateFlow<EstadoTicket?>(null);val prioridadElegida=MutableStateFlow<Prioridad?>(null)
 val sla=combine(ticket,observarReloj()) { detalle,ahora -> detalle?.let { calcularSla(it.ticket,ahora) } }
  .stateIn(viewModelScope,SharingStarted.WhileSubscribed(0),null)
 private val _error=MutableStateFlow<String?>(null);val error=_error.asStateFlow()
 private val _mensaje=MutableStateFlow<String?>(null);val mensaje=_mensaje.asStateFlow()
 private val _ocupado=MutableStateFlow(false);val ocupado=_ocupado.asStateFlow()
 private val _respuestaEnviada=MutableStateFlow<String?>(null);val respuestaEnviada=_respuestaEnviada.asStateFlow()
 fun confirmarEnvioRespuesta() { _respuestaEnviada.value=null }
 private fun accion(bloque: suspend (TicketConDetalle) -> Unit) {
  if(_ocupado.value) return
  _ocupado.value=true;_error.value=null;_mensaje.value=null
  viewModelScope.launch {
   try {
    require(usuario.rol=="ADMIN") { "Solo Soporte puede atender tickets" }
    val d=repo.observarTicket(id).first();require(d!=null) { "Ticket no encontrado" }
    require(d.ticket.estado!="CERRADO") { "El ticket está cerrado" };bloque(d)
   } catch(e: Exception) { if(e is CancellationException) throw e;_error.value=if(e is IllegalArgumentException) e.message else "No se pudo guardar el cambio" }
   finally { _ocupado.value=false }
  }
 }
 fun cambiarEstado(estado: EstadoTicket) { if(estado!=EstadoTicket.CERRADO && ticket.value?.ticket?.estado!="CERRADO") estadoElegido.value=estado }
 fun cambiarPrioridad(p: Prioridad) { if(ticket.value?.ticket?.estado!="CERRADO") prioridadElegida.value=p }
 fun responder(texto: String) { if(texto.isBlank()) return;accion { repo.agregarComentario(id,usuario,texto.trim());_respuestaEnviada.value=texto;_mensaje.value="Respuesta enviada" } }
 fun guardar() = accion { d ->
  val estado=estadoElegido.value ?: EstadoTicket.valueOf(d.ticket.estado)
  val prioridad=prioridadElegida.value ?: Prioridad.valueOf(d.ticket.prioridad)
  require(estado!=EstadoTicket.CERRADO) { "Soporte no puede cerrar tickets" }
  repo.cambiarEstado(id,estado);repo.cambiarPrioridad(id,prioridad)
  estadoElegido.value=null;prioridadElegida.value=null;_mensaje.value="Cambios guardados"
 }
}
