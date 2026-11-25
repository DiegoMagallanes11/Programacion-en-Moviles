package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()

    val lista = combine(_lista, _busqueda) { estado, consulta ->
        when (estado) {
            is UiState.Exito -> UiState.Exito(
                estado.datos.filter { it.title.contains(consulta.trim(), ignoreCase = true) }
            )
            else -> estado
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Cargando)

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion = _publicacion.asStateFlow()

    private var idDetalle: Int? = null
    private var cargaLista: Job? = null
    private var cargaDetalle: Job? = null
    private var envio: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        cargaLista?.cancel()
        _lista.value = UiState.Cargando
        val usuario = _usuarioFiltro.value
        cargaLista = viewModelScope.launch {
            val resultado = if (usuario == null) repo.listar() else repo.porUsuario(usuario)
            _lista.value = resultado.comoUiState()
        }
    }

    fun buscar(consulta: String) {
        _busqueda.value = consulta
    }

    fun filtrar(userId: Int?) {
        if (_usuarioFiltro.value == userId) return
        _usuarioFiltro.value = userId
        cargar()
    }

    fun restaurarDetalle(id: Int) {
        if (idDetalle != id) abrir(id)
    }

    fun abrir(id: Int) {
        idDetalle = id
        cargaDetalle?.cancel()
        _detalle.value = UiState.Cargando
        cargaDetalle = viewModelScope.launch {
            val (post, comentarios) = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await() to comentarios.await()
            }
            _detalle.value = post.fold(
                onSuccess = { datos -> comentarios.map { DetalleUi(datos, it) }.comoUiState() },
                onFailure = { e -> UiState.Error(e.message ?: "Error desconocido") }
            )
        }
    }

    fun cerrarDetalle() {
        idDetalle = null
        cargaDetalle?.cancel()
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank() || _publicacion.value == UiState.Cargando) return
        _publicacion.value = UiState.Cargando
        envio = viewModelScope.launch {
            _publicacion.value = repo.crear(NuevoPost(1, titulo.trim(), texto.trim())).comoUiState()
        }
    }

    fun editarBorrador() {
        if (_publicacion.value != UiState.Cargando) _publicacion.value = null
    }

    fun cerrarCrear() {
        envio?.cancel()
        _publicacion.value = null
    }
}
