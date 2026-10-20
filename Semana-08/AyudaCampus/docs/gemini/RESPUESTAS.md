# Respuestas reales de Gemini en Android Studio

Fecha de la consulta: 20 de octubre de 2026. Modelo mostrado por Android Studio: **Gemini 3.6 Flash** (`gemini-3.6-flash`).

Los bloques Kotlin se transcribieron de la respuesta visible; Codex adaptó únicamente su indentación al integrarlos. Las capturas originales se conservan sin alterar su contenido. Se ajustaron las extensiones de archivo a JPEG, su formato real.

## Primera propuesta

Prompt real: [01-prompt-sla.txt](01-prompt-sla.txt).

```kotlin
@Test
fun fechasGrandesYCambioDePrioridadConservanElPlazoOriginal() {
    val fechas = listOf(Int.MAX_VALUE.toLong() - 1_000L, 1_800_000_000_000L)
    for (fecha in fechas) {
        val ahora = fecha + 5L * 3_600_000L
        val ticketAlta = ticket.copy(fecha = fecha, prioridad = "ALTA", estado = "EN_PROCESO")
        val ticketBaja = ticketAlta.copy(prioridad = "BAJA")
        val sla = calcularSla(ticketBaja, ahora)

        val plazoBajaMs = 72L * 3_600_000L
        val transcurridoMs = ahora - ticketBaja.fecha
        val restanteMs = plazoBajaMs - transcurridoMs
        val horasEsperadas = restanteMs / 3_600_000L

        assertEquals(SituacionSla.ACTIVO, sla.situacion)
        assertEquals("SLA: $horasEsperadas h restantes", sla.texto)
    }
}
```

**Explicación de Gemini, resumida:** la prueba amplía la cobertura a fechas que rebasan el rango de Int y comprueba el plazo al cambiar la prioridad, usando la función pura y sin acceder a Room, Repository, Composables o reloj real.

**Revisión de Codex:** el bloque solo calculaba el SLA después del cambio a BAJA; no comprobaba la situación ALTA anterior. También derivaba la expectativa con una fórmula de plazo y tiempo transcurrido semejante a la lógica bajo prueba. Codex pidió incluir la comprobación inicial y usar expectativas literales independientes. Esto es una mejora de la prueba; no se encontró ni se inventó un fallo de MVVM.

## Propuesta corregida e integrada

Prompt real: [02-prompt-correccion.txt](02-prompt-correccion.txt).

```kotlin
@Test
fun fechasGrandesYCambioDePrioridadConservanElPlazoOriginal() {
 val fechas = listOf(Int.MAX_VALUE.toLong() - 1_000L, 1_800_000_000_000L)
 for (fecha in fechas) {
  val ahora = fecha + 5L * 3_600_000L
  val ticketAlta = ticket.copy(fecha = fecha, prioridad = "ALTA", estado = "EN_PROCESO")

  val slaAlta = calcularSla(ticketAlta, ahora)
  assertEquals(SituacionSla.VENCIDO, slaAlta.situacion)
  assertEquals("SLA vencido hace 1 h", slaAlta.texto)

  val ticketBaja = ticketAlta.copy(prioridad = "BAJA")
  val slaBaja = calcularSla(ticketBaja, ahora)
  assertEquals(SituacionSla.ACTIVO, slaBaja.situacion)
  assertEquals("SLA: 67 h restantes", slaBaja.texto)
 }
}
```

**Explicación visible de Gemini:**

> Se incluyó la evaluación previa de ticketAlta a las 5 horas de creación, verificando de forma estricta el estado SituacionSla.VENCIDO y la expiración exacta de 1 hora antes del cambio. Además, se reemplazó el cálculo dinámico de expectativas por los literales de cadena exactos "SLA vencido hace 1 h" y "SLA: 67 h restantes", garantizando una aserción independiente de la lógica interna de calcularSla.

Codex comprobó las expectativas: con cinco horas transcurridas, ALTA (4 h) lleva una hora vencida y BAJA (72 h) conserva 67 horas. La fecha y el instante actual permanecen iguales al cambiar prioridad. El código se integró en `SlaTest.kt`; Gemini no editó archivos ni ejecutó las pruebas. Los resultados de ejecución de Codex se registran en [ACEPTACION.md](../ACEPTACION.md).

## Capturas originales

- [Consulta real y lectura de SlaTest.kt](01-consulta-real.jpg).
- [Propuesta inicial](02-propuesta-inicial.jpg).
- [Código y explicación corregidos](04-explicacion-corregida.jpg).

La última captura muestra las fechas, las aserciones y la explicación; la cabecera del método queda fuera del área visible. Su nombre consta en los prompts reales y en el código integrado.
