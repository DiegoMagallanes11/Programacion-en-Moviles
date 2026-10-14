package com.tecsup.ayudacampus.data.local
import androidx.room.withTransaction
import kotlinx.coroutines.flow.first
import com.tecsup.ayudacampus.data.local.entity.*
object DatosIniciales {
 suspend fun sembrar(db: AyudaDatabase) = db.withTransaction {
  if(db.usuarioDao().contar() != 0) return@withTransaction
  if(db.categoriaDao().contar() == 0) db.categoriaDao().insertarTodas(listOf(
   Categoria(nombre="Hardware",icono="pc"), Categoria(nombre="Software",icono="code"),
   Categoria(nombre="Red / Wi-Fi",icono="wifi"), Categoria(nombre="Cuenta",icono="key"), Categoria(nombre="Impresión",icono="printer")
  ))
  db.usuarioDao().insertarTodos(listOf(
   Usuario(nombre="Juan Pérez",correo="juan@campus.test",clave="1234",rol="CLIENTE"),
   Usuario(nombre="Soporte TI",correo="soporte@campus.test",clave="admin",rol="ADMIN")
  ))
  val cliente = requireNotNull(db.usuarioDao().login("juan@campus.test","1234"))
  val soporte = requireNotNull(db.usuarioDao().login("soporte@campus.test","admin"))
  val categorias = db.categoriaDao().observarTodas().first()
  val ahora=System.currentTimeMillis()
  val casos=listOf(
   Triple("No conecta el Wi-Fi","Red / Wi-Fi","ABIERTO"),
   Triple("Equipo del aula no enciende","Hardware","EN_PROCESO"),
   Triple("Cuenta de correo bloqueada","Cuenta","RESUELTO"),
   Triple("Impresora sin conexión","Impresión","CERRADO"),
   Triple("Error al iniciar el editor","Software","ABIERTO"),
   Triple("Teclado con teclas dañadas","Hardware","RESUELTO")
  )
  casos.forEachIndexed { i,(titulo,categoria,estado) ->
   val id=db.ticketDao().insertarTicket(Ticket(clienteId=cliente.id,categoriaId=categorias.first { it.nombre == categoria }.id,
    titulo=titulo,descripcion="Incidencia de prueba: $titulo. Necesito ayuda para continuar con las clases.",ubicacion="Laboratorio ${i+1}",
    prioridad=listOf("ALTA","MEDIA","BAJA")[i%3],estado=estado,fecha=ahora-(i+2)*3600000L,
    fechaCierre=if(estado=="CERRADO") ahora-3600000 else null,calificacion=if(estado=="CERRADO") 4 else null)).toInt()
   if(i<2) db.ticketDao().insertarComentario(Comentario(ticketId=id,autorId=cliente.id,autorNombre=cliente.nombre,autorRol=cliente.rol,texto="El problema continúa desde esta mañana.",fecha=ahora-1800000))
   if(i==1 || i==2) db.ticketDao().insertarComentario(Comentario(ticketId=id,autorId=soporte.id,autorNombre=soporte.nombre,autorRol=soporte.rol,texto=if(i==2) "El acceso ya fue restablecido. Confirma si funciona." else "Estamos revisando el equipo.",fecha=ahora-900000))
  }
 }
}
