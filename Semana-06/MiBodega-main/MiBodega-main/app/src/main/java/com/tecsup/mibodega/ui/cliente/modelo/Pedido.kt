package com.tecsup.mibodega.ui.cliente.modelo

data class Pedido(
    val id: String,
    val fecha: String,
    val items: List<ItemCarrito>,
    val total: Double
)
