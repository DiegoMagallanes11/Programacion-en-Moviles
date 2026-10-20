package com.tecsup.ayudacampus

import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.model.*
import com.tecsup.ayudacampus.viewmodel.observarReloj
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SlaTest {
 private val ticket=Ticket(clienteId=1,categoriaId=1,titulo="Prueba",descripcion="",ubicacion="",fecha=1_000L)
 @Test fun prioridadesYLimitesExactos() {
  for ((prioridad,horas) in listOf("ALTA" to 4L,"MEDIA" to 24L,"BAJA" to 72L)) {
   val t=ticket.copy(prioridad=prioridad)
   val limite=t.fecha+horas*3_600_000L
   assertEquals(SituacionSla.ACTIVO,calcularSla(t,limite-1L).situacion)
   assertEquals(SituacionSla.VENCIDO,calcularSla(t,limite).situacion)
   assertEquals("SLA vencido hace 1 min",calcularSla(t,limite+60_000L).texto)
   assertEquals("SLA: $horas h restantes",calcularSla(t,t.fecha).texto)
  }
 }
 @Test fun prioridadActualEstadosYReaperturaNoReinicianFecha() {
  val ahora=ticket.fecha+5L*3_600_000L
  val alta=ticket.copy(prioridad="ALTA",estado="EN_PROCESO")
  assertEquals(SituacionSla.VENCIDO,calcularSla(alta,ahora).situacion)
  assertEquals(SituacionSla.ACTIVO,calcularSla(alta.copy(prioridad="MEDIA"),ahora).situacion)
  assertEquals("SLA: ticket resuelto",calcularSla(alta.copy(estado="RESUELTO"),ahora).texto)
  assertEquals("SLA: ticket cerrado",calcularSla(alta.copy(estado="CERRADO"),ahora).texto)
  assertEquals("SLA vencido hace 1 h",calcularSla(alta.copy(estado="ABIERTO"),ahora).texto)
 }
 @Test fun relojActualizaCada30SegundosYSeDetieneSinSuscriptores() = runTest {
  var lecturas=0
  val reloj=observarReloj { lecturas++;testScheduler.currentTime }
   .stateIn(backgroundScope,SharingStarted.WhileSubscribed(0),-1L)
  val valores=mutableListOf<Long>()
  val job=backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { reloj.collect { valores.add(it) } }
  runCurrent();assertEquals(0L,reloj.value)
  advanceTimeBy(29_999L);runCurrent();assertEquals(0L,reloj.value)
  advanceTimeBy(1L);runCurrent();assertEquals(30_000L,reloj.value)
  job.cancel();runCurrent();val antes=lecturas
  advanceTimeBy(90_000L);runCurrent();assertEquals(antes,lecturas)
  val nuevo=backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { reloj.collect() }
  runCurrent();assertEquals(testScheduler.currentTime,reloj.value);nuevo.cancel()
 }
 @Test fun textoPasaAVencidoSinReabrirPantalla() = runTest {
  val t=ticket.copy(prioridad="ALTA")
  val limite=t.fecha+4L*3_600_000L
  val estado=observarReloj { limite-15_000L+testScheduler.currentTime }
   .map { calcularSla(t,it) }.stateIn(backgroundScope,SharingStarted.WhileSubscribed(0),null)
  val job=backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { estado.collect() }
  runCurrent();assertEquals(SituacionSla.ACTIVO,estado.value!!.situacion)
  advanceTimeBy(30_000L);runCurrent()
  assertEquals("SLA vencido hace menos de 1 min",estado.value!!.texto)
  job.cancel()
 }
}
