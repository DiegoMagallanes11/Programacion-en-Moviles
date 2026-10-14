package com.tecsup.ayudacampus.data.prefs
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import java.io.IOException
private val Context.dataStore by preferencesDataStore("ajustes")
class PreferenciasStore(context: Context) {
 private val store = context.applicationContext.dataStore
 private object Claves {
  val perfil=stringPreferencesKey("perfilActivo"); val usuario=intPreferencesKey("usuarioId")
  val oscuro=booleanPreferencesKey("modoOscuro"); val notificaciones=booleanPreferencesKey("notificaciones")
  val recibir=booleanPreferencesKey("recibirTickets")
 }
 private val datos = store.data.catch { if(it is IOException) emit(emptyPreferences()) else throw it }
 val perfilActivo = datos.map { it[Claves.perfil] ?: "" }.distinctUntilChanged()
 val usuarioId = datos.map { it[Claves.usuario] ?: 0 }.distinctUntilChanged()
 val sesion = datos.map { (it[Claves.perfil] ?: "") to (it[Claves.usuario] ?: 0) }.distinctUntilChanged()
 val modoOscuro = datos.map { it[Claves.oscuro] ?: false }.distinctUntilChanged()
 val notificaciones = datos.map { it[Claves.notificaciones] ?: true }.distinctUntilChanged()
 val recibirTickets = datos.map { it[Claves.recibir] ?: true }.distinctUntilChanged()
 suspend fun guardarSesion(rol: String, usuarioId: Int) { store.edit { it[Claves.perfil]=rol;it[Claves.usuario]=usuarioId } }
 suspend fun guardarModoOscuro(b: Boolean) { store.edit { it[Claves.oscuro]=b } }
 suspend fun guardarNotificaciones(b: Boolean) { store.edit { it[Claves.notificaciones]=b } }
 suspend fun guardarRecibirTickets(b: Boolean) { store.edit { it[Claves.recibir]=b } }
 suspend fun cerrarSesion() { store.edit { it.remove(Claves.perfil);it.remove(Claves.usuario) } }
}
