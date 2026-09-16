package com.magallanes.lab04

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
) {
    val subtotalItem: Double get() = precio * cantidad
}