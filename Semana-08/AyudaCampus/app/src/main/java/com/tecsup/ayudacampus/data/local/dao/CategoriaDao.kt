package com.tecsup.ayudacampus.data.local.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.tecsup.ayudacampus.data.local.entity.Categoria
@Dao interface CategoriaDao {
 @Query("SELECT * FROM categorias ORDER BY nombre") fun observarTodas(): Flow<List<Categoria>>
 @Query("SELECT * FROM categorias WHERE activa = 1 ORDER BY nombre") fun observarActivas(): Flow<List<Categoria>>
 @Query("SELECT COUNT(*) FROM categorias") suspend fun contar(): Int
 @Insert suspend fun insertarTodas(lista: List<Categoria>)
 @Insert suspend fun insertar(c: Categoria): Long
 @Update suspend fun actualizar(c: Categoria)
 @Delete suspend fun eliminar(c: Categoria)
 @Query("UPDATE categorias SET activa = :activa WHERE id = :id") suspend fun cambiarActiva(id: Int, activa: Boolean)
}
