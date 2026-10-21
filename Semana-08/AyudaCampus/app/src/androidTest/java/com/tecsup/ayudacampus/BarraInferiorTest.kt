package com.tecsup.ayudacampus

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BarraInferiorTest {
 @get:Rule val ui=createAndroidComposeRule<MainActivity>()
 private fun esperar(texto: String) {
  ui.waitUntil(20000) { ui.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() }
 }
 private fun click(texto: String) {
  val n=ui.onNode(hasText(texto) and hasClickAction())
  runCatching { n.performScrollTo() };n.performClick()
 }
 private fun abrirTicket() {
  ui.onAllNodes(hasClickAction() and hasText("#",substring=true))[0].performClick()
  esperar("Conversación")
 }
 @Test fun cambiarPestanasDesdeDetalleYNuevoNoRestauraElDetalle() {
  // Requiere una sesión de práctica y al menos un ticket. No escribe datos.
  ui.waitUntil(20000) { listOf("Perfil","Ajustes").any { ui.onAllNodesWithText(it).fetchSemanticsNodes().isNotEmpty() } }
  if(ui.onAllNodesWithText("Ajustes").fetchSemanticsNodes().isNotEmpty()) {
   click("Ajustes");click("Cambiar a Estudiante")
  }
  click("Tickets");esperar("Hola, Diego")
  repeat(2) {
   abrirTicket()
   ui.onNode(hasText("Tickets") and hasClickAction()).assertIsSelected()
   click("Nuevo");esperar("Nuevo ticket")
   click("Tickets");esperar("Hola, Diego")
   ui.onAllNodesWithContentDescription("Volver").assertCountEquals(0)
   abrirTicket();click("Perfil");esperar("Mi perfil")
   click("Tickets");esperar("Hola, Diego")
   abrirTicket();click("Tickets");esperar("Hola, Diego")
  }
  click("Nuevo")
  val campo=hasSetTextAction() and hasText("Título")
  ui.onNode(campo).performScrollTo().performTextReplacement("Borrador de barra")
  ui.onNode(campo).performClick()
  click("Perfil");esperar("Mi perfil")
  click("Nuevo");esperar("Nuevo ticket")
  ui.onNode(campo).assertTextContains("Borrador de barra")
  ui.onNode(campo).performTextClearance()
  click("Tickets");esperar("Hola, Diego")
  click("Perfil");click("Cambiar a Soporte TI");esperar("Panel de soporte")
  repeat(2) {
   click("Tickets");esperar("Bandeja de tickets");abrirTicket()
   ui.onNode(hasText("Tickets") and hasClickAction()).assertIsSelected()
   click("Panel");esperar("Panel de soporte")
   click("Tickets");esperar("Bandeja de tickets");abrirTicket()
   click("Ajustes");esperar("Cambiar a Estudiante")
   click("Tickets");esperar("Bandeja de tickets")
   ui.onAllNodesWithContentDescription("Volver").assertCountEquals(0)
   click("Categorías");click("Nueva categoría");esperar("Nueva categoría")
   click("Categorías");esperar("Organiza las incidencias del campus")
   click("Nueva categoría");click("Panel");esperar("Panel de soporte")
   click("Categorías");esperar("Organiza las incidencias del campus")
  }
  click("Ajustes");click("Cambiar a Estudiante");esperar("Hola, Diego")
 }
}
