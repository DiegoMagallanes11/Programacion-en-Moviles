package com.tecsup.ayudacampus.model

import com.tecsup.ayudacampus.data.local.entity.Ticket
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle

enum class SituacionSla { ACTIVO, VENCIDO, FINALIZADO }
data class Sla(val texto: String, val situacion: SituacionSla)
data class TarjetaTicket(val detalle: TicketConDetalle, val sla: Sla)

fun calcularSla(ticket: Ticket, ahora: Long): Sla {
 if (ticket.estado == "RESUELTO") return Sla("SLA: ticket resuelto", SituacionSla.FINALIZADO)
 if (ticket.estado == "CERRADO") return Sla("SLA: ticket cerrado", SituacionSla.FINALIZADO)
 val horas = when (Prioridad.valueOf(ticket.prioridad)) {
  Prioridad.ALTA -> 4L
  Prioridad.MEDIA -> 24L
  Prioridad.BAJA -> 72L
 }
 val restante = ticket.fecha + horas * 3_600_000L - ahora
 return if (restante > 0L) Sla("SLA: ${duracionLegible(restante).replaceFirstChar { it.lowercase() }} restantes", SituacionSla.ACTIVO)
 else Sla("SLA vencido hace ${duracionLegible(-restante).replaceFirstChar { it.lowercase() }}", SituacionSla.VENCIDO)
}
