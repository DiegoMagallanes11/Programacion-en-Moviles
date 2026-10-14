package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
data class ResumenPerfil(val abiertos: Int = 0, val resueltos: Int = 0, val total: Int = 0)
class PerfilViewModel(repo: AyudaRepository, usuarioId: Int) : ViewModel() {
 val resumen=repo.observarTicketsCliente(usuarioId).map { lista -> ResumenPerfil(lista.count { it.ticket.estado=="ABIERTO" },lista.count { it.ticket.estado=="RESUELTO" },lista.size) }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),ResumenPerfil())
}
