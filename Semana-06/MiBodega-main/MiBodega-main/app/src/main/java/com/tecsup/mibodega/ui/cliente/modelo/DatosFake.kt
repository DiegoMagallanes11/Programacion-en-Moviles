package com.tecsup.mibodega.ui.cliente.modelo

import com.tecsup.mibodega.R

/**
 * Datos de ejemplo (fake) para mostrar la UI sin base de datos.
 * Cuando conecten Room o una API, este archivo se reemplaza por
 * un Repository real, pero las pantallas no cambian porque ya
 * reciben una List<Producto> como parámetro.
 */
val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks")

val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz Costeño",
        descripcion = "Arroz extra, grano largo, ideal para el día a día.",
        precio = 4.50,
        categoria = "Abarrotes",
        // TODO: Puedes colocar una imagen personalizada en res/drawable/arroz_costeno.png y cambiar este ID por R.drawable.arroz_costeno
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal 1 L, alto en vitamina E.",
        precio = 8.90,
        categoria = "Abarrotes",
        // TODO: Puedes colocar una imagen personalizada en res/drawable/aceite_primor.png y cambiar este ID por R.drawable.aceite_primor
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada entera 1 L.",
        precio = 5.20,
        categoria = "Abarrotes",
        // TODO: Puedes colocar una imagen personalizada en res/drawable/leche_gloria.png y cambiar este ID por R.drawable.leche_gloria
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Galletas de chocolate rellenas 126 g.",
        precio = 3.50,
        categoria = "Snacks",
        // TODO: Puedes colocar una imagen personalizada en res/drawable/galleta_oreo.png y cambiar este ID por R.drawable.galleta_oreo
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 5,
        nombre = "Coca-Cola Original",
        descripcion = "Bebida gaseosa sabor cola. Ideal para compartir en familia.",
        precio = 6.50,
        categoria = "Bebidas",
        // TODO: Puedes colocar una imagen personalizada en res/drawable/coca_cola.png y cambiar este ID por R.drawable.coca_cola
        imagenRes = R.drawable.ilustracion_bodega
    )
)
