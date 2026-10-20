package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import com.tecsup.ayudacampus.model.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
class MisTicketsViewModel(repo: AyudaRepository, usuarioId: Int) : ViewModel() {
 val estado=MutableStateFlow("");val busqueda=MutableStateFlow("")
 private val todos=repo.observarTicketsCliente(usuarioId).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 val abiertos=todos.map { lista -> lista.count { it.ticket.estado == "ABIERTO" } }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
 val tickets=combine(todos,estado,busqueda) { lista,filtro,texto ->
  lista.filter { (filtro.isEmpty() || it.ticket.estado==filtro) && (it.ticket.titulo.contains(texto.trim(),ignoreCase=true) || it.ticket.id.toString().contains(texto.trim().removePrefix("#"))) }
 }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 val tarjetas=combine(tickets,observarReloj()) { lista,ahora ->
  lista.map { TarjetaTicket(it,calcularSla(it.ticket,ahora)) }
 }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(0),emptyList())
}
