package com.tecsup.ayudacampus.data.local.dao
import androidx.room.*
import com.tecsup.ayudacampus.data.local.entity.Usuario
@Dao interface UsuarioDao {
 @Query("UPDATE usuarios SET nombre = :nombre WHERE correo = 'juan@campus.test' COLLATE NOCASE AND nombre = 'Juan Pérez' AND rol = 'CLIENTE'")
 suspend fun personalizarNombreDePractica(nombre: String)
 @Query("UPDATE usuarios SET correo = 'magallanes@gmail.com' WHERE correo = 'juan@campus.test' COLLATE NOCASE AND rol = 'CLIENTE' AND NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'magallanes@gmail.com' COLLATE NOCASE)")
 suspend fun actualizarCorreoDePractica()
 @Query("SELECT * FROM usuarios WHERE correo = :correo COLLATE NOCASE AND clave = :clave LIMIT 1") suspend fun login(correo: String, clave: String): Usuario?
 @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE correo = :correo COLLATE NOCASE)")
 suspend fun existeCorreo(correo: String): Boolean
 @Insert suspend fun insertar(usuario: Usuario): Long
 @Insert suspend fun insertarTodos(lista: List<Usuario>)
 @Query("SELECT COUNT(*) FROM usuarios") suspend fun contar(): Int
 @Query("SELECT * FROM usuarios WHERE id = :id") suspend fun obtener(id: Int): Usuario?
 @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1") suspend fun obtenerPorRol(rol: String): Usuario?
}
