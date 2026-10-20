package com.tecsup.ayudacampus

import com.tecsup.ayudacampus.model.*
import org.junit.Assert.*
import org.junit.Test
import androidx.lifecycle.ViewModelStore
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.viewmodel.PanelViewModel
import java.lang.reflect.Proxy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*

class PromedioTest {
 @OptIn(ExperimentalCoroutinesApi::class)
 @Test fun panelDistingueCargaAusenciaYCeroYRecibeCambios() = runTest {
  Dispatchers.setMain(StandardTestDispatcher(testScheduler))
  val store=ViewModelStore()
  try {
   val promedio=MutableSharedFlow<Double?>(replay=1)
   val repo=Proxy.newProxyInstance(AyudaRepository::class.java.classLoader,arrayOf(AyudaRepository::class.java)) { _,metodo,_ ->
    when(metodo.name) {
     "observarPromedioResolucion" -> promedio
     "observarCantidadPorEstado","observarAltasSinAtender" -> flowOf(0)
     "observarCantidadPorCategoria" -> flowOf(emptyList<ConteoCategoria>())
     else -> error("Operación no esperada: ${metodo.name}")
    }
   } as AyudaRepository
   val vm=PanelViewModel(repo).also { store.put("panel",it) }
   val job=backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.promedioResolucion.collect() }
   runCurrent();assertEquals("Cargando promedio…",vm.promedioResolucion.value)
   promedio.emit(null);runCurrent();assertEquals("Sin tickets cerrados",vm.promedioResolucion.value)
   promedio.emit(0.0);runCurrent();assertEquals("Menos de 1 min",vm.promedioResolucion.value)
   promedio.emit(8_100_000.0);runCurrent();assertEquals("2 h 15 min",vm.promedioResolucion.value)
   job.cancelAndJoin()
  } finally { store.clear();runCurrent();Dispatchers.resetMain() }
 }
 @Test fun ausenciaNoEsCero() {
  assertEquals("Sin tickets cerrados", promedioLegible(null))
  assertEquals("Menos de 1 min", promedioLegible(0.0))
 }
 @Test fun unidadesComprensibles() {
  assertEquals("Menos de 1 min", duracionLegible(59_999L))
  assertEquals("1 min", duracionLegible(60_000L))
  assertEquals("2 h 15 min", duracionLegible(8_100_000L))
  assertEquals("72 h", duracionLegible(259_200_000L))
 }
}
