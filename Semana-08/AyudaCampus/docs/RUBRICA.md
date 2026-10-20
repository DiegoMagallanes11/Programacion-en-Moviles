# Revisión de la rúbrica del Laboratorio 08

Estudiante: Diego Magallanes. Revisión del 20 de octubre de 2026.

Esta revisión contrasta los criterios enviados por el estudiante con las evidencias del proyecto. No asigna una nota ni sustituye la evaluación docente.

| Criterio | Máximo | Evidencia y alcance |
| --- | --- | --- |
| Commits por cada avance | 4 | Catorce commits de Fase 1 en main, cuatro commits iniciales de Fase 2 y seguimiento posterior en la rama publicada, sin reescribir el historial. |
| Modelo de datos | 4 | Entidades Usuario, Categoria, Ticket y Comentario; relaciones, claves y DAOs Room. DatosYReglasTest verifica aislamiento, orden y reglas. |
| ViewModel | 4 | Los ViewModels dependen de AyudaRepository y exponen estado observable. Los cálculos y las reglas permanecen fuera de las pantallas. |
| Vista y ejercicio 1 | 4 | Pantallas Compose y recorrido completo de Estudiante y Soporte previamente verificado. La guía facilitada no contiene un apartado literalmente llamado Ejercicio 1; la equivalencia con este criterio debe confirmarla el docente. |
| Puntualidad e informe sin copias | 2 | Informe con capturas de la ejecución y atribución real de Codex y Gemini. La fecha límite y la puntualidad de la entrega no pueden acreditarse solo con el repositorio. |
| Observaciones y conclusiones | 2 | Análisis técnico a continuación y en el informe Word. El estudiante debe revisar y comprender el análisis antes de presentarlo. |

## Observaciones técnicas

- El promedio mide hasta el cierre confirmado por el estudiante. Si la confirmación se demora, aumenta el indicador aunque soporte ya haya resuelto el ticket. No debe interpretarse como tiempo exclusivo de trabajo de soporte.
- El SLA se calcula con la fecha original y la prioridad actual, sin persistir un contador. El modelo no conserva la hora histórica de resolución, por lo que no permite reconstruir si un ticket resuelto cumplió el plazo.
- Compilar no comprueba la navegación. La revisión añade cambios de pestaña con foco de edición, conservación del borrador y recorrido por ambos perfiles. La confirmación de envío se consume antes de volver a restaurar el formulario.
- Los bloqueos de System UI y ADB fuera de línea pertenecen al entorno del emulador. Deben distinguirse de fallos en las reglas de negocio y registrarse sin dar por aprobados los intentos fallidos.
- La cuenta de demostración se personaliza como Diego Magallanes. Se mantiene juan@campus.test como identificador de acceso para conservar los datos, la sesión y las credenciales documentadas. Las capturas anteriores conservan sus nombres históricos.

## Conclusiones

La separación MVVM permite incorporar indicadores y filtros manteniendo la View dedicada a representar el estado y enviar eventos. Room y Flow propagan los cambios del almacenamiento sin introducir consultas directas desde los Composables.

Las capturas muestran el resultado visible, mientras que las pruebas controladas verifican límites, exclusiones y conservación de datos. Ambas evidencias son necesarias: una pantalla correcta no demuestra por sí sola que se respeten todas las reglas.

El aporte de IA requiere revisión y atribución. Codex implementó las mejoras y Gemini aportó una prueba posterior de SLA; su propuesta se corrigió para evaluar expectativas independientes de la lógica implementada.

## Mejoras recomendadas

Priorizar accesibilidad, teclado, pantallas pequeñas y modo oscuro. Después, revisar los avisos de lint y el icono de la aplicación con cambios específicos. Un servidor, registro de usuarios o una fecha histórica de resolución requieren requisitos propios y quedan fuera del alcance de esta entrega local.

Ver [ACEPTACION.md](ACEPTACION.md) para los resultados reales y [PROMPTS.md](../PROMPTS.md) para la intervención de las herramientas.
