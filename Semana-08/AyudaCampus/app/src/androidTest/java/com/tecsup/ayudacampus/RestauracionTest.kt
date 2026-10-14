package com.tecsup.ayudacampus
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.*
import org.junit.runner.RunWith

/** Ejecutar después de FlujoCompletoTest, en una nueva instrumentación y sin borrar datos. */
@RunWith(AndroidJUnit4::class)
class RestauracionTest {
 @get:Rule val ui=createAndroidComposeRule<MainActivity>()
 @Test fun restauraSesionTicketsYTemaTrasReiniciarProceso() {
  ui.waitUntil(15000) { ui.onAllNodesWithText("Hola, Juan").fetchSemanticsNodes().isNotEmpty() }
  ui.onNodeWithText("Perfil").performClick()
  ui.waitUntil(15000) { ui.onAllNodesWithText("Modo oscuro").fetchSemanticsNodes().isNotEmpty() }
  val modo=ui.onAllNodes(isToggleable())[0]
  modo.assertIsOn()
  ui.onNodeWithText("7").assertExists()
  modo.performClick()
  ui.waitUntil(10000) { ui.onAllNodes(isToggleable())[0].fetchSemanticsNode().config[SemanticsProperties.ToggleableState]==ToggleableState.Off }
 }
}
