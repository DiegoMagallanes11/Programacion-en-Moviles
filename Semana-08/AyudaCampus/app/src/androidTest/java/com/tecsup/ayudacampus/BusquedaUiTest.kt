package com.tecsup.ayudacampus

import android.graphics.Bitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.model.promedioLegible
import java.io.File
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.*
import org.junit.runner.RunWith

/** Emulador dedicado. Ejecutar después de RestauracionTest; añade datos ficticios propios. */
@RunWith(AndroidJUnit4::class)
class BusquedaUiTest {
 @get:Rule val ui=createAndroidComposeRule<MainActivity>()
 @After fun registrarPantalla() { ui.onRoot().printToLog("BusquedaUi") }
 private fun esperar(texto: String) {
  ui.waitUntil(20_000L) {
   ui.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() || runCatching {
    ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(hasText(texto))
    ui.onNodeWithText(texto).assertExists()
   }.isSuccess
  }
 }
 private fun click(texto: String) {
  mostrar(texto)
  ui.onNodeWithText(texto).also { runCatching { it.performScrollTo() };it.performClick() }
 }
 private fun mostrar(texto: String) {
  if(ui.onAllNodesWithText(texto).fetchSemanticsNodes().isEmpty())
   ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(hasText(texto))
 }
 private fun ocultarTeclado() {
  ui.runOnUiThread {
   ui.activity.getSystemService(android.view.inputmethod.InputMethodManager::class.java)
    .hideSoftInputFromWindow(ui.activity.window.decorView.windowToken,0)
  }
  ui.waitForIdle()
 }
 private fun buscar(texto: String) {
  val campo=hasSetTextAction() and hasText("Buscar por título, descripción o número")
  ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(campo)
  ui.onNode(campo).performTextReplacement(texto)
 }
 private fun mostrarTicket() {
  ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(hasText("Incidencia histórica SLA"))
  ui.onNodeWithText("Incidencia histórica SLA").assertExists()
 }
 private fun captura(nombre: String) {
  ui.waitForIdle()
  val inst=InstrumentationRegistry.getInstrumentation()
  // La semántica de Compose puede actualizarse antes de que SurfaceFlinger dibuje.
  inst.uiAutomation.waitForIdle(100L,3_000L)
  inst.waitForIdleSync()
  android.os.SystemClock.sleep(250L)
  val bitmap=inst.uiAutomation.takeScreenshot()
  val archivo=File(inst.targetContext.filesDir,"capturas/fase-2/$nombre.png")
  archivo.parentFile!!.mkdirs()
  archivo.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) }
  bitmap.recycle()
 }
 @Test fun filtrosCombinadosRetornoYActualizacionReactiva() {
  ui.waitUntil(20_000L) {
   ui.onAllNodesWithText("Hola, Diego").fetchSemanticsNodes().isNotEmpty() ||
    ui.onAllNodesWithText("Panel de soporte").fetchSemanticsNodes().isNotEmpty()
  }
  if(ui.onAllNodesWithText("Panel de soporte").fetchSemanticsNodes().isNotEmpty()) {
   click("Ajustes");click("Cambiar a Estudiante")
  }
  esperar("Hola, Diego")
  val app=InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as AyudaApp
  val id=runBlocking {
   app.listo.first { it }
   val repo=app.repository
   if(repo.observarCategorias().first().none { it.nombre=="Archivo Fase 2" })
    repo.guardarCategoria(Categoria(nombre="Archivo Fase 2",icono="wifi",activa=false))
   val categoria=repo.observarCategorias().first().first { it.nombre=="Archivo Fase 2" }
   val cliente=repo.login("magallanes@gmail.com","1234")!!
   val previo=repo.observarTodosLosTickets().first().firstOrNull { it.ticket.titulo=="Incidencia histórica SLA" }
   if(previo!=null) {
    app.database.ticketDao().actualizarEstado(previo.ticket.id,"ABIERTO")
    app.database.ticketDao().actualizarPrioridad(previo.ticket.id,"ALTA")
    previo.ticket.id
   } else app.database.ticketDao().insertarTicket(Ticket(clienteId=cliente.id,categoriaId=categoria.id,
    titulo="Incidencia histórica SLA",descripcion="Red de laboratorio Fase 2",ubicacion="Laboratorio 8",
    prioridad="ALTA",fecha=System.currentTimeMillis()-5L*3_600_000L)).toInt()
  }
  ui.onNode(hasSetTextAction() and hasText("Buscar por título o número"))
   .performTextReplacement("#$id")
  ocultarTeclado();esperar("Incidencia histórica SLA")
  captura("02-sla-mis-tickets")
  click("Incidencia histórica SLA")
  esperar("Conversación")
  ui.onNode(hasText("SLA vencido hace",substring=true)).assertExists()
  captura("03-sla-detalle-estudiante")
  click("Perfil");esperar("Cambiar a Soporte TI");click("Cambiar a Soporte TI")
  esperar("Tiempo promedio de resolución")
  val promedioEsperado=runBlocking { promedioLegible(app.repository.observarPromedioResolucion().first()) }
  esperar(promedioEsperado);captura("01-promedio-panel")
  click("Tickets");esperar("Bandeja de tickets")
  buscar("  rED DE LABORATORIO  ")
  // Con el teclado abierto los controles siguen siendo alcanzables por desplazamiento.
  click("ALTA");click("Abiertos");click("Categoría: Todas")
  click("Archivo Fase 2 (inactiva)");ocultarTeclado()
  esperar("1 resultado")
  mostrarTicket()
  captura("05-busqueda-combinada")
  click("Incidencia histórica SLA");esperar("Guardar cambios")
  ui.onNode(hasText("SLA vencido hace",substring=true)).assertExists()
  captura("04-sla-atencion-soporte")
  ui.onNodeWithContentDescription("Volver").performClick();esperar("Bandeja de tickets")
  val campo=hasSetTextAction() and hasText("Buscar por título, descripción o número")
  ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(campo)
  ui.onNode(campo).assertTextContains("  rED DE LABORATORIO  ")
  ui.onNodeWithText("ALTA").assertIsSelected()
  ui.onNodeWithText("Categoría: Archivo Fase 2").assertExists()
  runBlocking { app.database.ticketDao().actualizarEstado(id,"RESUELTO") }
  esperar("No hay tickets que coincidan con los filtros")
  ui.onNodeWithText("No hay tickets que coincidan con los filtros").performScrollTo()
  captura("06-busqueda-sin-resultados")
  click("Limpiar filtros")
  val total=runBlocking { app.repository.observarTodosLosTickets().first().size }
  esperar("$total resultados")
  for (numero in listOf(id.toString(),"#${id.toString().padStart(4,'0')}")) {
   buscar(numero);ocultarTeclado();esperar("1 resultado")
   mostrarTicket()
  }
  click("Limpiar filtros");esperar("$total resultados")
 }
}
