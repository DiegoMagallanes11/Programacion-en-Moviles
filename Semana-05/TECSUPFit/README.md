# TECSUPFit - Sustentación

### 1. ¿Cómo llega la clase seleccionada hasta Confirmación?
El objeto `ClaseFit` viaja por `NavController` de `Inicio` a `Detalle`, y luego a `Agendar`. Al confirmar el horario, el ítem se guarda en la lista `reservas` (en `Navegacion.kt`) para que `Confirmacion` muestre el resumen.

### 2. ¿Cómo sabe la bottomBar qué ícono resaltar?
Consulta la ruta activa con `navController.currentBackStackEntryAsState()`. En la barra se compara si `currentRoute == item.ruta`; si coinciden, Compose aplica el resaltado.

### 3. ¿Por qué la selección de horario actúa como RadioButton si son chips?
Por exclusión mutua: solo se elige un horario a la vez. Aunque sean chips, responden a una sola variable de estado (`horarioSeleccionado`). Al marcar uno, la variable cambia y desmarca el resto.

### 4. ¿Qué corregiste del código de la IA en la Fase 2?
La IA sugería un ViewModel innecesario. Lo simplifiqué pasando un callback (`onCancelarReserva`) a `Navegacion.kt` para usar `reservas.remove(reserva)`. Además, agregué la navegación al Inicio cuando la lista queda vacía.