package com.tecsup.ayudacampus.model

import com.tecsup.ayudacampus.data.local.entity.Ticket

fun coincideBusquedaTicket(ticket: Ticket, consulta: String, incluirDescripcion: Boolean = false): Boolean {
 val texto=consulta.trim()
 if(texto.isEmpty()) return true
 val numero=texto.removePrefix("#").toIntOrNull()
 return ticket.titulo.contains(texto,ignoreCase=true) ||
  (incluirDescripcion && ticket.descripcion.contains(texto,ignoreCase=true)) ||
  ticket.id==numero
}
