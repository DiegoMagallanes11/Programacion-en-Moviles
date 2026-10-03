package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Una línea del carrito: el producto y cuántas unidades se agregaron.
 */
data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int
)