package com.fernandez.lab06magallanes

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    var esFavorito: Boolean = false
)