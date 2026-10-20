package com.tecsup.ayudacampus.model

fun duracionLegible(milisegundos: Long): String {
 val minutos = milisegundos.coerceAtLeast(0L) / 60_000L
 return when {
  minutos == 0L -> "Menos de 1 min"
  minutos < 60L -> "$minutos min"
  minutos % 60L == 0L -> "${minutos / 60L} h"
  else -> "${minutos / 60L} h ${minutos % 60L} min"
 }
}

fun promedioLegible(promedio: Double?): String =
 promedio?.let { duracionLegible(it.toLong()) } ?: "Sin tickets cerrados"
