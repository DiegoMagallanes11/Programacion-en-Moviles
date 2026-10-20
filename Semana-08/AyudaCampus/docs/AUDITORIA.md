# Auditoría funcional del Laboratorio 08

Revisión del 20 de octubre de 2026. Estudiante: Diego Magallanes.

La Fase 1 se considera terminada por indicación del estudiante. Sus catorce commits y `main` se conservan en `ac4e370`. La auditoría se realiza en `mejora-ia-ayudacampus`: verifica las tres mejoras de Fase 2 y comprueba que no rompan el recorrido base. Se leyó la guía completa de 26 páginas; sus requisitos se distinguen de las instrucciones del estudiante.

## Hallazgos y correcciones

1. **Nombre del perfil.** La cuenta de práctica mostraba Juan Pérez. Se personaliza a Diego Magallanes conservando ID, correo de acceso, tickets, sesión y preferencias. Una prueba simula una instalación antigua y comprueba la idempotencia y la conservación de sus datos y de otro usuario.
2. **Historial de autores.** La guía, página 4, define `autorNombre` y `autorRol` como una foto histórica. Se retiró la actualización que renombraba autores antiguos. La primera personalización había afectado dos nombres en la instalación de presentación; sus valores originales se recuperaron del respaldo de manera selectiva, comprobando antes los IDs, autores, textos y fechas. La comprobación de mantenimiento aprobó en 0.132 s y no revirtió mensajes, fechas ni tickets; la herramienta temporal se retiró del código entregado. El código entregado no renombra comentarios históricos.
3. **Navegación y formulario.** Antes de editar, Perfil → Nuevo → Tickets → Perfil funcionó y el perfil tenía contenido: no se inventa una causa ni un bloqueo permanente no reproducido. Se reforzó la conservación de pestañas, el foco y el teclado. Al confirmar un envío se limpia el formulario y se consume el evento para que restaurar Nuevo no repita la redirección.
4. **Resumen del perfil.** Se muestran Abiertos, En proceso, Resueltos, Cerrados y el total; se conservan preferencias, cambio de perfil y cierre de sesión.
5. **Búsqueda del estudiante.** `#0002` no coincidía con el número visible y `#` podía aceptar todos por comparar una cadena numérica vacía. Una función de dominio comparte la normalización del número con la búsqueda de soporte. Solo soporte busca también en descripción. La prueba cubre mayúsculas, espacios, número con/sin #, ceros iniciales y la exclusión del número vacío.
6. **Carga del promedio.** El valor inicial podía mostrar «Sin tickets cerrados» antes de recibir datos de Room. Ahora muestra «Cargando promedio…». Una prueba del PanelViewModel distingue carga, ausencia, cero real y un promedio posterior. La captura del panel espera al resultado real de Room.

## Contraste funcional con la guía

| Requisito | Guía | Evidencia |
| --- | --- | --- |
| Cuatro entidades, relaciones, índices y datos iniciales | 4 y 10–12 | Revisión de entidades, DAOs y singleton; DatosYReglasTest |
| Repository como interface y escrituras suspend con lecturas Flow | 3 y 13 | Revisión del contrato y su implementación; compilación y pruebas Room |
| Sesión, modo oscuro y preferencias en DataStore | 13–14 y 22–24 | FlujoCompletoTest y RestauracionTest, en instrumentaciones separadas |
| Login incorrecto no permite entrar | 14–15 y 23 | FlujoCompletoTest; validaciones de campos vacíos y rol revisadas en SesionViewModel |
| Solo tickets del estudiante y filtros por estado y búsqueda | 15–16 | DatosYReglasTest, BusquedaTest y búsqueda de número en el recorrido UI |
| Crear con validación por campo, categoría activa y prioridad | 16–17 | FlujoCompletoTest y DatosYReglasTest |
| Conversación reactiva y alineación según autor | 7 y 18–19 | Observador existente de TicketDetalleViewModel en DatosYReglasTest; recorrido de respuesta; revisión de BurbujaComentario |
| Cierre solo desde RESUELTO con 1–5 estrellas | 5 y 18–19 | DatosYReglasTest y FlujoCompletoTest |
| Reapertura y prohibición de comentar tickets cerrados | 5 y 23–24 | DatosYReglasTest; cierre y ocultación del campo en FlujoCompletoTest |
| Orden ALTA → MEDIA → BAJA y fecha descendente | 5 y 20 | DatosYReglasTest y BusquedaTest |
| Conteos, gráficos y alerta ALTA abierta más de una hora | 19–20 y 24 | Consultas Room comprobadas, DatosYReglasTest y capturas reales del panel |
| Atención sin permitir cerrar desde Soporte | 21 | DatosYReglasTest y FlujoCompletoTest |
| Categorías con validación, icono, edición y activación | 21–22 | CRUD desde NavegacionPerfilTest; validaciones y categorías inactivas en DatosYReglasTest |
| No eliminar categorías con tickets | 21 | DatosYReglasTest y restricción de clave foránea de Room |
| Pestañas de ambos perfiles y borrador al cambiar | 7 y 22 | NavegacionPerfilTest; teclado virtual habilitado en el AVD de auditoría |
| Mesa cerrada impide crear | 22 | FlujoCompletoTest y validación de NuevoTicketViewModel |
| Restauración sin borrar datos | 23–24 | RestauracionTest después del recorrido exitoso y en un proceso nuevo |
| Promedio de resolución y actualización por Flow | 25 | PromedioTest y PromedioRoomTest, con null, cero, exclusiones y observador existente |
| SLA de 4, 24 y 72 h y actualización temporal | 25 | Cinco casos SlaTest: límites exactos, estados, cambio de prioridad, reapertura, reloj y fechas grandes |
| SLA visible en las dos listas y los dos detalles | 25 | BusquedaUiTest y capturas de Fase 2 |
| Búsqueda de soporte por filtros AND y datos reactivos | 25 | BusquedaTest y BusquedaUiTest: inactivas, limpieza, regreso, número y cero coincidencias |

## Resultados de la ejecución

Se utilizó un AVD nuevo e independiente, `AyudaCampusAuditoria`, Android 15/API 35 x86_64, puerto 5562, con pantalla 720 × 1600 y teclado virtual habilitado. El Pixel 7 de presentación se detuvo temporalmente; no se borró su instalación. El AVD de auditoría se inició sin copiar la base de datos de presentación.

- Once casos unitarios aprobaron antes de añadir la prueba del estado de carga. La suite final aprobó doce: promedio 3, SLA 5 y búsqueda 4, con 0 fallos y 0 errores.
- Los diez casos instrumentados distintos se ejecutaron en bloques: DatosYReglasTest 4, PromedioRoomTest 2 y FlujoCompletoTest 1; después RestauracionTest 1 en una instrumentación nueva, sin borrar datos; finalmente NavegacionPerfilTest 1 y BusquedaUiTest 1.
- El bloque base y promedio aprobó 7 casos en 89.699 s; restauración aprobó 1 en 16.227 s; navegación y búsqueda aprobaron 2 en 67.983 s.
- Tras ajustar la carga del indicador, se repitieron únicamente las pruebas Room de promedio y el recorrido UI de búsqueda, además de la suite unitaria y lint. El bloque focalizado aprobó 3 casos en 13.351 s. Los resultados finales se detallan en [ACEPTACION.md](ACEPTACION.md).
- Lint terminó sin errores y con 17 advertencias: 11 de dependencias, 3 de comprobaciones personalizadas obsoletas de Navigation, 1 de API objetivo, 1 de extracción de datos y 1 de icono. Los tres checks omitidos no se presentan como ejecutados.

Los resultados anteriores no se borran ni se atribuyen a esta ejecución. Las nuevas imágenes están en [capturas/auditoria](capturas/auditoria/). Las capturas históricas de ambas fases permanecen intactas.

## Alcance y observaciones pendientes

No se detectó una funcionalidad obligatoria ausente entre los contratos de la guía y las tres mejoras elegidas. El registro de usuarios, los comentarios internos y las animaciones avanzadas eran alternativas de Fase 2; no se suman al alcance seleccionado. Los enlaces decorativos de registro o recuperación de contraseña del mockup no sustituyen los contratos escritos ni se añaden como funciones falsas.

El aporte real de Gemini se limita a la prueba posterior de SLA documentada en PROMPTS.md; las tres implementaciones se atribuyen a Codex. La auditoría funcional no acredita por sí sola la autoría de una fase ni garantiza una calificación. La Fase 1 se conserva terminada, conforme a la aclaración del estudiante.

La rama conserva el nombre alternativo autorizado porque la antigua mejora-ia divergía con trabajo de otro laboratorio. No se fuerza el historial ni se mezcla en main. Los avisos de lint y la presión de memoria del equipo siguen documentados; revisar el icono y la accesibilidad visual son mejoras de presentación posteriores, no funciones nuevas obligatorias.

Los [registros de pruebas](resultados/auditoria/pruebas.txt) conservan las salidas reales de los bloques y el resumen de los XML unitarios. La herramienta local de restauración de autores no se suma a los diez casos funcionales ni forma parte de la app entregada.

Correcciones funcionales registradas en [4d2ea10](https://github.com/DiegoMagallanes11/Programacion-en-Moviles/commit/4d2ea10a1237bce20005d896f2f55a5daadbd0aa), sin modificar main.

La revisión visual final sustituyó la captura transitoria de cero coincidencias y agregó [los filtros con teclado abierto](capturas/auditoria/fase-2/07-filtros-con-teclado.png). BusquedaUiTest volvió a aprobar en 37.225 s tras sincronizar la captura; se conserva la fotografía válida anterior del resultado combinado. Las imágenes mantienen su contenido original.
