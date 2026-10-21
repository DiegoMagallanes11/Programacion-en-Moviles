package com.tecsup.ayudacampus

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.core.view.WindowInsetsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ControlesPerfilTest {
 @get:Rule val ui=createAndroidComposeRule<MainActivity>()
 private fun esperar(texto: String) {
  ui.waitUntil(20000) { ui.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() }
 }
 private fun click(texto: String) {
  val nodo=ui.onNode(hasText(texto) and hasClickAction())
  runCatching { nodo.performScrollTo() }
  nodo.performClick()
 }
 private fun interruptor(texto: String)=ui.onNode(hasText(texto) and isToggleable())
 private fun valor(texto: String)=interruptor(texto).fetchSemanticsNode().config[SemanticsProperties.ToggleableState]==ToggleableState.On
 private fun establecer(texto: String,valor: Boolean) {
  if(this.valor(texto)!=valor) click(texto)
  ui.waitUntil(10000) { this.valor(texto)==valor }
 }
 @Test fun preferenciasMesaSesionYRegresoConTeclado() {
  // No crea tickets ni altera sus estados o conversaciones. Restaura las preferencias.
  ui.waitUntil(20000) { listOf("Ingresar","Perfil","Ajustes").any { ui.onAllNodesWithText(it).fetchSemanticsNodes().isNotEmpty() } }
  if(ui.onAllNodesWithText("Ingresar").fetchSemanticsNodes().isNotEmpty()) login()
  if(ui.onAllNodesWithText("Ajustes").fetchSemanticsNodes().isNotEmpty()) {
   click("Ajustes");click("Cambiar a Estudiante")
  }
  click("Perfil");esperar("Diego Magallanes")
  val oscuro=valor("Modo oscuro")
  val avisos=valor("Notificaciones")
  establecer("Modo oscuro",!oscuro)
  establecer("Notificaciones",!avisos)
  ui.activityRule.scenario.recreate()
  esperar("Perfil");click("Perfil")
  interruptor("Modo oscuro").assert(if(!oscuro) isOn() else isOff())
  interruptor("Notificaciones").assert(if(!avisos) isOn() else isOff())
  establecer("Modo oscuro",oscuro);establecer("Notificaciones",avisos)
  click("Cambiar a Soporte TI");esperar("Panel de soporte")
  click("Ajustes")
  val abierta=valor("Recibir tickets")
  establecer("Recibir tickets",false)
  click("Cambiar a Estudiante");click("Nuevo")
  esperar("Mesa de ayuda cerrada")
  ui.onNodeWithText("Enviar ticket").assertIsNotEnabled()
  click("Perfil");click("Cambiar a Soporte TI");click("Ajustes")
  establecer("Recibir tickets",true)
  click("Cambiar a Estudiante");click("Nuevo")
  ui.onNodeWithText("Enviar ticket").assertIsEnabled()
  click("Perfil");click("Cambiar a Soporte TI");click("Ajustes")
  establecer("Recibir tickets",abierta)
  click("Cambiar a Estudiante");click("Tickets")
  ui.onAllNodes(hasClickAction() and hasText("#",substring=true))[0].performClick()
  esperar("Conversación")
  val comentario=ui.onNode(hasSetTextAction() and hasText("Escribe un comentario"))
  comentario.performScrollTo().performClick()
  ui.waitUntil(10000) { tecladoVisible() }
  ui.onNodeWithContentDescription("Volver").performClick()
  esperar("Hola, Diego")
  ui.waitUntil(10000) { !tecladoVisible() }
  click("Perfil");click("Cerrar sesión");esperar("Ingresar")
  login();esperar("Hola, Diego");click("Perfil");esperar("Diego Magallanes")
 }
 private fun tecladoVisible(): Boolean {
  var visible=false
  ui.runOnUiThread {
   visible=ui.activity.window.decorView.rootWindowInsets?.let {
    WindowInsetsCompat.toWindowInsetsCompat(it).isVisible(WindowInsetsCompat.Type.ime())
   } ?: false
  }
  return visible
 }
 private fun login() {
  click("Estudiante")
  ui.onNode(hasSetTextAction() and hasText("Correo")).performTextReplacement("juan@campus.test")
  ui.onNode(hasSetTextAction() and hasText("Contraseña")).performTextReplacement("1234")
  click("Ingresar");esperar("Hola, Diego")
 }
}
