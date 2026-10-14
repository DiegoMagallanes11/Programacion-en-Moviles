package com.tecsup.ayudacampus.data.local
import android.content.Context
import androidx.room.*
import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.local.dao.CategoriaDao
@Database(entities = [Categoria::class], version = 1, exportSchema = false)
abstract class AyudaDatabase : RoomDatabase() {
 abstract fun categoriaDao(): CategoriaDao
 companion object {
  @Volatile private var instance: AyudaDatabase? = null
  fun obtener(context: Context): AyudaDatabase = instance ?: synchronized(this) {
   instance ?: Room.databaseBuilder(context.applicationContext, AyudaDatabase::class.java, "ayudacampus.db").build().also { instance = it }
  }
 }
}
