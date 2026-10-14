package com.tecsup.ayudacampus.data.repository
import com.tecsup.ayudacampus.data.local.AyudaDatabase
import com.tecsup.ayudacampus.data.local.entity.Categoria
class AyudaRepositoryRoom(private val db: AyudaDatabase) : AyudaRepository {
 override fun observarCategorias() = db.categoriaDao().observarTodas()
 override fun observarCategoriasActivas() = db.categoriaDao().observarActivas()
 override suspend fun guardarCategoria(c: Categoria) { if (c.id == 0) db.categoriaDao().insertar(c) else db.categoriaDao().actualizar(c) }
 override suspend fun eliminarCategoria(c: Categoria) = db.categoriaDao().eliminar(c)
 override suspend fun cambiarActiva(id: Int, activa: Boolean) = db.categoriaDao().cambiarActiva(id, activa)
}
