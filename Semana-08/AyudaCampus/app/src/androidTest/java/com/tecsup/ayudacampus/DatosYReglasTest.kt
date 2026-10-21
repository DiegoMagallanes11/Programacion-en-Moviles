package com.tecsup.ayudacampus
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import androidx.lifecycle.ViewModelStore
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.ayudacampus.data.local.*
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.data.repository.*
import com.tecsup.ayudacampus.model.*
import com.tecsup.ayudacampus.viewmodel.*
@RunWith(AndroidJUnit4::class)
class DatosYReglasTest {
 private lateinit var db: AyudaDatabase
 private lateinit var repo: AyudaRepository
 private val vms=ViewModelStore()
 @Before fun preparar() = runBlocking {
  db=Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext,AyudaDatabase::class.java).build()
  repo=AyudaRepositoryRoom(db);DatosIniciales.sembrar(db)
 }
 @After fun cerrar() { runBlocking { withContext(Dispatchers.Main) { vms.clear() } };db.close() }
 @Test fun personalizacionConservaDatosDeUnaInstalacionAnterior() = runBlocking {
  val anterior=Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext,AyudaDatabase::class.java).build()
  try {
   anterior.usuarioDao().insertarTodos(listOf(
    Usuario(17,"Juan Pérez","juan@campus.test","1234","CLIENTE"),
    Usuario(18,"Juan Pérez","otra@campus.test","1234","CLIENTE")
   ))
   anterior.categoriaDao().insertarTodas(listOf(Categoria(id=9,nombre="Histórica",icono="pc")))
   anterior.ticketDao().insertarTicket(Ticket(id=23,clienteId=17,categoriaId=9,titulo="Ticket conservado",descripcion="Descripción original",ubicacion="Aula",fecha=1000L))
   anterior.ticketDao().insertarComentario(Comentario(ticketId=23,autorId=17,autorNombre="Juan Pérez",autorRol="CLIENTE",texto="Mensaje original",fecha=2000L))
   anterior.ticketDao().insertarComentario(Comentario(ticketId=23,autorId=18,autorNombre="Juan Pérez",autorRol="CLIENTE",texto="Otro autor",fecha=3000L))
   DatosIniciales.sembrar(anterior)
   DatosIniciales.sembrar(anterior)
   assertEquals("Diego Magallanes",anterior.usuarioDao().obtener(17)!!.nombre)
   assertEquals(17,anterior.usuarioDao().login("MAGALLANES@GMAIL.COM","1234")!!.id)
   assertNull(anterior.usuarioDao().login("juan@campus.test","1234"))
   assertEquals("otra@campus.test",anterior.usuarioDao().obtener(18)!!.correo)
   assertEquals("Juan Pérez",anterior.usuarioDao().obtener(18)!!.nombre)
   assertEquals(2,anterior.usuarioDao().contar())
   val detalle=anterior.ticketDao().observarUno(23).first()!!
   assertEquals("Ticket conservado",detalle.ticket.titulo)
   assertEquals(1000L,detalle.ticket.fecha)
   assertEquals(listOf("Mensaje original","Otro autor"),detalle.comentarios.map { it.texto })
   // El nombre del comentario es una foto histórica, aunque cambie el perfil.
   assertEquals(listOf("Juan Pérez","Juan Pérez"),detalle.comentarios.map { it.autorNombre })
   assertEquals(1,anterior.ticketDao().observarTodos().first().size)
  } finally { anterior.close() }
 }
 @Test fun semillaIdempotenteOrdenYVisibilidad() = runBlocking {
  DatosIniciales.sembrar(db)
  assertEquals(2,db.usuarioDao().contar());assertEquals(5,repo.observarCategorias().first().size)
  assertEquals(6,repo.observarTodosLosTickets().first().size)
  assertNull(repo.login("magallanes@gmail.com","incorrecta"))
  val juan=repo.login("MAGALLANES@GMAIL.COM","1234")!!
  db.usuarioDao().insertarTodos(listOf(Usuario(nombre="Ana",correo="ana@campus.test",clave="test",rol="CLIENTE")))
  val ana=repo.login("ana@campus.test","test")!!
  val categoria=repo.observarCategoriasActivas().first().first()
  repo.crearTicket(ana.id,categoria.id,"Incidencia de Ana","Descripción de prueba suficiente","Aula 1",Prioridad.ALTA)
  assertEquals(6,repo.observarTicketsCliente(juan.id).first().size)
  assertEquals(1,repo.observarTicketsCliente(ana.id).first().size)
  val lista=repo.observarTodosLosTickets().first()
  assertEquals("Incidencia de Ana",lista.first().ticket.titulo)
  assertEquals(lista.map { listOf("ALTA","MEDIA","BAJA").indexOf(it.ticket.prioridad) }.sorted(),lista.map { listOf("ALTA","MEDIA","BAJA").indexOf(it.ticket.prioridad) })
  assertEquals(7,repo.observarCantidadPorCategoria().first().sumOf { it.cantidad })
  assertEquals(1,repo.observarAltasSinAtender(System.currentTimeMillis()-3600000).first())
 }
 @Test fun reglasDeRolesConversacionCierreYReapertura() = runBlocking {
  val juan=repo.login("magallanes@gmail.com","1234")!!;val soporte=repo.login("soporte@campus.test","admin")!!
  val id=repo.observarTicketsCliente(juan.id).first().first { it.ticket.estado=="ABIERTO" }.ticket.id
  val cliente=withContext(Dispatchers.Main) { TicketDetalleViewModel(repo,id,juan).also { vms.put("cliente",it) } }
  val admin=withContext(Dispatchers.Main) { AtenderViewModel(repo,id,soporte).also { vms.put("admin",it) } }
  withTimeout(5000) { cliente.ticket.first { it!=null } }
  withContext(Dispatchers.Main) { cliente.cerrar(4) }
  withTimeout(5000) { cliente.error.first { it!=null } };assertEquals("ABIERTO",repo.observarTicket(id).first()!!.ticket.estado)
  withContext(Dispatchers.Main) { admin.cambiarEstado(EstadoTicket.CERRADO) };assertNull(admin.estadoElegido.value)
  withContext(Dispatchers.Main) { admin.cambiarEstado(EstadoTicket.EN_PROCESO);admin.guardar() }
  withTimeout(5000) { repo.observarTicket(id).first { it?.ticket?.estado=="EN_PROCESO" } }
  withTimeout(5000) { admin.ocupado.first { !it } }
  withContext(Dispatchers.Main) { admin.responder("Estamos atendiendo la incidencia") }
  withTimeout(5000) { cliente.ticket.first { d -> d?.comentarios?.any { it.texto=="Estamos atendiendo la incidencia" }==true } }
  withTimeout(5000) { cliente.ocupado.first { !it } }
  repo.cambiarEstado(id,EstadoTicket.RESUELTO)
  withContext(Dispatchers.Main) { cliente.cerrar(0) }
  withTimeout(5000) { cliente.error.first { it?.contains("calificación")==true } }
  withContext(Dispatchers.Main) { cliente.reabrir() }
  val abierto=withTimeout(5000) { repo.observarTicket(id).first { it?.ticket?.estado=="ABIERTO" } }!!
  assertNull(abierto.ticket.calificacion);assertNull(abierto.ticket.fechaCierre)
  withTimeout(5000) { cliente.ocupado.first { !it } }
  repo.cambiarEstado(id,EstadoTicket.RESUELTO)
  withContext(Dispatchers.Main) { cliente.cerrar(4) }
  val cerrado=withTimeout(5000) { repo.observarTicket(id).first { it?.ticket?.estado=="CERRADO" } }!!
  assertEquals(4,cerrado.ticket.calificacion);assertNotNull(cerrado.ticket.fechaCierre)
  withTimeout(5000) { cliente.ocupado.first { !it } }
  val cantidad=cerrado.comentarios.size
  withContext(Dispatchers.Main) { cliente.comentar("No debe guardarse") }
  withTimeout(5000) { cliente.error.first { it?.contains("cerrado")==true } }
  assertEquals(cantidad,repo.observarTicket(id).first()!!.comentarios.size)
 }
 @Test fun categoriasInactivasYProteccionAlEliminar() = runBlocking {
  val categoria=repo.observarCategorias().first().first { it.nombre=="Hardware" }
  repo.cambiarActiva(categoria.id,false)
  assertFalse(repo.observarCategoriasActivas().first().any { it.id==categoria.id })
  val vm=withContext(Dispatchers.Main) { CategoriasViewModel(repo).also { vms.put("categorias",it) } }
  withContext(Dispatchers.Main) { vm.eliminar(categoria) }
  withTimeout(5000) { vm.error.first { it?.contains("tiene tickets")==true } }
  assertTrue(repo.observarCategorias().first().any { it.id==categoria.id })
  repo.guardarCategoria(Categoria(nombre="Sin tickets",icono="tag"))
  assertEquals(0,repo.observarCantidadPorCategoria().first().first { it.nombre=="Sin tickets" }.cantidad)
 }
}
