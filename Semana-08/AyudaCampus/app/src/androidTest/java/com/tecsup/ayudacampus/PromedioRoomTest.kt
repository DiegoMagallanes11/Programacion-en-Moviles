package com.tecsup.ayudacampus

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tecsup.ayudacampus.data.local.AyudaDatabase
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.data.repository.AyudaRepositoryRoom
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PromedioRoomTest {
 private lateinit var db: AyudaDatabase
 @Before fun preparar() = runBlocking {
  db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext, AyudaDatabase::class.java).build()
  db.usuarioDao().insertarTodos(listOf(Usuario(1,"Juan","juan@test","1234","CLIENTE")))
  db.categoriaDao().insertar(Categoria(1,"Red","wifi"))
  Unit
 }
 @After fun cerrar() { db.close() }
 private suspend fun insertar(estado: String, cierre: Long?, fecha: Long = 1_000L) = db.ticketDao().insertarTicket(
  Ticket(clienteId=1,categoriaId=1,titulo="Prueba",descripcion="Prueba",ubicacion="Aula",estado=estado,fecha=fecha,fechaCierre=cierre)
 )
 @Test fun promedioExcluyeInvalidosYConservaCero() = runBlocking {
  val repo = AyudaRepositoryRoom(db)
  assertNull(repo.observarPromedioResolucion().first())
  insertar("ABIERTO",9_000L); insertar("RESUELTO",9_000L)
  insertar("CERRADO",null); insertar("CERRADO",999L)
  assertNull(repo.observarPromedioResolucion().first())
  insertar("CERRADO",1_000L)
  assertEquals(0.0,repo.observarPromedioResolucion().first()!!,0.0)
  insertar("CERRADO",3_000L)
  assertEquals(1_000.0,repo.observarPromedioResolucion().first()!!,0.0)
 }
 @Test fun cierreActualizaObservadorExistente() = runBlocking {
  val repo = AyudaRepositoryRoom(db)
  val id = insertar("RESUELTO",null).toInt()
  val emisiones = mutableListOf<Double?>()
  val job = launch { repo.observarPromedioResolucion().collect { emisiones.add(it) } }
  withTimeout(5_000L) { while(emisiones.isEmpty()) delay(10) }
  db.ticketDao().cerrar(id,5,5_000L)
  withTimeout(5_000L) { while(emisiones.last() != 4_000.0) delay(10) }
  assertNull(emisiones.first()); assertEquals(4_000.0,emisiones.last()!!,0.0)
  job.cancelAndJoin()
 }
}
