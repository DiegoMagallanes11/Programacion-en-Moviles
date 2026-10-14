package com.tecsup.ayudacampus
import android.app.Application
import com.tecsup.ayudacampus.data.local.*
import com.tecsup.ayudacampus.data.repository.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
class AyudaApp : Application() {
 val database by lazy { AyudaDatabase.obtener(this) }
 val repository: AyudaRepository by lazy { AyudaRepositoryRoom(database) }
 val listo = MutableStateFlow(false)
 val errorInicio = MutableStateFlow<String?>(null)
 override fun onCreate() {
  super.onCreate()
  CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
   try { DatosIniciales.sembrar(database); listo.value = true }
   catch (e: Exception) { errorInicio.value = "No se pudo preparar la base de datos" }
  }
 }
}
