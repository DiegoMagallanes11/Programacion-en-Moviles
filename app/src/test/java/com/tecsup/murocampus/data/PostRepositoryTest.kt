package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.PostApi
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class PostRepositoryTest {
    private val api = ApiDePrueba()
    private val repo = PostRepositoryRemote(api)

    @Test
    fun listaYFiltroUsanLaOperacionCorrespondiente() = runBlocking {
        assertEquals(100, repo.listar().getOrThrow().size)
        val filtrados = repo.porUsuario(2).getOrThrow()
        assertEquals(10, filtrados.size)
        assertTrue(filtrados.all { it.userId == 2 })
    }

    @Test
    fun pideElDetalleYSusComentarios() = runBlocking {
        assertEquals(1, repo.detalle(1).getOrThrow().id)
        val comentarios = repo.comentarios(1).getOrThrow()
        assertEquals(5, comentarios.size)
        assertTrue(comentarios.all { it.postId == 1 })
    }

    @Test
    fun convierte404EnUnErrorDeServidor() = runBlocking {
        val error = repo.detalle(9999).exceptionOrNull()
        assertEquals("Error del servidor (404)", error?.message)
    }

    @Test
    fun recuperaLaListaDespuesDeUnErrorDeConexion() = runBlocking {
        api.falloLista = IOException("offline")
        assertTrue(repo.listar().exceptionOrNull()?.message.orEmpty().startsWith("Sin conexión a internet"))
        api.falloLista = null
        assertEquals(100, repo.listar().getOrThrow().size)
    }

    @Test
    fun convierteUnErrorDeSerializacionEnUnMensajeDeUI() = runBlocking {
        api.falloLista = SerializationException("faltan campos")
        assertEquals("Respuesta inesperada", repo.listar().exceptionOrNull()?.message)
    }

    @Test(expected = CancellationException::class)
    fun conservaLaCancelacion() = runBlocking<Unit> {
        api.falloLista = CancellationException("detalle cerrado")
        repo.listar()
    }

    @Test
    fun elPostEnviaDatosSinIdYRecibeElIdSimulado() = runBlocking {
        val nuevo = NuevoPost(1, "Prueba", "Contenido")
        assertFalse(Json.encodeToString(NuevoPost.serializer(), nuevo).contains("\"id\""))
        assertEquals(Post(1, 101, "Prueba", "Contenido"), repo.crear(nuevo).getOrThrow())
    }

    @Test
    fun toleraCamposAdicionalesDelServidor() {
        val json = Json { ignoreUnknownKeys = true }
        val post = json.decodeFromString(
            Post.serializer(),
            """{"id":1,"userId":1,"title":"Título","body":"Contenido","extra":true}"""
        )
        assertEquals(Post(1, 1, "Título", "Contenido"), post)
    }

    @Test
    fun eliminaSoloSiLaRespuestaEsExitosa() = runBlocking {
        assertTrue(repo.eliminar(1).isSuccess)
        assertEquals("Error del servidor (404)", repo.eliminar(9999).exceptionOrNull()?.message)
    }

    private class ApiDePrueba : PostApi {
        var falloLista: Exception? = null
        private val posts = (1..100).map { id ->
            Post((id - 1) / 10 + 1, id, "Post $id", "Contenido")
        }

        override suspend fun listar(): List<Post> {
            falloLista?.let { throw it }
            return posts
        }

        override suspend fun detalle(id: Int): Post =
            posts.firstOrNull { it.id == id }
                ?: throw HttpException(Response.error<Post>(404, "{}".toResponseBody()))

        override suspend fun comentarios(id: Int) =
            (1..5).map { Comentario(id, it, "Nombre", "demo@example.com", "Comentario") }

        override suspend fun porUsuario(userId: Int) = posts.filter { it.userId == userId }

        override suspend fun crear(nuevo: NuevoPost) =
            Post(nuevo.userId, 101, nuevo.title, nuevo.body)

        override suspend fun eliminar(id: Int): Response<Unit> =
            if (id == 1) Response.success(Unit) else Response.error(404, "{}".toResponseBody())
    }
}
