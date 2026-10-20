package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.*
@OptIn(ExperimentalCoroutinesApi::class)
class PanelViewModel(repo: AyudaRepository) : ViewModel() {
 private val inicio=SharingStarted.WhileSubscribed(5000)
 val promedioResolucion=repo.observarPromedioResolucion().map(::promedioLegible).stateIn(viewModelScope,inicio,"Sin tickets cerrados")
 val abiertos=repo.observarCantidadPorEstado(EstadoTicket.ABIERTO).stateIn(viewModelScope,inicio,0)
 val enProceso=repo.observarCantidadPorEstado(EstadoTicket.EN_PROCESO).stateIn(viewModelScope,inicio,0)
 val resueltos=repo.observarCantidadPorEstado(EstadoTicket.RESUELTO).stateIn(viewModelScope,inicio,0)
 private val cerrados=repo.observarCantidadPorEstado(EstadoTicket.CERRADO)
 val porEstado=combine(abiertos,enProceso,resueltos,cerrados) { a,p,r,c -> listOf(DatoGrafico("Abiertos",a),DatoGrafico("En proceso",p),DatoGrafico("Resueltos",r),DatoGrafico("Cerrados",c)) }.stateIn(viewModelScope,inicio,emptyList())
 val porCategoria=repo.observarCantidadPorCategoria().map { lista -> lista.map { DatoGrafico(it.nombre,it.cantidad) } }.stateIn(viewModelScope,inicio,emptyList())
 private val reloj=flow { while(currentCoroutineContext().isActive) { emit(System.currentTimeMillis()-3600000L);delay(30000) } }
 val altasSinAtender=reloj.flatMapLatest { repo.observarAltasSinAtender(it) }.stateIn(viewModelScope,inicio,0)
}
