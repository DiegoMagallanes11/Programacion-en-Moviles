package com.tecsup.ayudacampus.data.local.dao
import androidx.room.*
import com.tecsup.ayudacampus.data.local.entity.Usuario
@Dao interface UsuarioDao {
 @Query("UPDATE usuarios SET nombre = :nombre WHERE correo = 'juan@campus.test' COLLATE NOCASE AND nombre = 'Juan Pérez' AND rol = 'CLIENTE'")
 suspend fun personalizarNombreDePractica(nombre: String)
 @Query("SELECT * FROM usuarios WHERE correo = :correo COLLATE NOCASE AND clave = :clave LIMIT 1") suspend fun login(correo: String, clave: String): Usuario?
 @Insert suspend fun insertarTodos(lista: List<Usuario>)
 @Query("SELECT COUNT(*) FROM usuarios") suspend fun contar(): Int
 @Query("SELECT * FROM usuarios WHERE id = :id") suspend fun obtener(id: Int): Usuario?
 @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1") suspend fun obtenerPorRol(rol: String): Usuario?
}
