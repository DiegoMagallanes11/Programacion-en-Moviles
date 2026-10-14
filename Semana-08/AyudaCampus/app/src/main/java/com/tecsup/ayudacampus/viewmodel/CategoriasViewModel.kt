package com.tecsup.ayudacampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.data.local.entity.Categoria
class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {
 val categorias=repo.observarCategorias().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 val formulario=MutableStateFlow(Categoria(nombre="",icono="pc"))
 private val _error=MutableStateFlow<String?>(null);val error=_error.asStateFlow()
 private val _guardado=MutableStateFlow(false);val guardado=_guardado.asStateFlow()
 private val _ocupado=MutableStateFlow(false);val ocupado=_ocupado.asStateFlow()
 private fun accion(bloque: suspend () -> Unit) {
  if(_ocupado.value) return
  _ocupado.value=true;_error.value=null
  viewModelScope.launch {
   try { bloque() }
   catch(e: Exception) { if(e is CancellationException) throw e;_error.value=if(e is IllegalArgumentException) e.message else "No se pudo guardar. Comprueba que la categoría no tenga tickets." }
   finally { _ocupado.value=false }
  }
 }
 fun cargar(id: Int) { viewModelScope.launch {
  _guardado.value=false;_error.value=null
  formulario.value=if(id==0) Categoria(nombre="",icono="pc") else repo.observarCategorias().first().firstOrNull { it.id==id } ?: Categoria(nombre="",icono="pc")
 } }
 fun alternar(c: Categoria) = accion { repo.cambiarActiva(c.id,!c.activa) }
 fun guardar(categoria: Categoria) = accion {
  require(categoria.nombre.isNotBlank()) { "Escribe el nombre de la categoría" }
  require(categoria.icono in listOf("pc","code","wifi","key","printer","tag")) { "Selecciona un icono válido" }
  repo.guardarCategoria(categoria.copy(nombre=categoria.nombre.trim()));_guardado.value=true
 }
 fun eliminar(categoria: Categoria) = accion {
  require(repo.observarTodosLosTickets().first().none { it.ticket.categoriaId==categoria.id }) { "No puedes eliminar una categoría que tiene tickets" }
  repo.eliminarCategoria(categoria);_guardado.value=true
 }
}
