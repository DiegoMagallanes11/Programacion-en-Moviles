package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
class BandejaViewModel(repo: AyudaRepository) : ViewModel() {
 val estado=MutableStateFlow("")
 val tickets=combine(repo.observarTodosLosTickets(),estado) { lista,filtro -> lista.filter { filtro.isEmpty() || it.ticket.estado==filtro } }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
}
