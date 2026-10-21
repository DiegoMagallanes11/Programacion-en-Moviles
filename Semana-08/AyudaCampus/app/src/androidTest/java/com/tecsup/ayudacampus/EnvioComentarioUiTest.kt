package com.tecsup.ayudacampus

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.ui.cliente.DetalleTicketScreen
import com.tecsup.ayudacampus.ui.soporte.AtenderTicketScreen
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.*
import java.io.IOException
import java.lang.reflect.Proxy
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.flow.flowOf
import org.junit.*
import org.junit.runner.RunWith

/** Pantallas aisladas con Repository simulado; no usa ni escribe la base real. */
@RunWith(AndroidJUnit4::class)
class EnvioComentarioUiTest {
 @get:Rule val ui=createComposeRule()
 private val store=ViewModelStore()
 private val intentos=AtomicInteger()
 private val cliente=Usuario(1,"Estudiante de prueba","cliente@campus.test","","CLIENTE")
 private val detalle=TicketConDetalle(Ticket(id=1,clienteId=1,categoriaId=1,titulo="Ticket de prueba",descripcion="Prueba aislada",ubicacion="Aula",fecha=1000L),emptyList(),Categoria(id=1,nombre="Prueba",icono="pc"),cliente)
 private val repo=Proxy.newProxyInstance(AyudaRepository::class.java.classLoader,arrayOf(AyudaRepository::class.java)) { _,m,_ ->
  when(m.name) {
   "observarTicket" -> flowOf(detalle)
   "agregarComentario" -> { if(intentos.incrementAndGet()==1) throw IOException("Fallo de prueba");Unit }
   else -> error("Operación inesperada: ${m.name}")
  }
 } as AyudaRepository
 @After fun limpiar() { ui.runOnIdle { store.clear() } }
 private fun comprobar(campo: String,boton: String) {
  val texto="Mensaje que debe conservarse"
  val editor=ui.onNode(hasSetTextAction() and hasText(campo))
  editor.performScrollTo().performTextReplacement(texto)
  ui.onNodeWithText(boton).performScrollTo().performClick()
  ui.waitUntil(10000) { ui.onAllNodesWithText("No se pudo guardar el cambio").fetchSemanticsNodes().isNotEmpty() }
  editor.assertTextContains(texto)
  Assert.assertEquals(1,intentos.get())
  ui.onNodeWithText(boton).performScrollTo().performClick()
  ui.waitUntil(10000) { ui.onAllNodes(hasSetTextAction() and hasText(texto)).fetchSemanticsNodes().isEmpty() }
  Assert.assertEquals(2,intentos.get())
  editor.assertExists()
 }
 @Test fun estudianteConservaComentarioAlFallarYLimpiaTrasReintentar() {
  val vm=TicketDetalleViewModel(repo,1,cliente).also { store.put("detalle",it) }
  ui.setContent { AyudaTheme { DetalleTicketScreen(vm,1) } }
  ui.waitUntil(10000) { ui.onAllNodesWithText("Conversación").fetchSemanticsNodes().isNotEmpty() }
  comprobar("Escribe un comentario","Enviar comentario")
 }
 @Test fun soporteConservaRespuestaAlFallarYLimpiaTrasReintentar() {
  val soporte=Usuario(2,"Soporte","soporte@campus.test","","ADMIN")
  val vm=AtenderViewModel(repo,1,soporte).also { store.put("atender",it) }
  ui.setContent { AyudaTheme { AtenderTicketScreen(vm,2) } }
  ui.waitUntil(10000) { ui.onAllNodesWithText("Conversación").fetchSemanticsNodes().isNotEmpty() }
  comprobar("Escribe una respuesta","Enviar respuesta")
 }
}
