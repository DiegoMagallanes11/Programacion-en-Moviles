package com.tecsup.ayudacampus

import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tecsup.ayudacampus.data.local.AyudaDatabase
import com.tecsup.ayudacampus.data.local.entity.Usuario
import com.tecsup.ayudacampus.data.prefs.PreferenciasStore
import com.tecsup.ayudacampus.data.repository.AyudaRepositoryRoom
import com.tecsup.ayudacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CambioPerfilTest {
    private lateinit var db: AyudaDatabase
    private lateinit var preferencias: PreferenciasStore
    private val viewModels = ViewModelStore()

    @Before
    fun preparar() = runBlocking {
        val contextoObjetivo = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(contextoObjetivo, AyudaDatabase::class.java).build()
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(1, "Cuenta anterior", "anterior@campus.test", "1234", "CLIENTE"),
                Usuario(2, "Soporte", "soporte@campus.test", "admin", "ADMIN"),
                Usuario(3, "Cuenta nueva", "nueva@campus.test", "4321", "CLIENTE")
            )
        )
        preferencias = PreferenciasStore(contextoObjetivo)
        preferencias.cerrarSesion()
        preferencias.guardarSesion("CLIENTE", 3)
    }

    @After
    fun cerrar() = runBlocking {
        if (::preferencias.isInitialized) preferencias.cerrarSesion()
        withContext(Dispatchers.Main) { viewModels.clear() }
        if (::db.isInitialized) db.close()
    }

    @Test
    fun vuelveAlMismoEstudianteDespuesDeCambiarASoporte() = runBlocking {
        val repository = AyudaRepositoryRoom(db)
        val sesion = withContext(Dispatchers.Main) {
            SesionViewModel(repository, preferencias).also { viewModels.put("sesion", it) }
        }

        withTimeout(5_000) { sesion.usuarioActual.first { it?.id == 3 } }
        withContext(Dispatchers.Main) { sesion.cambiarPerfil() }
        withTimeout(5_000) { sesion.usuarioActual.first { it?.id == 2 } }
        withContext(Dispatchers.Main) { sesion.cambiarPerfil() }
        val estudiante = withTimeout(5_000) {
            sesion.usuarioActual.first { it?.rol == "CLIENTE" }
        }

        assertEquals(3, estudiante?.id)
        assertEquals("nueva@campus.test", estudiante?.correo)
    }
}
