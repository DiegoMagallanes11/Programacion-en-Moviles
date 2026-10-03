package com.tecsup.mibodega.ui.cliente

/**
 * Objeto con las rutas de navegación de la app cliente.
 * Centralizar las rutas aquí evita repetir strings literales
 * y permite renombrar una pantalla tocando un solo lugar.
 */
object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"
    const val PEDIDOS = "pedidos"
    const val PERFIL = "perfil"

    /** Construye la ruta de detalle pasando el id del producto. */
    fun detalle(productoId: Int) = "detalle/$productoId"
}
