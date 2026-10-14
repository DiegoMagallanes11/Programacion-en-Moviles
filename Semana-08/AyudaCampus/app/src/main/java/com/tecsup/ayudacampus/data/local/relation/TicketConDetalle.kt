package com.tecsup.ayudacampus.data.local.relation
import androidx.room.*
import com.tecsup.ayudacampus.data.local.entity.*
data class TicketConDetalle(
 @Embedded val ticket: Ticket,
 @Relation(parentColumn="id",entityColumn="ticketId") val comentarios: List<Comentario>,
 @Relation(parentColumn="categoriaId",entityColumn="id") val categoria: Categoria,
 @Relation(parentColumn="clienteId",entityColumn="id") val cliente: Usuario
)
