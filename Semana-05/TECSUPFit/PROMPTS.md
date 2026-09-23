# Registro de Prompts - TECSUPFit (Fase 2)

## Prompt 1: Cancelación de Reservas
**Objetivo:** Agregar la función de cancelación de reservas en la pantalla de Mis Reservas.

**Consulta enviada:**
> Necesito agregar la funcionalidad de cancelar reservas en `PantallaReservas.kt`. Cada tarjeta debe tener un botón para cancelar que elimine la reserva de la lista mutable `reservas` en `Navegacion.kt`, actualizando la UI de forma reactiva sin arquitectura MVVM.

**Respuesta aplicada:**
Se implementó el parámetro `onCancelarReserva` en `PantallaReservas`, un botón `OutlinedButton` de cancelación en `TarjetaReserva`, y la lógica de remoción mediante `reservas.remove(reserva)` en `Navegacion.kt`.