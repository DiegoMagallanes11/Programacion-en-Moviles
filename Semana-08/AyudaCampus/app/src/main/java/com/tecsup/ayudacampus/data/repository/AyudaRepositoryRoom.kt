package com.tecsup.ayudacampus.data.repository
import android.database.sqlite.SQLiteConstraintException
import com.tecsup.ayudacampus.data.local.AyudaDatabase
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.model.*
import java.util.Locale
class AyudaRepositoryRoom(private val db: AyudaDatabase) : AyudaRepository {
 override fun observarPromedioResolucion() = db.ticketDao().observarPromedioResolucion()
 override suspend fun login(correo: String, clave: String) = db.usuarioDao().login(correo.trim(), clave)
 override suspend fun registrarEstudiante(nombre: String, correo: String, clave: String): Boolean {
  val correoNormalizado = correo.trim().lowercase(Locale.ROOT)
  if (db.usuarioDao().existeCorreo(correoNormalizado)) return false
  return try {
   db.usuarioDao().insertar(Usuario(nombre=nombre.trim(),correo=correoNormalizado,clave=clave,rol=Rol.CLIENTE.name))
   true
  } catch (_: SQLiteConstraintException) {
   false
  }
 }
 override suspend fun obtenerUsuario(id: Int) = db.usuarioDao().obtener(id)
 override suspend fun obtenerUsuarioPorRol(rol: Rol) = db.usuarioDao().obtenerPorRol(rol.name)
 override suspend fun crearTicket(clienteId: Int, categoriaId: Int, titulo: String, descripcion: String, ubicacion: String, prioridad: Prioridad) = db.ticketDao().insertarTicket(Ticket(clienteId=clienteId,categoriaId=categoriaId,titulo=titulo,descripcion=descripcion,ubicacion=ubicacion,prioridad=prioridad.name))
 override fun observarTicketsCliente(clienteId: Int) = db.ticketDao().observarPorCliente(clienteId)
 override fun observarTodosLosTickets() = db.ticketDao().observarTodos()
 override fun observarTicket(id: Int) = db.ticketDao().observarUno(id)
 override suspend fun cambiarEstado(ticketId: Int, estado: EstadoTicket) = db.ticketDao().actualizarEstado(ticketId,estado.name)
 override suspend fun cambiarPrioridad(ticketId: Int, prioridad: Prioridad) = db.ticketDao().actualizarPrioridad(ticketId,prioridad.name)
 override suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int) = db.ticketDao().cerrar(ticketId,calificacion,System.currentTimeMillis())
 override suspend fun reabrir(ticketId: Int) = db.ticketDao().reabrir(ticketId)
 override suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String) = db.ticketDao().insertarComentario(Comentario(ticketId=ticketId,autorId=autor.id,autorNombre=autor.nombre,autorRol=autor.rol,texto=texto))
 override fun observarCantidadPorEstado(estado: EstadoTicket) = db.ticketDao().contarPorEstado(estado.name)
 override fun observarCantidadPorCategoria() = db.ticketDao().contarPorCategoria()
 override fun observarAltasSinAtender(limite: Long) = db.ticketDao().contarAltasSinAtender(limite)
 override fun observarCategorias() = db.categoriaDao().observarTodas()
 override fun observarCategoriasActivas() = db.categoriaDao().observarActivas()
 override suspend fun guardarCategoria(c: Categoria) { if (c.id == 0) db.categoriaDao().insertar(c) else db.categoriaDao().actualizar(c) }
 override suspend fun eliminarCategoria(c: Categoria) = db.categoriaDao().eliminar(c)
 override suspend fun cambiarActiva(id: Int, activa: Boolean) = db.categoriaDao().cambiarActiva(id, activa)
}
