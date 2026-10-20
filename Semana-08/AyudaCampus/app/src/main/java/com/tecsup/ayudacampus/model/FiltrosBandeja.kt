package com.tecsup.ayudacampus.model

import com.tecsup.ayudacampus.data.local.entity.Categoria
import com.tecsup.ayudacampus.data.local.relation.TicketConDetalle

data class FiltrosBandeja(
 val estado: String = "",
 val texto: String = "",
 val prioridad: Prioridad? = null,
 val categoriaId: Int? = null
)

data class EstadoBandeja(
 val filtros: FiltrosBandeja = FiltrosBandeja(),
 val categorias: List<Categoria> = emptyList(),
 val tarjetas: List<TarjetaTicket> = emptyList(),
 val total: Int = 0,
 val cargando: Boolean = true
)

fun filtrarBandeja(tickets: List<TicketConDetalle>, filtros: FiltrosBandeja): List<TicketConDetalle> {
 val texto = filtros.texto.trim()
 val numero = texto.removePrefix("#").toIntOrNull()
 // filter conserva exactamente el orden de prioridad/fecha/ID entregado por Room.
 return tickets.filter { detalle ->
  val t = detalle.ticket
  (filtros.estado.isEmpty() || t.estado == filtros.estado) &&
   (filtros.prioridad == null || t.prioridad == filtros.prioridad.name) &&
   (filtros.categoriaId == null || t.categoriaId == filtros.categoriaId) &&
   (texto.isEmpty() || t.titulo.contains(texto,ignoreCase=true) ||
    t.descripcion.contains(texto,ignoreCase=true) || t.id == numero)
 }
}
