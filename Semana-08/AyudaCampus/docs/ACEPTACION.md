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


## Fase 2 — ejecución del 19 de octubre de 2026

Esta sección agrega resultados de una ejecución nueva. Los resultados y las seis capturas de Fase 1 anteriores se conservan como evidencia de aquella fase.

### Entorno y protección

- Windows; JDK 21.0.11; Gradle 8.13; SDK/target 35; minSdk 24. Dependencias originales sin actualizaciones.
- AVD independiente `AyudaCampusFase2`, Android 15/API 35, x86_64, puerto 5560. Configuración Pixel 7; resolución reducida a 720 × 1600 con densidad 280 para aliviar el uso de recursos y mantener un ancho lógico de unos 411 dp.
- Las pruebas Room usan una base en memoria independiente. El recorrido de UI usa únicamente datos ficticios del AVD dedicado. No se borró la base de datos del usuario ni la instalación de presentación.
- `main` permanece en `ac4e370c55a27f214239b5dd934d8be40458a77f`, con los catorce commits del proyecto de Fase 1. La antigua `mejora-ia` local y remota y los cinco stashes encontrados permanecen intactos. El stash con el nombre indicado en el prompt no estaba presente en esta copia del equipo; no se aplicó ni eliminó ningún stash.
- Tras `fetch`, `origin/mejora-ia` seguía en `4a580c8`, pero no era antecesora de `main`: tenía ocho commits exclusivos de otro laboratorio. Con autorización del usuario se creó `mejora-ia-ayudacampus` desde `main` en un worktree separado. No se forzó ningún historial ni se mezcló la Fase 2 en `main`.
- Autor y cuenta GitHub comprobados: DiegoMagallanes11, con el correo de autor solicitado. No se incluyeron archivos de otros laboratorios, credenciales, cachés, compilaciones ni configuración local del SDK.

### Resultados de compilación y pruebas

| Comprobación | Resultado real y alcance |
| --- | --- |
| `:app:assembleDebug` | Aprobada antes de cada commit funcional |
| `:app:assembleDebugAndroidTest` | Aprobada, incluida la adaptación de desplazamiento de las pruebas |
| `PromedioTest` | 2 aprobadas: ausencia frente a cero y formato de unidades |
| `SlaTest` | 4 aprobadas: prioridades y límites; estados/prioridad/reapertura; reloj y suscripciones; transición del texto a vencido |
| `BusquedaTest` | 3 aprobadas: filtros individuales; AND y orden; ViewModel reactivo, categoría eliminada, limpiar y lista vacía |
| `DatosYReglasTest` | 3 aprobadas de nuevo con el código de Fase 2; Room en memoria |
| `PromedioRoomTest` | 2 aprobadas: promedio/exclusiones y actualización por Flow con un observador ya suscrito |
| `FlujoCompletoTest` | 1 aprobada tras adaptar el desplazamiento a la nueva bandeja; recorrido completo y precondiciones originales |
| `RestauracionTest` | 1 aprobada en instrumentación nueva tras detener el proceso, sin borrar datos desde el recorrido exitoso; conserva sesión, siete tickets y tema |
| `BusquedaUiTest` | 1 aprobada: filtros AND, categoría inactiva, teclado abierto, regreso del detalle, actualización por cambio de estado, limpieza y número con/sin #; seis capturas reales |
| `:app:lintDebug` | Completada sin errores; 17 advertencias. Tres avisos corresponden a comprobaciones personalizadas obsoletas de Navigation que se omiten por incompatibilidad. Otros: target API, dependencias, extracción de datos e icono. No equivale a haber ejecutado los checks omitidos |

**Totales: 9 pruebas unitarias y 8 instrumentadas aprobadas.** Las nueve unitarias pasaron también juntas en la preparación final. No se presenta el intento inicial fallido ni una mera compilación de pruebas como si fueran pruebas aprobadas.

### Matriz de las tres mejoras

| Criterio | Evidencia y alcance |
| --- | --- |
| Sin cerrados válidos se conserva null y se presenta «Sin tickets cerrados» | `PromedioRoomTest.promedioExcluyeInvalidosYConservaCero` y `PromedioTest.ausenciaNoEsCero` |
| Un cerrado válido y promedio real cero | Misma prueba Room: fecha 1000, cierre 1000, promedio 0 |
| Varios cerrados con promedio conocido | Misma prueba Room: duraciones 0 y 2000 ms, promedio 1000 ms |
| Se excluyen ABIERTO, RESUELTO, cierre nulo y cierre anterior a creación | Misma prueba Room, antes de insertar cerrados válidos |
| El cierre actualiza un observador existente | `PromedioRoomTest.cierreActualizaObservadorExistente`: emisión null → 4000 ms, sin resuscribir |
| Tarjeta, significado y duración legible del promedio | `PromedioTest.unidadesComprensibles` y [captura del panel](capturas/fase-2/01-promedio-panel.png); se mantiene el panel anterior |
| SLA ALTA 4 h, MEDIA 24 h y BAJA 72 h | `SlaTest.prioridadesYLimitesExactos`, con fechas controladas y Long |
| Antes, exactamente y después del vencimiento | Misma prueba: límite −1 ms, límite exacto y límite +60000 ms |
| Cambio de prioridad desde fecha original | `SlaTest.prioridadActualEstadosYReaperturaNoReinicianFecha`: ALTA vencida a las 5 h, MEDIA activa |
| RESUELTO, CERRADO y reapertura sin reiniciar fecha | Misma prueba pura; revisión del DAO de reapertura, que no modifica fecha, y prueba previa de reglas de estados ejecutada de nuevo |
| Actualización del texto sin volver a abrir la pantalla | `SlaTest.textoPasaAVencidoSinReabrirPantalla`: avance virtual de 30000 ms, activo → vencido |
| Un reloj por pantalla y detención sin suscriptores | `SlaTest.relojActualizaCada30SegundosYSeDetieneSinSuscriptores`; revisión de stateIn/viewModelScope y collectAsStateWithLifecycle, sin corrutinas en las tarjetas |
| SLA en ambas listas y ambos detalles | Capturas [Mis tickets](capturas/fase-2/02-sla-mis-tickets.png), [detalle](capturas/fase-2/03-sla-detalle-estudiante.png), [atención](capturas/fase-2/04-sla-atencion-soporte.png) y [bandeja](capturas/fase-2/05-busqueda-combinada.png) |
| Estado, prioridad, categoría y texto por separado | `BusquedaTest.filtrosIndividualesTextoYNumero` |
| Todos los filtros combinados con AND | `BusquedaTest.filtrosSonAndSinAlterarOrden`; `BusquedaUiTest` y [captura combinada](capturas/fase-2/05-busqueda-combinada.png) |
| Título/descripción, mayúsculas y espacios exteriores | `BusquedaTest.filtrosIndividualesTextoYNumero`; búsqueda en descripción con « CONEXIÓN » |
| Número con y sin # y ceros iniciales | Misma prueba: 2, #2 y #0002; # aislado no coincide por un número vacío |
| Categorías inactivas con tickets | Fixture inactivo en `BusquedaTest` y en el recorrido de UI; la consulta usa todas las categorías. `DatosYReglasTest` confirma que creación sigue usando las activas |
| Eliminar la categoría seleccionada restablece Todas | `BusquedaTest.viewModelReaccionaADatosCategoriasYLimpiar`, con Flow controlado del Repository; no se elimina en Room una categoría con tickets |
| Orden por prioridad y fecha conservado | `BusquedaTest` conserva IDs 2,1 y luego 5,2,1 al recibir datos nuevos; `DatosYReglasTest` comprueba el orden del DAO real |
| Cambios de datos sin tocar filtros | `BusquedaTest` agrega un ticket y modifica su descripción, manteniendo los filtros activos |
| Limpiar los cuatro filtros y distinguir bandeja vacía de cero coincidencias | `BusquedaTest.viewModelReaccionaADatosCategoriasYLimpiar`: filtros por defecto, total 5 con cero coincidencias frente a total 0 |
| Filtros conservados al entrar y volver, teclado abierto y pantalla pequeña | `BusquedaUiTest` aprobada; conserva texto, prioridad y categoría al volver, usa controles con teclado abierto y reacciona a un cambio de estado sin tocar filtros; [sin coincidencias](capturas/fase-2/06-busqueda-sin-resultados.png) |
| Conservación de login, perfiles, conversación, cierre/calificación, mesa cerrada y preferencias | `DatosYReglasTest`, `FlujoCompletoTest` adaptado solo en el desplazamiento y `RestauracionTest`, ejecutados de nuevo |

### Incidencias reales y correcciones

1. El JBR predeterminado de Android Studio era Java 25. La primera compilación falló con `IllegalArgumentException: 25.0.2`. Se seleccionó JDK 21 ya instalado y las compilaciones pasaron, sin actualizar dependencias.
2. El primer arranque de instrumentación terminó antes de informar casos por un ANR de inicio del proceso en el emulador. Se liberaron los daemons de compilación y se preparó el código Android; después corrieron las pruebas. También se redujo la resolución del AVD dedicado tras un bloqueo de su interfaz del sistema.
3. En el primer recorrido de UI se aprobaron las tres pruebas de datos y las dos de promedio, pero el recorrido original falló al intentar encontrar una tarjeta no compuesta fuera de pantalla. Los nuevos filtros ocupan más espacio; se cambió una acción del test a `performScrollToNode`. Se restauraron solo los datos ficticios de este AVD para recuperar sus precondiciones, y el recorrido pasó. No se borró nada entre el recorrido exitoso y restauración.
4. La prueba nueva de búsqueda encontró dos nodos con el mismo título: el texto escrito y la tarjeta. Se usó el número para la búsqueda del estudiante. También se corrigió el desplazamiento explícito de tarjetas y campos tras cambios de teclado para no depender de qué elementos de LazyColumn estaban compuestos. Solo se repitieron las comprobaciones afectadas.

### Pendientes

Al cerrar la ejecución del 19 de octubre, la participación de **Gemini en Android Studio** estaba pendiente y la herramienta utilizada había sido Codex. El seguimiento del 20 de octubre se documenta a continuación, diferenciando la autoría de las mejoras de la contribución posterior a su verificación.

Lint completó su tarea con los avisos descritos; las comprobaciones personalizadas omitidas no se dan por aprobadas. Las entidades y el esquema Room permanecen sin cambios, y no se añadió una migración innecesaria.


## Seguimiento del 20 de octubre de 2026 — aporte real de Gemini

| Comprobación nueva | Resultado real |
| --- | --- |
| Uso de Gemini en Android Studio | Se enviaron dos prompts reales a Gemini 3.6 Flash desde el panel Agent de AyudaCampus, tras el inicio de sesión del usuario |
| Propuesta inicial | Generó una prueba con fechas grandes y cambio a BAJA, pero no evaluó ALTA antes del cambio; Codex pidió corregirla y usar expectativas literales |
| Propuesta corregida | `fechasGrandesYCambioDePrioridadConservanElPlazoOriginal`, incorporada a `SlaTest.kt`; sin cambios de lógica funcional o dependencias |
| Fecha cerca del límite Int | `Int.MAX_VALUE.toLong() - 1_000L`; la hora actual y el plazo cruzan el límite de 32 bits |
| Fecha Unix grande | `1_800_000_000_000L`; todos los instantes y duraciones son Long |
| Cambio de prioridad con fecha original | A las 5 h, ALTA: VENCIDO y «SLA vencido hace 1 h»; BAJA: ACTIVO y «SLA: 67 h restantes» |
| Pruebas ejecutadas por Codex | `:app:testDebugUnitTest --tests com.tecsup.ayudacampus.SlaTest`: **5 aprobadas, 0 fallos, 0 errores**, incluida la nueva prueba |
| Arquitectura | Prueba de función pura con hora controlada; sin DAO, Repository, Composables, reloj real ni esperas |
| Registro y evidencia | [Prompts 5 y 6](../PROMPTS.md), [respuestas transcritas y capturas originales](gemini/RESPUESTAS.md) |

El total acumulado es de **10 casos unitarios aprobados**: los 5 de SLA ejecutados el 20 de octubre y los 2 de promedio y 3 de búsqueda aprobados previamente. Los 8 casos instrumentados y lint conservan los resultados del 19 de octubre; no se ejecutaron de nuevo porque este aporte solo añade una prueba unitaria y documentación.

Los cuatro commits originales se conservan íntegros y publicados. El aporte de Gemini y su documentación se registran en un commit posterior, sobre la misma rama, sin mezclarla en `main`. La contribución real de Gemini se limita a la prueba de SLA: no se le atribuyen las tres implementaciones anteriores realizadas por Codex. Este registro acredita la participación observada y su alcance; la evaluación académica corresponde al docente.


## Revisión posterior del 20 de octubre de 2026 — nombre perfil y navegación

El usuario aclaró que el nombre a cambiar dentro de la app era Juan y pidió Diego Magallanes. Antes de editar se probaron Perfil → Nuevo → Tickets → Perfil en el emulador activo; los cuatro destinos respondieron y Perfil tenía contenido. No se afirma haber reproducido un bloqueo permanente ni se inventa su causa. Se reforzó la navegación para conservar pestañas, gestionar teclado/foco y consumir la confirmación de envío. Se amplió el resumen del perfil con los cuatro estados.

| Comprobación | Resultado real |
| --- | --- |
| assembleDebug y assembleDebugAndroidTest | Aprobadas; dependencias y esquema sin cambios |
| testDebugUnitTest | 10 aprobadas de nuevo: promedio 2, SLA 5, búsqueda 3; XML con 0 fallos y 0 errores |
| DatosYReglasTest | 4 aprobadas: tres existentes y una nueva de personalización de una instalación anterior, idempotencia, conservación de IDs, fecha, textos y otro usuario sin modificar |
| NavegacionPerfilTest | 1 aprobada: dos ciclos por las pestañas de Estudiante y Soporte, perfil con Diego, foco de edición, conservación y limpieza del borrador, regreso a Estudiante |
| Capturas nuevas | Dos capturas reales de la prueba, en docs/capturas/revision; las anteriores permanecen íntegras |
| Instalación existente | Actualización con install -r y copia local previa de datos y preferencias; no se desinstaló ni se borró la base |

Resultado de instrumentación: OK (5 tests), 18.385 segundos. Las pruebas Room usan memoria independiente; la de navegación no crea ni modifica tickets. El correo de acceso ficticio permanece juan@campus.test. Los saludos esperados de FlujoCompletoTest, RestauracionTest y BusquedaUiTest se adaptaron a Diego, pero esas tres clases no se volvieron a ejecutar en esta revisión; sus resultados anteriores siguen fechados y con su alcance original. Lint tampoco se repitió.

El total acumulado es de 10 casos unitarios y 10 casos instrumentados distintos aprobados; la ejecución actual fue de 10 unitarios y 5 instrumentados, no de toda la suite acumulada. El contraste de la rúbrica y las observaciones técnicas están en RUBRICA.md.


## Auditoría integral posterior — 20 de octubre de 2026

El usuario confirmó que la Fase 1 ya estaba terminada. Se mantuvo main y sus catorce commits sin cambios. Se revisó la guía completa y se ejecutó el flujo base como regresión de las tres mejoras en un AVD nuevo, AyudaCampusAuditoria, API 35 x86_64, puerto 5562. No se copió ni borró la base de presentación.

| Comprobación | Resultado real |
| --- | --- |
| assembleDebug y assembleDebugAndroidTest | Aprobadas con JDK 21 y dependencias originales |
| Suite unitaria final | 12 aprobadas: promedio 3, SLA 5, búsqueda 4; XML con 0 fallos y 0 errores |
| DatosYReglasTest y PromedioRoomTest con FlujoCompletoTest | 7 aprobadas en 89.699 s; recorrido con creación, respuesta, cierre con estrellas y regreso a Nuevo sin repetir el envío |
| RestauracionTest | 1 aprobada en 16.227 s, en instrumentación nueva tras detener el proceso, sin borrar datos desde el recorrido exitoso; conserva siete tickets, sesión y tema |
| NavegacionPerfilTest y BusquedaUiTest | 2 aprobadas en 67.983 s; pestañas de ambos perfiles, borrador, CRUD de categorías, teclado virtual habilitado, filtros AND, retorno, inactivas y cambios reactivos |
| Repetición focalizada tras corregir la carga del promedio | PromedioRoomTest 2 y BusquedaUiTest 1 aprobadas en 13.351 s; la captura espera al promedio emitido por Room |
| lintDebug final | Completada sin errores y con 17 advertencias; se conserva el alcance limitado de los tres checks de Navigation omitidos |
| Evidencias | Ocho capturas reales nuevas en docs/capturas/auditoria y registros en docs/resultados/auditoria/pruebas.txt |

El total final es de 12 casos unitarios y 10 instrumentados distintos aprobados. Las repeticiones focalizadas no se cuentan como casos nuevos. Las diez instrumentadas se ejecutaron por bloques para respetar las precondiciones de flujo completo y restauración. Los resultados previos se conservan con su alcance original.

### Hallazgos corregidos durante la auditoría

- La búsqueda del estudiante no admitía el número mostrado con ceros iniciales y # podía coincidir con todos. La normalización compartida corrige ambos casos; no amplía al estudiante la búsqueda por descripción exclusiva de soporte.
- El promedio tenía como valor inicial «Sin tickets cerrados» antes de leer Room. Ahora indica «Cargando promedio…»; una prueba de PanelViewModel comprueba carga, null, cero y un promedio posterior.
- La guía define los nombres del comentario como una foto histórica. Se retiró el renombrado de autores antiguos. Dos nombres afectados por la primera personalización se recuperaron del respaldo con una comprobación selectiva de IDs, texto, rol y fecha; no se revirtió la base entera. El nombre actual del perfil sigue siendo Diego Magallanes.
- La prueba de restauración se adaptó al resumen ampliado del perfil. El recorrido base comprueba que Nuevo vuelva a ser utilizable después de crear un ticket. Navegación comprueba crear, editar, desactivar y eliminar una categoría sin tickets.

### Incidencias de las comprobaciones

Una ampliación del test de recorrido necesitó importar Assert.assertEquals para compilar. La primera prueba nueva del promedio verificó sus valores, pero falló al limpiar las corrutinas después de restaurar Main demasiado pronto; se esperó su cancelación antes de resetMain y la suite final de doce pasó. Una captura inicial tomó el valor transitorio antes de la emisión de Room: se reemplazó por una captura real tomada después de esperar el valor calculado. No se presentan esos intentos como aprobados.

Ver [AUDITORIA.md](AUDITORIA.md) para el contraste completo con la guía y [RUBRICA.md](RUBRICA.md) para los criterios de evaluación. La contribución de Gemini conserva su alcance real documentado; no se le atribuye esta auditoría de Codex.


### Revisión final de la evidencia visual

Una captura de cero coincidencias se había tomado antes de terminar el dibujo nativo, aunque Compose ya exponía el mensaje correcto y la prueba había pasado. Se sincronizó la captura con la inactividad de la interfaz y se dejó un margen de 250 ms para completar el dibujo; BusquedaUiTest pasó de nuevo en 37.225 s. Se sustituyó la imagen de cero resultados por la captura real verificada visualmente. Para el resultado combinado se conserva la captura válida de la ejecución anterior, con formulario, conteo y tarjeta visibles. La nueva captura adicional 07-filtros-con-teclado.png muestra los filtros y la barra por encima del teclado. No se reconstruyeron ni editaron las imágenes. Esta repetición no aumenta los diez casos instrumentados distintos.

### Controles de Perfil y ajustes 20 de octubre de 2026

ControlesPerfilTest aprobó un caso nuevo en 28.913 s en el emulador abierto API 35. Verificó el cambio desde el texto de los interruptores, conservación de modo oscuro y Notificaciones al recrear la Activity, mesa cerrada con Enviar ticket deshabilitado y habilitación al abrirla, cambio de ambos roles, regreso desde el detalle con teclado abierto, cierre de sesión e ingreso de nuevo. No creó ni editó tickets o comentarios; restableció las preferencias usadas por la prueba. Se mantuvieron los seis tickets de presentación.

La compilación debug y del APK de pruebas pasó en 2 min 43 s. lintDebug volvió a terminar correctamente en 2 min 10 s; mantiene 0 errores y 17 advertencias, con los checks omitidos ya documentados. Este caso es adicional a los diez instrumentados anteriores; no se volvió a ejecutar la suite completa.

### Barra inferior desde pantallas secundarias 21 de octubre de 2026

Se reprodujo un fallo que las pruebas anteriores no cubrían: desde el detalle del ticket 3, Nuevo abría el formulario, pero Tickets restauraba el detalle en vez de la lista. No era un botón deshabilitado; era una restauración incorrecta de la pila.

Se corrigió la navegación a las raíces de las pestañas y su selección visual. BarraInferiorTest aprobó un caso en 19.435 s: repite detalle → Nuevo → Tickets, detalle → Perfil → Tickets, selección de Tickets desde el propio detalle, borrador de Nuevo con teclado, atención → Panel/Ajustes/Tickets y formulario de categoría → Categorías/Panel. No crea ni modifica datos. Debug y el APK de pruebas compilaron en 1 min 30 s antes de la ejecución. Los resultados previos no se presentan como repetidos.

Notificaciones conserva una preferencia, como pide el contrato DataStore; no implementa avisos del sistema. Se aclaró en ambos perfiles. La flecha Volver ahora limpia foco y oculta teclado. Evidencia real: [Perfil oscuro](capturas/controles/01-perfil-oscuro.png) y [Perfil claro](capturas/controles/02-perfil-claro.png). La instalación quedó en Perfil del estudiante y modo claro.
