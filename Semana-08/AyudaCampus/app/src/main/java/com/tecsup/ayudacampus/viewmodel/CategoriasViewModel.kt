package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.local.entity.Categoria
class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {
 val categorias = repo.observarCategorias().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
 fun alternar(c: Categoria) { viewModelScope.launch { repo.cambiarActiva(c.id, !c.activa) } }
}
