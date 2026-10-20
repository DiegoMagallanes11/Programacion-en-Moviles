package com.tecsup.ayudacampus
import androidx.compose.ui.test.*
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import android.graphics.Bitmap
import java.io.File
@RunWith(AndroidJUnit4::class)
class FlujoCompletoTest {
 @get:Rule val ui=createAndroidComposeRule<MainActivity>()
 private fun closeSoftKeyboard() {
  ui.runOnUiThread {
   val teclado=ui.activity.getSystemService(android.view.inputmethod.InputMethodManager::class.java)
   teclado.hideSoftInputFromWindow(ui.activity.window.decorView.windowToken,0)
  }
  ui.waitForIdle()
 }
 private fun esperar(texto: String) { ui.waitUntil(15000) { ui.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() } }
 private fun click(texto: String) {
  val nodo=ui.onNodeWithText(texto)
  runCatching { nodo.performScrollTo() }
  nodo.performClick()
 }
 private fun campo(etiqueta: String, valor: String) { ui.onNode(hasSetTextAction() and hasText(etiqueta)).performTextReplacement(valor) }
 private fun captura(nombre: String) {
  ui.waitForIdle()
  val inst=InstrumentationRegistry.getInstrumentation()
  val bitmap=inst.uiAutomation.takeScreenshot()
  val archivo=File(inst.targetContext.filesDir,"capturas/$nombre.png");archivo.parentFile!!.mkdirs()
  archivo.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) };bitmap.recycle()
 }
 @Test fun recorridoEstudianteYSoporte() {
  esperar("Ingresar")
  campo("Correo","juan@campus.test");campo("Contraseña","incorrecta");closeSoftKeyboard();click("Ingresar")
  esperar("Correo o contraseña incorrectos")
  campo("Contraseña","1234");closeSoftKeyboard();click("Ingresar");esperar("Hola, Diego")
  captura("01-estudiante-tickets")
  click("Nuevo");esperar("Enviar ticket");captura("02-estudiante-nuevo");ui.onNodeWithText("Enviar ticket").performScrollTo().performClick()
  esperar("Escribe al menos 5 caracteres");ui.onNodeWithText("Hardware").performScrollTo().performClick()
  campo("Título","Equipo de prueba final");campo("Ubicación","Laboratorio 8");ui.onNodeWithText("Descripción").performScrollTo()
  campo("Descripción","El equipo no enciende durante la práctica del laboratorio.");closeSoftKeyboard()
  ui.onNodeWithText("Enviar ticket").performScrollTo().performClick();esperar("Hola, Diego");esperar("Equipo de prueba final")
  // Restaurar la pestaña no debe repetir la navegación del envío anterior.
  click("Nuevo");esperar("Enviar ticket")
  assertEquals("",ui.onNode(hasSetTextAction() and hasText("Título")).fetchSemanticsNode().config[SemanticsProperties.EditableText].text)
  click("Tickets");esperar("Equipo de prueba final")
  click("Perfil");esperar("Cambiar a Soporte TI");click("Cambiar a Soporte TI");esperar("Panel de soporte")
  captura("04-soporte-panel")
  click("Tickets");esperar("Bandeja de tickets");captura("05-soporte-bandeja")
  ui.onAllNodes(hasScrollToNodeAction())[0].performScrollToNode(hasText("Equipo de prueba final"))
  ui.onNodeWithText("Equipo de prueba final").performClick();esperar("Guardar cambios")
  ui.onNodeWithText("Cerrado").assertIsNotEnabled()
  click("En proceso");click("Guardar cambios");esperar("Cambios guardados")
  ui.onNodeWithText("Escribe una respuesta").performScrollTo();campo("Escribe una respuesta","Revisamos el equipo y ya funciona correctamente.");closeSoftKeyboard()
  ui.onNodeWithText("Enviar respuesta").performScrollTo().performClick();esperar("Respuesta enviada")
  ui.onNodeWithText("Resuelto").performScrollTo().performClick();ui.onNodeWithText("Guardar cambios").performScrollTo().performClick();esperar("Cambios guardados")
  captura("06-soporte-atender")
  click("Ajustes");esperar("Cambiar a Estudiante");click("Cambiar a Estudiante");esperar("Hola, Diego")
  click("Equipo de prueba final");esperar("Tu ticket ha sido resuelto")
  ui.onNodeWithContentDescription("Calificar 4 estrellas").performScrollTo().performClick()
  captura("03-estudiante-detalle")
  ui.onNodeWithText("Confirmar y cerrar").performScrollTo().performClick();esperar("Atención finalizada")
  ui.onNodeWithText("Enviar comentario").assertDoesNotExist()
  click("Perfil");esperar("Modo oscuro")
  ui.onAllNodes(isToggleable())[0].performClick()
  ui.activityRule.scenario.recreate();esperar("Modo oscuro")
  ui.onAllNodes(isToggleable())[0].assertIsOn()
  click("Cambiar a Soporte TI");esperar("Panel de soporte");click("Ajustes");esperar("Recibir tickets")
  ui.onAllNodes(isToggleable())[2].performClick();click("Cambiar a Estudiante");esperar("Hola, Diego")
  click("Nuevo");esperar("Mesa de ayuda cerrada");ui.onNodeWithText("Enviar ticket").performScrollTo().assertIsNotEnabled()
  click("Perfil");esperar("Cambiar a Soporte TI");click("Cambiar a Soporte TI");esperar("Panel de soporte");click("Ajustes");esperar("Recibir tickets")
  ui.onAllNodes(isToggleable())[2].performClick();ui.onAllNodes(isToggleable())[0].performClick()
  ui.onNodeWithText("Cerrar sesión").performScrollTo().performClick();esperar("Ingresar")
  campo("Correo","juan@campus.test");campo("Contraseña","1234");closeSoftKeyboard();click("Ingresar");esperar("Hola, Diego")
  click("Perfil");esperar("Modo oscuro");ui.onAllNodes(isToggleable())[0].performClick()
 }
}
