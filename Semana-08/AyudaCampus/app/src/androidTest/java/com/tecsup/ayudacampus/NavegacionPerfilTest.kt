package com.tecsup.ayudacampus

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavegacionPerfilTest {
 @get:Rule val ui=createAndroidComposeRule<MainActivity>()
 private fun esperar(texto: String) {
  ui.waitUntil(20000) { ui.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() }
 }
 private fun click(texto: String) {
  if(ui.onAllNodesWithText(texto).fetchSemanticsNodes().isEmpty())
   ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(hasText(texto))
  val nodo=ui.onNode(hasText(texto) and hasClickAction())
  runCatching { nodo.performScrollTo() }
  nodo.performClick()
 }
 private fun captura(nombre: String) {
  ui.waitForIdle()
  val bitmap=InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
  val archivo=File(ui.activity.filesDir,"capturas/revision/$nombre.png")
  archivo.parentFile!!.mkdirs()
  archivo.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) }
  bitmap.recycle()
 }
 @Test fun pestanasPerfilTecladoYFormularioConservanSuEstado() {
  // Acepta una sesión de práctica existente; no elimina datos ni crea tickets.
  ui.waitUntil(20000) {
   listOf("Ingresar","Tickets","Perfil").any { ui.onAllNodesWithText(it).fetchSemanticsNodes().isNotEmpty() }
  }
  if(ui.onAllNodesWithText("Ingresar").fetchSemanticsNodes().isNotEmpty()) {
   ui.onNode(hasSetTextAction() and hasText("Correo")).performTextReplacement("magallanes@gmail.com")
   ui.onNode(hasSetTextAction() and hasText("Contraseña")).performTextReplacement("1234")
   click("Ingresar")
  }
  if(ui.onAllNodesWithText("Perfil").fetchSemanticsNodes().isEmpty()) {
   click("Ajustes");click("Cambiar a Estudiante")
  }
  esperar("Perfil")
  repeat(2) {
   click("Perfil");esperar("Mi perfil");esperar("Diego Magallanes")
   click("Nuevo");esperar("Nuevo ticket")
   click("Tickets");esperar("Hola, Diego")
  }
  click("Nuevo")
  val titulo=ui.onNode(hasSetTextAction() and hasText("Título"))
  titulo.performScrollTo().performTextReplacement("Borrador de navegación")
  titulo.performClick()
  click("Perfil");esperar("Mi perfil")
  click("Nuevo");esperar("Nuevo ticket")
  ui.onNode(hasSetTextAction() and hasText("Título")).assertTextContains("Borrador de navegación")
  ui.onNode(hasSetTextAction() and hasText("Título")).performTextClearance()
  click("Perfil");esperar("Diego Magallanes")
  captura("01-perfil-diego")
  click("Cambiar a Soporte TI");esperar("Panel de soporte")
  repeat(2) {
   click("Tickets");esperar("Bandeja de tickets")
   click("Categorías");esperar("Nueva categoría")
   click("Ajustes");esperar("Cambiar a Estudiante")
   click("Panel");esperar("Panel de soporte")
  }
  // CRUD de categorías desde la interfaz, incluida la validación de nombre.
  click("Categorías");click("Nueva categoría");click("Guardar categoría")
  esperar("Escribe el nombre de la categoría")
  ui.onNode(hasSetTextAction() and hasText("Nombre")).performTextReplacement("Auditoría sin tickets")
  ui.onNodeWithContentDescription("tag").performClick()
  click("Guardar categoría");esperar("Auditoría sin tickets")
  click("Auditoría sin tickets");esperar("Editar categoría")
  ui.onNode(hasSetTextAction() and hasText("Nombre")).performTextReplacement("Auditoría revisada")
  ui.onAllNodes(isToggleable())[0].performClick()
  click("Guardar categoría");esperar("Auditoría revisada")
  click("Auditoría revisada");esperar("Editar categoría")
  click("Eliminar categoría");esperar("Nueva categoría")
  ui.onAllNodesWithText("Auditoría revisada").assertCountEquals(0)
  click("Ajustes");click("Cambiar a Estudiante")
  esperar("Hola, Diego")
  captura("02-tickets-diego")
 }
}
