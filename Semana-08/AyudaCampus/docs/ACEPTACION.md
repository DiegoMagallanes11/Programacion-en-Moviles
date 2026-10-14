# Verificación de la Fase 1

Entorno: emulador Medium Phone, Android 16 / API 36.1, ARM64. Compilación con SDK 35, Kotlin 2.0.21 y JDK 21 de Android Studio.

## Resultados

- APK debug: generado correctamente.
- Paquete de pruebas Android: generado correctamente.
- `DatosYReglasTest`: 3 pruebas correctas, con base Room en memoria independiente.
- `FlujoCompletoTest`: 1 prueba correcta; recorrido de Estudiante y Soporte, validaciones, atención, cierre, ajustes y cambio de perfil.
- `RestauracionTest`: 1 prueba correcta en una instrumentación nueva tras detener el proceso, sin borrar datos. Conserva sesión, 7 tickets y modo oscuro; después restablece el tema claro.
- Total: 5 pruebas aprobadas en ejecuciones selectivas.
- Seis capturas reales del emulador, revisadas visualmente.
- Lint: revisión adicional no completada porque quedó esperando una descarga. No se presenta como una comprobación aprobada.

## Matriz de aceptación

| Comportamiento | Evidencia |
| --- | --- |
| Login incorrecto no permite entrar | FlujoCompletoTest |
| Formulario vacío muestra errores por campo | FlujoCompletoTest |
| Ticket válido aparece en ambas listas | FlujoCompletoTest |
| Bandeja ordena ALTA, MEDIA, BAJA y fecha descendente | DatosYReglasTest |
| El estudiante solo consulta sus tickets | DatosYReglasTest, con una segunda cuenta ficticia |
| Soporte responde y el estudiante recibe el cambio por Flow | DatosYReglasTest y FlujoCompletoTest |
| Soporte no puede seleccionar CERRADO | DatosYReglasTest y FlujoCompletoTest |
| Cierre solo desde RESUELTO y con calificación 1–5 | DatosYReglasTest y FlujoCompletoTest |
| Reapertura vuelve a ABIERTO y borra calificación y fecha de cierre | DatosYReglasTest |
| Un ticket cerrado no admite comentarios | DatosYReglasTest y FlujoCompletoTest |
| Conteos por categoría y alerta de ALTA con más de una hora | DatosYReglasTest |
| Una categoría inactiva no se ofrece entre las activas | DatosYReglasTest |
| Categorías con tickets no pueden eliminarse | DatosYReglasTest |
| Modo oscuro y cambio de perfil | FlujoCompletoTest |
| Mesa cerrada bloquea crear tickets | FlujoCompletoTest |
| Tickets, perfil y modo oscuro persisten tras cerrar el proceso | RestauracionTest |

Las seis capturas del README se obtuvieron de la aplicación ejecutada, con datos ficticios. La preferencia de notificaciones se guarda localmente; esta fase no implementa entrega de notificaciones.
