package com.tecsup.ayudacampus.data.local
import android.content.Context
import androidx.room.*
import com.tecsup.ayudacampus.data.local.entity.*
import com.tecsup.ayudacampus.data.local.dao.*
import com.tecsup.ayudacampus.data.local.dao.CategoriaDao
@Database(entities = [Categoria::class, Usuario::class, Ticket::class, Comentario::class], version = 2, exportSchema = false)
abstract class AyudaDatabase : RoomDatabase() {
 abstract fun categoriaDao(): CategoriaDao
 abstract fun usuarioDao(): UsuarioDao
 abstract fun ticketDao(): TicketDao
 companion object {
  @Volatile private var instance: AyudaDatabase? = null
  fun obtener(context: Context): AyudaDatabase = instance ?: synchronized(this) {
   instance ?: Room.databaseBuilder(context.applicationContext, AyudaDatabase::class.java, "ayudacampus.db").addMigrations(object : androidx.room.migration.Migration(1,2) {
    override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
     db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, correo TEXT NOT NULL, clave TEXT NOT NULL, rol TEXT NOT NULL)")
     db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_usuarios_correo ON usuarios(correo)")
     db.execSQL("CREATE TABLE IF NOT EXISTS tickets (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, clienteId INTEGER NOT NULL, categoriaId INTEGER NOT NULL, titulo TEXT NOT NULL, descripcion TEXT NOT NULL, ubicacion TEXT NOT NULL, prioridad TEXT NOT NULL, estado TEXT NOT NULL, fecha INTEGER NOT NULL, fechaCierre INTEGER, calificacion INTEGER, FOREIGN KEY(clienteId) REFERENCES usuarios(id) ON UPDATE NO ACTION ON DELETE NO ACTION, FOREIGN KEY(categoriaId) REFERENCES categorias(id) ON UPDATE NO ACTION ON DELETE NO ACTION)")
     db.execSQL("CREATE INDEX IF NOT EXISTS index_tickets_clienteId ON tickets(clienteId)")
     db.execSQL("CREATE INDEX IF NOT EXISTS index_tickets_categoriaId ON tickets(categoriaId)")
     db.execSQL("CREATE TABLE IF NOT EXISTS comentarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ticketId INTEGER NOT NULL, autorId INTEGER NOT NULL, autorNombre TEXT NOT NULL, autorRol TEXT NOT NULL, texto TEXT NOT NULL, fecha INTEGER NOT NULL, FOREIGN KEY(ticketId) REFERENCES tickets(id) ON UPDATE NO ACTION ON DELETE CASCADE)")
     db.execSQL("CREATE INDEX IF NOT EXISTS index_comentarios_ticketId ON comentarios(ticketId)")
    }
   }).build().also { instance = it }
  }
 }
}
