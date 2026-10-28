package com.tecsup.ayudacampus

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tecsup.ayudacampus.data.local.AyudaDatabase
import com.tecsup.ayudacampus.data.repository.AyudaRepositoryRoom
import com.tecsup.ayudacampus.ui.cliente.RegistroScreen
import com.tecsup.ayudacampus.ui.theme.AyudaTheme
import com.tecsup.ayudacampus.viewmodel.RegistroViewModel
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistroUiTest {
    @get:Rule
    val ui = createComposeRule()

    private lateinit var db: AyudaDatabase
    private lateinit var repository: AyudaRepositoryRoom
    private val viewModels = ViewModelStore()

    @Before
    fun preparar() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AyudaDatabase::class.java
        ).build()
        repository = AyudaRepositoryRoom(db)
    }

    @After
    fun cerrar() {
        runBlocking { withContext(Dispatchers.Main) { viewModels.clear() } }
        db.close()
    }

    @Test
    fun validaFormularioYCierraRegistroConCorreoCreado() {
        val correoDevuelto = AtomicReference<String?>(null)
        val viewModel = RegistroViewModel(repository).also {
            viewModels.put("registro", it)
        }

        ui.setContent {
            AyudaTheme {
                RegistroScreen(
                    viewModel = viewModel,
                    onVolver = {},
                    onRegistrado = correoDevuelto::set
                )
            }
        }

        ui.onNode(hasClickAction() and hasText("Crear cuenta")).performClick()
        ui.onNodeWithText("Escribe un correo válido").assertExists()
        ui.onNodeWithText("La contraseña debe tener al menos 4 caracteres").assertExists()

        ui.onNode(hasSetTextAction() and hasText("Nombre completo"))
            .performTextReplacement("Cuenta de prueba")
        ui.onNode(hasSetTextAction() and hasText("Correo"))
            .performTextReplacement("registro@campus.test")
        ui.onNode(hasSetTextAction() and hasText("Contraseña"))
            .performTextReplacement("4321")
        ui.onNode(hasSetTextAction() and hasText("Confirmar contraseña"))
            .performTextReplacement("4321")
        ui.onNode(hasClickAction() and hasText("Crear cuenta")).performClick()

        ui.waitUntil(10_000) { correoDevuelto.get() == "registro@campus.test" }
        val usuario = runBlocking { repository.login("registro@campus.test", "4321") }
        assertNotNull(usuario)
    }
}
