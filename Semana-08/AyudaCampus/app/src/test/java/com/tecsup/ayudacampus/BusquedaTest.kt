package com.tecsup.ayudacampus

import androidx.lifecycle.ViewModelStore
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle
import com.tecsup.ayudacampus.data.repository.AyudaRepository
import com.tecsup.ayudacampus.model.*
import com.tecsup.ayudacampus.viewmodel.BandejaViewModel
import java.lang.reflect.Proxy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class BusquedaTest {
 private val store=ViewModelStore()
 private val cliente=Usuario(1,"Juan","juan@test","1234","CLIENTE")
 private val red=Categoria(10,"Red","wifi",false)
 private val hardware=Categoria(20,"Hardware","pc")
 private fun detalle(id: Int, prioridad: String, fecha: Long, categoria: Categoria=red, estado: String="ABIERTO") =
  TicketConDetalle(Ticket(id,1,categoria.id,"Equipo $id","Conexión del aula", "Aula",prioridad,estado,fecha),emptyList(),categoria,cliente)
 private val lista=listOf(detalle(2,"ALTA",200),detalle(1,"ALTA",100),detalle(3,"MEDIA",300,hardware,"EN_PROCESO"),detalle(4,"BAJA",400))
 @Before fun preparar() { Dispatchers.setMain(StandardTestDispatcher()) }
 @After fun cerrar() { store.clear();Dispatchers.resetMain() }

 @Test fun busquedaEstudianteAdmiteNumeroVisibleSinCoincidenciaVacia() {
  val t=lista.first().ticket
  for(texto in listOf("2","#2"," #0002 "," eQUiPo 2 ","   "))
   assertTrue(coincideBusquedaTicket(t,texto))
  assertFalse(coincideBusquedaTicket(t,"#"))
  assertFalse(coincideBusquedaTicket(t,"Conexión"))
  assertTrue(coincideBusquedaTicket(t,"Conexión",incluirDescripcion=true))
  assertFalse(coincideBusquedaTicket(t.copy(id=12,titulo="Otro equipo"),"2"))
 }

 @Test fun filtrosIndividualesTextoYNumero() {
  fun ids(f: FiltrosBandeja)=filtrarBandeja(lista,f).map { it.ticket.id }
  assertEquals(listOf(3),ids(FiltrosBandeja(estado="EN_PROCESO")))
  assertEquals(listOf(2,1),ids(FiltrosBandeja(prioridad=Prioridad.ALTA)))
  assertEquals(listOf(2,1,4),ids(FiltrosBandeja(categoriaId=red.id)))
  assertEquals(listOf(2),ids(FiltrosBandeja(texto="  eQUiPo 2  ")))
  assertEquals(listOf(2,1,3,4),ids(FiltrosBandeja(texto=" CONEXIÓN ")))
  for (texto in listOf("2","#2"," #0002 ")) assertEquals(listOf(2),ids(FiltrosBandeja(texto=texto)))
  assertTrue(ids(FiltrosBandeja(texto="#")).isEmpty())
  assertEquals(listOf(2,1,3,4),ids(FiltrosBandeja(texto="   ")))
 }
 @Test fun filtrosSonAndSinAlterarOrden() {
  val todos=FiltrosBandeja("ABIERTO"," conexión ",Prioridad.ALTA,red.id)
  assertEquals(listOf(2,1),filtrarBandeja(lista,todos).map { it.ticket.id })
  assertTrue(filtrarBandeja(lista,todos.copy(categoriaId=hardware.id)).isEmpty())
  assertTrue(filtrarBandeja(emptyList(),todos).isEmpty())
 }
 @Test fun viewModelReaccionaADatosCategoriasYLimpiar() = runTest {
  val tickets=MutableStateFlow(lista)
  val categorias=MutableStateFlow(listOf(red,hardware))
  val repo=Proxy.newProxyInstance(AyudaRepository::class.java.classLoader,arrayOf(AyudaRepository::class.java)) { _,metodo,_ ->
   when(metodo.name) {
    "observarTodosLosTickets" -> tickets
    "observarCategorias" -> categorias
    else -> error("Operación no esperada: ${metodo.name}")
   }
  } as AyudaRepository
  val vm=BandejaViewModel(repo).also { store.put("bandeja",it) }
  val job=backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.pantalla.collect() }
  runCurrent()
  assertFalse(vm.pantalla.value.cargando)
  assertEquals(listOf(red,hardware),vm.pantalla.value.categorias)
  vm.cambiarEstado("ABIERTO");vm.buscar(" CONEXIÓN ");vm.cambiarPrioridad(Prioridad.ALTA);vm.cambiarCategoria(red.id)
  runCurrent();assertEquals(listOf(2,1),vm.pantalla.value.tarjetas.map { it.detalle.ticket.id })
  tickets.value=listOf(detalle(5,"ALTA",500))+lista
  runCurrent();assertEquals(listOf(5,2,1),vm.pantalla.value.tarjetas.map { it.detalle.ticket.id })
  // Modificar el texto de un ticket debe sacarlo de los resultados sin tocar los filtros.
  tickets.value=tickets.value.map { if(it.ticket.id==5) it.copy(ticket=it.ticket.copy(descripcion="Otro problema")) else it }
  runCurrent();assertEquals(listOf(2,1),vm.pantalla.value.tarjetas.map { it.detalle.ticket.id })
  vm.buscar("inexistente");runCurrent()
  assertTrue(vm.pantalla.value.tarjetas.isEmpty());assertEquals(5,vm.pantalla.value.total)
  categorias.value=listOf(hardware)
  runCurrent();assertNull(vm.pantalla.value.filtros.categoriaId)
  vm.limpiarFiltros();runCurrent()
  assertEquals(FiltrosBandeja(),vm.pantalla.value.filtros)
  assertEquals(5,vm.pantalla.value.tarjetas.size)
  tickets.value=emptyList();runCurrent()
  assertEquals(0,vm.pantalla.value.total)
  job.cancel()
 }
}
