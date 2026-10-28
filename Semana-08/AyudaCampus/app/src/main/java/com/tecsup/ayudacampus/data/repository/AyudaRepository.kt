package com.tecsup.ayudacampus.data.repository
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.model.*
import kotlinx.coroutines.flow.Flow
interface AyudaRepository {
 fun observarPromedioResolucion(): Flow<Double?>
 suspend fun login(correo: String, clave: String): Usuario?
 suspend fun registrarEstudiante(nombre: String, correo: String, clave: String): Boolean
 suspend fun obtenerUsuario(id: Int): Usuario?
 suspend fun obtenerUsuarioPorRol(rol: Rol): Usuario?
 suspend fun crearTicket(clienteId: Int, categoriaId: Int, titulo: String, descripcion: String, ubicacion: String, prioridad: Prioridad): Long
 fun observarTicketsCliente(clienteId: Int): Flow<List<TicketConDetalle>>
 fun observarTodosLosTickets(): Flow<List<TicketConDetalle>>
 fun observarTicket(id: Int): Flow<TicketConDetalle?>
 suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket)
 suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad)
 suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int)
 suspend fun reabrir(ticketId: Int)
 suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String)
 fun observarCantidadPorEstado(estado: EstadoTicket): Flow<Int>
 fun observarCantidadPorCategoria(): Flow<List<ConteoCategoria>>
 fun observarAltasSinAtender(limite: Long): Flow<Int>
 fun observarCategorias(): Flow<List<Categoria>>
 fun observarCategoriasActivas(): Flow<List<Categoria>>
 suspend fun guardarCategoria(c: Categoria)
 suspend fun eliminarCategoria(c: Categoria)
 suspend fun cambiarActiva(id: Int, activa: Boolean)
}
