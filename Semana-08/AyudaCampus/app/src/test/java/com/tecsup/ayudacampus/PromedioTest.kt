package com.tecsup.ayudacampus

import com.tecsup.ayudacampus.model.*
import org.junit.Assert.*
import org.junit.Test

class PromedioTest {
 @Test fun ausenciaNoEsCero() {
  assertEquals("Sin tickets cerrados", promedioLegible(null))
  assertEquals("Menos de 1 min", promedioLegible(0.0))
 }
 @Test fun unidadesComprensibles() {
  assertEquals("Menos de 1 min", duracionLegible(59_999L))
  assertEquals("1 min", duracionLegible(60_000L))
  assertEquals("2 h 15 min", duracionLegible(8_100_000L))
  assertEquals("72 h", duracionLegible(259_200_000L))
 }
}
