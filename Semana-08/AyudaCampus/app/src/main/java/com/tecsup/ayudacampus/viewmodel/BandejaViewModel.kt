package com.tecsup.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.*
import kotlinx.coroutines.flow.*

class BandejaViewModel(repo: AyudaRepository) : ViewModel() {
 private val filtros = MutableStateFlow(FiltrosBandeja())
 private val categorias = repo.observarCategorias().onEach { lista ->
  filtros.update { actual ->
   if (actual.categoriaId != null && lista.none { it.id == actual.categoriaId })
    actual.copy(categoriaId=null) else actual
  }
 }
 val pantalla = combine(repo.observarTodosLosTickets(),categorias,filtros,observarReloj()) { todos,categorias,filtro,ahora ->
  val vigente = filtro.copy(categoriaId=filtro.categoriaId?.takeIf { id -> categorias.any { it.id == id } })
  EstadoBandeja(vigente,categorias,filtrarBandeja(todos,vigente).map {
   TarjetaTicket(it,calcularSla(it.ticket,ahora))
  },todos.size,false)
 }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(0),EstadoBandeja())

 fun cambiarEstado(estado: String) { filtros.update { it.copy(estado=estado) } }
 fun buscar(texto: String) { filtros.update { it.copy(texto=texto) } }
 fun cambiarPrioridad(prioridad: Prioridad?) { filtros.update { it.copy(prioridad=prioridad) } }
 fun cambiarCategoria(id: Int?) { filtros.update { it.copy(categoriaId=id) } }
 fun limpiarFiltros() { filtros.value = FiltrosBandeja() }
}
