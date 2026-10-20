# Prompts utilizados — Fase 2

## Herramienta y alcance reales

La implementación inicial fue realizada por **OpenAI Codex**, mediante esta conversación, edición local, Git, Gradle y Android SDK. El **20 de octubre de 2026** se consultó realmente a **Gemini 3.6 Flash en Android Studio**, que generó y corrigió una prueba adicional del SLA. No se incorporó una API de IA a la aplicación. Las instrucciones de la guía se consultaron en sus páginas 25–26; no se ejecutaron sus ejemplos como órdenes independientes de la solicitud del usuario.

Inicialmente el usuario aceptó continuar con Codex y dejar la contribución de Gemini pendiente. Después solicitó realizarla, inició sesión en Google y autorizó continuar. Se distingue la implementación de las tres mejoras por Codex de la contribución posterior de Gemini a su verificación; no se atribuyen a Gemini cambios anteriores que no realizó.

## Prompt 1: solicitud inicial

**Texto enviado:** solicitud real del usuario a Codex. Las rutas macOS y las suposiciones de Git se verificaron y corrigieron con el contexto posterior.

<details>
<summary>Solicitud inicial íntegra</summary>

```text
Actúa como desarrollador Android y completa la Fase 2 del laboratorio AyudaCampus. Implementa los cambios, verifica su funcionamiento, documenta las evidencias, crea los commits y publica la rama. No te limites a proponer código o explicar cómo hacerlo.

1. PROYECTO Y ESTADO INICIAL

Repositorio:
https://github.com/DiegoMagallanes11/Programacion-en-Moviles

Ruta local del repositorio:
/Users/tecsup/Desktop/Programacion-en-Moviles

Proyecto Android:
/Users/tecsup/Desktop/Programacion-en-Moviles/Semana-08/AyudaCampus

Guía del laboratorio:
/Users/tecsup/Downloads/GLAB-S08-JLEONS-2026_B.pdf

Lee las páginas 25–26 y el README del proyecto antes de modificar archivos.

La Fase 1 está terminada y publicada en main mediante exactamente 14 commits del proyecto. El último commit verificado es:
ac4e370c55a27f214239b5dd934d8be40458a77f
docs: agrega README con estructura y capturas

Comprueba el estado actual por si hubo cambios posteriores. Conserva íntegros los 14 commits de la Fase 1.

Tecnologías existentes:
- Kotlin y Jetpack Compose.
- Material 3.
- MVVM + Repository mediante interface.
- Room 2.6.1.
- DataStore.
- Navigation Compose.
- compileSdk y targetSdk 35; minSdk 24.
- Paquete: com.tecsup.ayudacampus.

No actualices dependencias ni cambies la arquitectura salvo que sea indispensable para las tres mejoras solicitadas.

Cuentas ficticias:
Estudiante: juan@campus.test / 1234
Soporte: soporte@campus.test / admin

Ambos perfiles usan la misma base Room local. No agregues un servidor, servicios externos ni una API de inteligencia artificial dentro de la app.

La guía pide utilizar Gemini en Android Studio como asistente de desarrollo. Si eres Gemini, registra los prompts reales utilizados. Si eres otra IA, declara la herramienta real y solicita la participación de Gemini para cumplir ese requisito; nunca atribuyas a Gemini trabajo que no realizó.

2. PROTECCIÓN DEL TRABAJO Y PREPARACIÓN DE GIT

Ejecuta los comandos Git desde:
/Users/tecsup/Desktop/Programacion-en-Moviles/Semana-08/AyudaCampus

Antes de editar:
- Lee las instrucciones AGENTS.md aplicables, si existen.
- Inspecciona git status, ramas, remotos, historial y stash.
- Confirma que origin corresponde al repositorio indicado.
- Usa exclusivamente la cuenta GitHub DiegoMagallanes11.
- Identidad de autor esperada:
  DiegoMagallanes11 <magallaneslda@gmail.com>
- Si detectas otra identidad, corrige la configuración local del repositorio antes de crear commits.
- No muestres tokens, contraseñas ni credenciales.

Existen cambios ajenos a AyudaCampus en otras carpetas del repositorio, incluidos movimientos de Semana-02 y archivos del IDE. No los incluyas en tus commits, no los descartes ni los modifiques.

Existe un stash de respaldo llamado:
Respaldo de mejora-ia antes de organizar desde main
No lo apliques ni lo elimines.

IMPORTANTE: ya existe una rama local y remota antigua llamada mejora-ia. Al preparar este prompt, origin/mejora-ia apuntaba a:
4a580c8
Agregar PROMPTS.md con la documentacion de la Fase 2

Ese commit era antecesor del main actual. Por tanto:
1. Actualiza las referencias remotas.
2. Verifica otra vez que origin/mejora-ia sea antecesor de main.
3. Si se mantiene esa condición, conserva la rama local antigua renombrándola a un nombre de respaldo único, por ejemplo respaldo/mejora-ia-anterior-YYYYMMDD-HHMMSS.
4. Crea una nueva rama local llamada mejora-ia desde el main actualizado.
5. Al terminar, el push normal de esta nueva rama puede avanzar la rama remota existente sin reescribir su historial.

No uses reset --hard, git clean, push --force, force-with-lease ni elimines ramas remotas. Si la rama remota cambió y ya no es antecesora de main, explica la divergencia antes de tomar una decisión que altere trabajo ajeno.

No crees commits de Fase 2 en main. No mezcles mejora-ia en main al terminar.

3. ALCANCE OBLIGATORIO: IMPLEMENTA ESTAS TRES MEJORAS

Implementa exactamente estas tres mejoras funcionales, además de su documentación y verificación:

A. Tiempo promedio de resolución en el panel.
B. Contador de SLA por prioridad.
C. Búsqueda avanzada en la bandeja de soporte.

No agregues registro de usuarios, comentarios internos ni otras funcionalidades fuera de este alcance.

4. REGLAS DE ARQUITECTURA

Respeta en todo momento:
View → ViewModel → AyudaRepository → AyudaRepositoryRoom → DAO → Room

- Ningún Composable debe acceder a un DAO, a Room ni al Repository.
- Los ViewModels dependen de la interface AyudaRepository.
- Declara cualquier nueva operación primero en AyudaRepository y después impleméntala en AyudaRepositoryRoom.
- Usa Flow para datos observables y StateFlow para el estado de pantalla.
- Mantén las reglas de negocio y los cálculos del SLA fuera de los Composables.
- Evita observadores duplicados y corrutinas que sobrevivan al ViewModel.
- No introduzcas GlobalScope ni ejecutes consultas bloqueantes en el hilo principal.
- Conserva la factoría y la inyección de dependencias existentes.

Archivos principales:
data/local/dao/TicketDao.kt
data/local/entity/Ticket.kt
data/repository/AyudaRepository.kt
data/repository/AyudaRepositoryRoom.kt
viewmodel/PanelViewModel.kt
viewmodel/BandejaViewModel.kt
viewmodel/TicketDetalleViewModel.kt
viewmodel/AtenderViewModel.kt
viewmodel/AyudaViewModelFactory.kt
ui/soporte/PanelScreen.kt
ui/soporte/BandejaScreen.kt
ui/soporte/AtenderTicketScreen.kt
ui/cliente/DetalleTicketScreen.kt
ui/components/TicketCard.kt

Inspecciona su contenido antes de decidir los cambios; no supongas firmas que no existen.

5. MEJORA A: TIEMPO PROMEDIO DE RESOLUCIÓN

Implementa una consulta observable en TicketDao para calcular:
AVG(fechaCierre - fecha)

El cálculo corresponde exclusivamente a tickets:
- Con estado CERRADO.
- Con fechaCierre no nula.
- Con fechaCierre mayor o igual que fecha.

Usa las fechas existentes, expresadas en milisegundos. No cambies el significado de fechaCierre ni añadas una fecha de resolución distinta.

Propaga el resultado mediante:
TicketDao → AyudaRepository → AyudaRepositoryRoom → PanelViewModel → PanelScreen.

Requisitos:
- El resultado debe actualizarse automáticamente cuando se cierre un ticket.
- Cuando no haya tickets cerrados válidos, muestra “Sin tickets cerrados”.
- No confundas ausencia de datos con un promedio real de cero.
- Muestra una tarjeta con el título “Tiempo promedio de resolución”.
- Presenta la duración en unidades comprensibles, por ejemplo “2 h 15 min” o “Menos de 1 min”.
- Documenta que el indicador mide desde la creación hasta el cierre por el estudiante.
- Conserva los conteos, gráficos y alertas existentes.

No se requiere modificar el esquema Room para esta mejora.

6. MEJORA B: CONTADOR DE SLA

Presupuesto por prioridad:
- ALTA: 4 horas.
- MEDIA: 24 horas.
- BAJA: 72 horas.

Fecha límite:
ticket.fecha + duración de la prioridad actual.

Tiempo restante:
fecha límite - hora actual.

Reglas:
- El contador corre para ABIERTO y EN_PROCESO.
- Mientras el tiempo restante sea positivo, muestra “SLA: … restantes”.
- Cuando sea cero o negativo, muestra “SLA vencido hace …”.
- Para RESUELTO muestra “SLA: ticket resuelto”.
- Para CERRADO muestra “SLA: ticket cerrado”.
- No inventes una hora histórica de resolución que el modelo no guarda.
- Si soporte cambia la prioridad, recalcula el plazo usando la fecha original del ticket y la nueva prioridad.
- La reapertura conserva la fecha original y vuelve a aplicar el plazo correspondiente; no reinicies el SLA.
- No guardes un contador decreciente en Room ni DataStore: derívalo de las fechas.
- No alteres las reglas actuales de estados ni la alerta existente de tickets ALTA abiertos por más de una hora.

Implementación:
- Crea un modelo de presentación o una función de dominio reutilizable para el SLA.
- Haz que el cálculo reciba la hora actual para poder verificarlo sin esperar horas.
- Usa un reloj observable compartido por pantalla o ViewModel, con actualización cada 30 segundos.
- No crees una corrutina por tarjeta.
- Cancela o suspende el trabajo cuando deje de ser necesario usando el ciclo de vida y viewModelScope.
- Usa Long para evitar desbordamientos en las duraciones.

Muestra el SLA en:
- Tarjetas de Mis tickets.
- Tarjetas de la bandeja de soporte.
- Detalle del ticket del estudiante.
- Pantalla de atención de soporte.

Usa texto además de color para distinguir vencido, activo y finalizado. Evita cambios innecesarios en el diseño de las tarjetas.

7. MEJORA C: BÚSQUEDA AVANZADA EN SOPORTE

Amplía BandejaViewModel y BandejaScreen.

Conserva el filtro actual por estado y añade:
- Texto.
- Prioridad: Todas, ALTA, MEDIA, BAJA.
- Categoría: Todas y las categorías existentes.

Todos los filtros deben funcionar simultáneamente mediante AND:
estado AND texto AND prioridad AND categoría.

Texto:
- Ignora mayúsculas y minúsculas.
- Ignora espacios al inicio y al final.
- Busca en título, descripción y número de ticket.
- Admite el número con o sin #.
- Un texto vacío no restringe resultados.

Categorías:
- Usa el ID como valor del filtro.
- Incluye categorías inactivas que todavía pueden tener tickets históricos.
- Si se elimina la categoría seleccionada, restablece el filtro a Todas.
- No confundas este selector con el formulario de creación, que debe seguir ofreciendo solo categorías activas.

Resultados:
- Mantén el orden actual ALTA → MEDIA → BAJA y, dentro de cada prioridad, fecha descendente.
- Los filtros no deben ordenar de nuevo los resultados de manera incompatible.
- Combina los filtros y el Flow del Repository en el ViewModel.
- La View solo envía eventos y representa el StateFlow.
- Actualiza los resultados cuando cambien los tickets aunque el usuario no toque los filtros.
- Muestra la cantidad de resultados.
- Añade “Limpiar filtros” para restablecer texto, prioridad, categoría y estado.
- Si no hay coincidencias, muestra “No hay tickets que coincidan con los filtros” y una acción para limpiarlos.
- Distingue ese caso de una bandeja realmente vacía.
- Conserva los filtros al abrir un ticket y volver a la bandeja.
- Asegura que el formulario sea usable con teclado abierto y en pantalla pequeña.

8. DISEÑO Y FUNCIONALIDADES QUE DEBES CONSERVAR

Mantén:
- AyudaTheme(rol, oscuro).
- Cian #0891B2 para Estudiante.
- Violeta #7C3AED para Soporte.
- Modo oscuro leído de PreferenciasStore mediante DataStore.
- Colores de pantalla obtenidos de MaterialTheme.
- Colores semánticos centralizados; no distribuyas literales por las pantallas.
- Navegación y cambio de perfil.
- Login y cierre de sesión.
- Aislamiento de tickets por estudiante.
- Conversación reactiva.
- Cierre con calificación y reapertura.
- Categorías activas e impedimento de borrar categorías con tickets.
- Bloqueo de creación cuando la mesa está cerrada.
- Persistencia de sesión y preferencias.

No borres la base de datos del usuario para resolver errores. Estas mejoras se pueden realizar sin cambiar las entidades; si detectas una necesidad real de cambiar el esquema, justifícala y utiliza una migración que conserve los datos.

9. VERIFICACIÓN SIN REPETICIONES INNECESARIAS

Trabaja por mejora. Antes de cada commit funcional:
- Revisa el diff.
- Comprueba que compila.
- Verifica de forma focalizada el criterio de esa mejora.

Concentra la prueba integrada del emulador al terminar las tres mejoras. No repitas la suite completa después de cada edición.

Verifica como mínimo:
A. Promedio:
- Sin cerrados.
- Un cerrado.
- Varios cerrados con promedio conocido.
- Exclusión de tickets abiertos/resueltos y fechas inválidas.
- Actualización después de cerrar un ticket.

B. SLA:
- Las tres prioridades.
- Instante anterior, exacto y posterior al vencimiento.
- Cambio de prioridad.
- Estados RESUELTO y CERRADO.
- Reapertura.
- Actualización del texto sin volver a abrir la pantalla.

C. Búsqueda:
- Cada filtro por separado.
- Todos combinados.
- Texto con diferencias de mayúsculas y espacios.
- Número con y sin #.
- Sin resultados.
- Limpiar filtros.
- Categorías inactivas con tickets.
- Orden por prioridad y fecha.
- Cambios de datos mientras los filtros están activos.

Usa pruebas pequeñas con fechas controladas para los cálculos. No esperes horas reales y no cambies la hora del dispositivo.

Ya existen:
- DatosYReglasTest: 3 pruebas aprobadas.
- FlujoCompletoTest: 1 recorrido aprobado.
- RestauracionTest: 1 prueba aprobada.

Lee sus precondiciones antes de ejecutarlas:
FlujoCompletoTest requiere datos iniciales y sesión cerrada.
RestauracionTest depende del estado que deja FlujoCompletoTest y se ejecuta después en una instrumentación nueva, sin borrar datos.

Usa un emulador de pruebas separado si necesitas datos controlados. No borres la instalación de presentación.

La compilación debug de Fase 1 pasó. Lint quedó pendiente por una descarga, por lo que no debes presentarlo como aprobado. Intenta completarlo al final si las dependencias están disponibles; informa cualquier bloqueo real.

Captura evidencias reales de las tres mejoras y verifica que las imágenes sean legibles. No fabriques capturas ni resultados de pruebas.

10. COMMITS OBLIGATORIOS

Crea cuatro commits de Fase 2, separados por responsabilidad y con estos mensajes exactos:

1.
ia: agrega tiempo promedio de resolucion al panel

Incluye únicamente la mejora A y su verificación específica.

2.
ia: agrega contador de SLA por prioridad

Incluye únicamente la mejora B y su verificación específica.

3.
ia: agrega busqueda avanzada en la bandeja

Incluye únicamente la mejora C y su verificación específica.

4.
docs: agrega PROMPTS.md

Incluye PROMPTS.md, actualización del README, matriz de aceptación y capturas de Fase 2.

No mezcles las tres mejoras en un solo commit.
No crees commits vacíos.
No agregues archivos de compilación, local.properties, secretos, cachés del IDE ni cambios de otros laboratorios.
Antes de cada commit inspecciona las rutas staged y confirma que todas pertenecen a Semana-08/AyudaCampus.
No modifiques ni reescribas commits ya publicados.

11. DOCUMENTACIÓN

Crea:
Semana-08/AyudaCampus/PROMPTS.md

Incluye para cada prompt realmente utilizado:
- Texto enviado.
- IA y herramienta utilizadas.
- Qué generó.
- Qué corregiste.
- Si respetó MVVM y por qué.
- Archivos afectados.

Registra este prompt inicial y los prompts posteriores relevantes. Si la IA propone acceso directo a DAO/Repository desde la View, corrígelo y documenta ese error.

Termina PROMPTS.md con una reflexión de exactamente cinco líneas sobre:
- Qué mejoró la IA.
- Qué no resolvió correctamente.
- Qué errores cometió.
- Qué aprendiste sobre MVVM y Room.
- Cómo verificaste las reglas de negocio.

No inventes errores, resultados ni intervenciones humanas. Redacta las correcciones según lo que realmente ocurrió.

Actualiza README.md:
- Conserva la documentación y seis capturas de Fase 1.
- Añade una sección Fase 2.
- Explica las tres mejoras y las reglas concretas del SLA.
- Documenta qué significa el promedio.
- Añade las capturas reales de Fase 2 en docs/capturas/fase-2/.
- Enlaza PROMPTS.md.
- Explica cómo ejecutar las verificaciones nuevas.
- Identifica qué comprobaciones pasaron y cuáles quedaron bloqueadas.
- Conserva las respuestas existentes de “Para pensar”.

Actualiza docs/ACEPTACION.md con evidencia de las tres mejoras, sin borrar los resultados anteriores ni atribuirles comprobaciones nuevas que no realizaron.

12. PUBLICACIÓN Y ENTREGA

Cuando implementación, comprobaciones y documentación estén terminadas:
- Confirma que estás en mejora-ia.
- Confirma los cuatro commits nuevos y sus mensajes exactos.
- Confirma que main mantiene la Fase 1.
- Publica mediante:
  git push -u origin mejora-ia

Usa únicamente la autenticación de DiegoMagallanes11.
Si Git por terminal no tiene credenciales, usa Git → Push de Android Studio con esa cuenta. No cambies a una cuenta distinta.
No solicites que el usuario pegue contraseñas ni tokens.
Si hace falta interacción humana para iniciar sesión, explica el paso concreto y continúa después.

Verifica el push comparando el SHA local de mejora-ia con el SHA remoto. Un intento de push no equivale a una publicación exitosa.

No crees un pull request ni mezcles la rama en main, salvo que yo lo solicite después.

Entrega un resumen final con:
- Las tres mejoras terminadas.
- Comprobaciones realizadas y resultados reales.
- Los cuatro hashes de commits.
- Enlace a la rama publicada.
- Enlaces a README y PROMPTS.md.
- Cualquier limitación pendiente.

No declares la Fase 2 completa si falta alguna mejora, documentación, evidencia o publicación. Ante un bloqueo real, describe exactamente qué falta y qué acción concreta permite continuar. Resuelve por tu cuenta las decisiones rutinarias siguiendo estas especificaciones.
```

</details>

**IA y herramienta:** OpenAI Codex, conversación de Codex y herramientas locales.

**Qué generó:** consulta Room observable del promedio; formato de duración; presentación y reloj de SLA; filtros AND de soporte; pantallas; pruebas controladas y documentación. No agregó registro de usuarios, comentarios internos, servidor ni servicios externos.

**Qué corrigió Codex:** comprobó el entorno Windows en lugar de usar las rutas macOS del prompt. La relación de ramas también difería: `origin/mejora-ia` no era antecesora de `main`, por lo que detuvo la preparación de esa rama y pidió decidir cómo conservar su historia. La rama local antigua y todos los stashes se conservaron sin aplicar ni eliminar ninguno. La primera compilación falló porque el JBR de Android Studio era Java 25; se seleccionó el JDK 21 ya instalado sin actualizar dependencias del proyecto. Una comprobación SQL auxiliar necesitó corregir las comillas del comando PowerShell; después pasó. La primera instrumentación no arrancó por un ANR del emulador; se liberaron los procesos de compilación y se preparó el código Android antes de repetir. El recorrido original encontró una tarjeta fuera de la composición de LazyColumn por el espacio ocupado por los nuevos filtros: se cambió su desplazamiento a performScrollToNode, sin cambiar sus reglas ni precondiciones. Se restauraron exclusivamente los datos ficticios del emulador dedicado para repetir ese recorrido. La prueba nueva de búsqueda también necesitó distinguir el texto introducido de la tarjeta con el mismo título; se buscó por número y se hizo reutilizable su preparación de datos ficticios. La revisión de capturas ajustó el orden del selector a ALTA–MEDIA–BAJA y el texto singular «1 resultado», y se verificó de nuevo solo la búsqueda afectada. Los resultados e incidencias quedan en la matriz de aceptación.

**¿Respeta MVVM?:** sí. La nueva operación del promedio se declaró en `AyudaRepository`, se implementó en `AyudaRepositoryRoom` y se propagó desde el DAO hasta el ViewModel. Las pantallas solo representan estado y envían eventos. El SLA y el filtrado se calculan fuera de los Composables. Las referencias directas a Room dentro de las pruebas instrumentadas preparan datos de prueba; no son código de View.

**Archivos afectados:**

- Promedio: `TicketDao.kt`, `AyudaRepository.kt`, `AyudaRepositoryRoom.kt`, `Duracion.kt`, `PanelViewModel.kt`, `PanelScreen.kt`, `PromedioTest.kt`, `PromedioRoomTest.kt`.
- SLA: `Sla.kt`, `Reloj.kt`, `SlaTexto.kt`, `TicketCard.kt`, `MisTicketsViewModel.kt`, `TicketDetalleViewModel.kt`, `AtenderViewModel.kt`, `BandejaViewModel.kt`, las cuatro pantallas correspondientes y `SlaTest.kt`.
- Búsqueda: `FiltrosBandeja.kt`, `BandejaViewModel.kt`, `BandejaScreen.kt`, `TicketCard.kt`, `BusquedaTest.kt`, `BusquedaUiTest.kt` y adaptación de desplazamiento en `FlujoCompletoTest.kt`.
- Documentación: este archivo, `README.md`, `docs/ACEPTACION.md`, texto inicial íntegro en este archivo y capturas de Fase 2.

## Prompt 2: ubicación real y explicación breve

**Texto enviado:**

> el repostorio esta en una de mis carpetas C:\Users\Magal\Programacion-en-Moviles creo que aca, debes de recargar la carpeta con archivos nuevos, dime como lo hiciste para aprender,. Dame contexto del requisito de gemini y como lo podemos cumplir, porfavor usa pocas palabras y explicate bien

El usuario también proporcionó `C:/Users/Magal/Downloads/GLAB-S08-JLEONS-2026_B.pdf`.

**IA y herramienta:** OpenAI Codex; inspección de Git y extracción de texto del PDF mediante pdfplumber.

**Qué generó:** localización del repositorio, comprobación de cambios ajenos, `fetch` y actualización por avance directo de `main` hasta `ac4e370c55a27f214239b5dd934d8be40458a77f`. Explicación de los comandos y del requisito de Gemini.

**Qué corrigió:** rutas del entorno y la suposición inicial de que no estaban disponibles ni el repositorio ni la guía. No descartó los cambios locales de otros laboratorios.

**¿Respeta MVVM?:** no modificó código de aplicación.

**Archivos afectados:** archivos recibidos de GitHub al actualizar `main`; ninguno editado manualmente en este paso.

## Prompt 3: autorización para trabajar con Codex

**Texto enviado:** `dale`.

**Contexto:** respuesta a la propuesta de hacer las mejoras con Codex y documentar una contribución futura y real de Gemini. No hubo intervención de Gemini durante la implementación inicial del 19 de octubre; la participación posterior se registra en los prompts 5 y 6.

**IA y herramienta:** OpenAI Codex y herramientas locales.

**Qué generó:** inicio de implementación y verificación.

**Qué corrigió:** mantuvo el nombre real de la herramienta; no inventó intervención humana ni de Gemini.

**¿Respeta MVVM?:** sí, por las mismas razones indicadas en el prompt 1.

**Archivos afectados:** los archivos de implementación y pruebas indicados en el prompt 1.

## Prompt 4: rama alternativa autorizada

**Texto enviado:** `dale`.

**Contexto:** respuesta a «Recomiendo crear mejora-ia-ayudacampus: conserva todo y permite trabajar limpio. La única diferencia con la guía sería el nombre de la rama. ¿Usamos ese nombre nuevo?».

**IA y herramienta:** OpenAI Codex y Git.

**Qué generó:** rama `mejora-ia-ayudacampus` desde `main` y un worktree separado dentro de la carpeta de trabajo de Codex. La antigua `mejora-ia` local y remota se mantuvo intacta.

**Qué corrigió:** evitó asumir que el commit remoto `4a580c8` pertenecía al historial de la Fase 1. La comprobación mostró ocho commits exclusivos de otro laboratorio en la rama remota y catorce commits de AyudaCampus en `main`.

**¿Respeta MVVM?:** no afecta la arquitectura.

**Archivos afectados:** metadatos Git y copia de trabajo aislada; no cambia archivos de otros laboratorios.

## Prompt 5: contribución real de Gemini — 20 de octubre de 2026

**Texto enviado a Gemini:**

```text
Participa realmente en la verificación de la Fase 2 de AyudaCampus. Las tres mejoras ya fueron implementadas con OpenAI Codex; tu aporte será una prueba adicional del SLA, y se registrará como trabajo de Gemini en Android Studio.

No modifiques archivos, no ejecutes comandos y no propongas dependencias nuevas. Devuelve únicamente un método Kotlin @Test para añadir a SlaTest y dos frases explicativas. No afirmes que lo ejecutaste.

Código real disponible:
- package com.tecsup.ayudacampus
- SlaTest ya importa org.junit.Test, org.junit.Assert.* y com.tecsup.ayudacampus.model.*.
- Tiene esta fixture: private val ticket=Ticket(clienteId=1,categoriaId=1,titulo="Prueba",descripcion="",ubicacion="",fecha=1_000L).
- Ticket es data class con fecha: Long, prioridad: String (ALTA/MEDIA/BAJA), estado: String (ABIERTO/EN_PROCESO/RESUELTO/CERRADO); admite copy.
- calcularSla(ticket: Ticket, ahora: Long): Sla es una función pura. Sla tiene texto: String y situacion: SituacionSla (ACTIVO, VENCIDO, FINALIZADO).
- Presupuestos desde fecha original: ALTA 4 horas, MEDIA 24 horas, BAJA 72 horas. Cada hora son 3_600_000L ms.
- Restante > 0 produce "SLA: ... restantes"; restante <= 0 produce "SLA vencido hace ...". Horas completas se formatean "1 h", "67 h", etc.

Las cuatro pruebas existentes usan fecha=1000L y ya cubren prioridades, límites exactos, estados, reapertura y reloj de 30 s. Falta probar fechas absolutas grandes y el cruce del límite Int de 32 bits.

Diseña UNA prueba de regresión con dos fechas controladas: una próxima a Int.MAX_VALUE cuyo plazo cruce ese límite, y otra fecha Unix realista de 1_800_000_000_000L. En ambas usa la misma hora actual fijada a cinco horas desde la creación, estado EN_PROCESO, y cambia prioridad de ALTA a BAJA mediante copy conservando fecha. Comprueba situación y texto exacto, con expectativas calculadas de forma independiente. Todas las sumas y duraciones deben ser Long. No uses Room, Repository, Composables, reloj real ni esperas.

Nombra la prueba fechasGrandesYCambioDePrioridadConservanElPlazoOriginal. Explica qué regresión detecta y por qué respeta MVVM.
```

**IA y herramienta:** Gemini 3.6 Flash (`gemini-3.6-flash`), panel Agent de Android Studio, dentro de AyudaCampus en `mejora-ia-ayudacampus`. Codex operó la interfaz tras el inicio de sesión realizado por el usuario. La consulta fue enviada realmente; Gemini leyó `SlaTest.kt` y `Duracion.kt`.

**Qué generó:** un método JUnit4 para dos fechas controladas: cerca del límite de Int de 32 bits y una fecha Unix de 1_800_000_000_000 ms. La propuesta inicial calculaba el SLA tras copiar la prioridad a BAJA y comprobaba su situación activa y tiempo restante.

**Qué revisó Codex:** faltaba comprobar ALTA antes del cambio, y la expectativa se derivaba con una fórmula de plazo/restante parecida a la lógica evaluada. Codex pidió reforzar esas aserciones mediante un segundo prompt. No se inventó un error de acceso de la View al DAO ni un defecto funcional de la app.

**¿Respeta MVVM?:** sí. Es una prueba de la función pura `calcularSla(ticket, ahora)`, sin consultar DAO, Room o Repository y sin introducir lógica en Composables. Gemini no editó archivos ni ejecutó comandos; Codex integró después la propuesta corregida.

**Archivos afectados:** propuestas y evidencia en `docs/gemini/`; el método definitivo se incorporó a `app/src/test/java/com/tecsup/ayudacampus/SlaTest.kt` después del prompt 6.

## Prompt 6: corrección real solicitada a Gemini

**Texto enviado a Gemini:**

```text
Corrige tu propuesta anterior. Actualmente solo evalúa calcularSla(ticketBaja, ahora), por lo que no comprueba el resultado ALTA previo al cambio. Añade para cada fecha la evaluación de ticketAlta y las aserciones exactas SituacionSla.VENCIDO y "SLA vencido hace 1 h". Después conserva el copy a BAJA sin cambiar fecha ni ahora y comprueba SituacionSla.ACTIVO y el literal "SLA: 67 h restantes". Evita recalcular la expectativa usando la misma fórmula de plazo/restante que la función bajo prueba. Mantén las dos fechas de tu propuesta, el nombre del método, Kotlin/JUnit4 y todas las fechas/duraciones como Long. No edites archivos ni ejecutes comandos: devuelve solo el método corregido y dos frases sobre el cambio.
```

**IA y herramienta:** Gemini 3.6 Flash en Android Studio, en la misma conversación titulada «Write SLA test for large dates and priority changes.».

**Qué generó:** la prueba `fechasGrandesYCambioDePrioridadConservanElPlazoOriginal`. Evalúa ALTA con cinco horas transcurridas como VENCIDO y «SLA vencido hace 1 h»; después cambia a BAJA conservando fecha y ahora y espera ACTIVO y «SLA: 67 h restantes». Repite el caso con las dos fechas grandes y usa Long.

**Qué corrigió Codex:** solicitó la comprobación previa de ALTA y expectativas literales; Gemini devolvió el método corregido. Codex transcribió e integró ese método, adaptando únicamente su indentación, revisó las expectativas y ejecutó las pruebas. No fue necesario cambiar la lógica de producción ni dependencias.

**¿Respeta MVVM?:** sí. La función recibe el instante actual, por lo que la prueba no espera tiempo real ni modifica el reloj del dispositivo. No crea accesos de la View al repositorio o a Room.

**Archivos afectados:** `SlaTest.kt`, `PROMPTS.md`, `README.md`, `docs/ACEPTACION.md` y los archivos de evidencia en `docs/gemini/`.

**Resultado real:** `:app:testDebugUnitTest --tests com.tecsup.ayudacampus.SlaTest` terminó correctamente: 5 pruebas, 0 fallos y 0 errores, incluida la prueba de Gemini. Ejecución realizada por Codex el 20 de octubre de 2026. Las pruebas de las otras mejoras y las instrumentadas conservan sus resultados anteriores; no se afirma haberlas repetido en este paso.

**Evidencia:** [prompts y respuestas transcritas](docs/gemini/RESPUESTAS.md), [consulta real](docs/gemini/01-consulta-real.jpg), [propuesta inicial](docs/gemini/02-propuesta-inicial.jpg) y [corrección visible](docs/gemini/04-explicacion-corregida.jpg). Las capturas se guardaron desde la interfaz real y se verificó su legibilidad. El usuario detuvo temporalmente el control con Esc y después solicitó continuar; la última captura y ambos prompts ya habían quedado guardados.

Los cuatro commits iniciales ya estaban publicados. Este aporte posterior se conserva en un commit adicional, sin reescribirlos. El alcance real de Gemini es la prueba de regresión del SLA; la implementación previa de las tres mejoras continúa atribuida a Codex.

## Prompt 7 — rúbrica nombre y navegación

**Texto enviado por el usuario a Codex:**

> cumple con esto nuestro android? ademas no me llamo josé, me llamo Diego Magallanes, aparte el navbar no funciona correctamente, cuando entro a uno no puedo entrar a otra ocpion, perfil tampoco creo que no hay nada, fijate que mas podriamos mejorar

El usuario adjuntó una rúbrica de seis criterios: commits por avance, modelo de datos, ViewModel, Vista y ejercicio 1, puntualidad e informe sin copias, observaciones y conclusiones. Su suma es 20 puntos. Se trató como criterio de evaluación, no como evidencia de una nota obtenida.

**Aclaración real del usuario:**

> No era jose, era juan, y esta dentro de la app

**IA y herramienta:** Codex, inspección de archivos, Gradle y ADB. Gemini no intervino en esta revisión.

**Qué generó:** personalización idempotente de la cuenta ficticia existente, conservación de pestañas, gestión del teclado y foco, consumo del evento de envío, resumen del perfil con cuatro estados, una prueba de datos antiguos y un recorrido de navegación. También agregó el contraste con la rúbrica y observaciones técnicas al informe.

**Qué corrigió y observó:** antes de editar, la secuencia Perfil → Nuevo → Tickets → Perfil funcionó y el perfil tenía contenido; no se atribuye una causa no reproducida al bloqueo descrito. Se conservó el correo de acceso para no cambiar las credenciales. Una primera llamada de diagnóstico falló por comillas de Python dentro de PowerShell; se corrigió guardando el script en un archivo. Los tests focalizados pasaron en su primera ejecución. Se consumió la confirmación de envío para evitar que restaurar el formulario repita la navegación tras adoptar saveState y restoreState.

**Respeta MVVM:** sí. La personalización pertenece a la inicialización transaccional de Room; las pantallas no acceden a DAOs ni Repository. El resumen sigue calculándose en PerfilViewModel y la navegación usa el NavController del punto de composición. No se cambió la interfaz del Repository porque no se agregó una operación funcional accesible desde las pantallas.

**Archivos afectados:** DatosIniciales.kt, UsuarioDao.kt, NavAyuda.kt, NuevoTicketScreen.kt, PerfilScreen.kt, NuevoTicketViewModel.kt, PerfilViewModel.kt, DatosYReglasTest.kt y NavegacionPerfilTest.kt; saludos esperados en tres pruebas históricas, README.md, docs/ACEPTACION.md, docs/RUBRICA.md y dos capturas de revisión. El Word se mantiene como entrega local fuera del repositorio.

**Verificación:** compilación debug y de instrumentación, diez unitarias y cinco instrumentadas focalizadas aprobadas. No se repitieron el recorrido completo, restauración, búsqueda UI ni lint en esta revisión. Los resultados anteriores conservan su fecha y alcance.

## Prompt 8 — auditoría completa y aclaración de Fase 1

**Texto real del usuario:**

> revisa el alboratorio y haz un analicis de lo que le falta a nuestra aplicacion, fijate que la apret con ia y sin ia esten compeltas y luego revisa la aprte con ia de nuestra app y haz pruebas de que funcione adecuadamente todo tiene que ser funcional de derecha a izquierda. si tienes preguntas de algo o alguna observacion, dimela

**Aclaración posterior real:**

> la fase sin ia ya esta terminada

**IA y herramienta:** Codex; lectura del PDF local completo, inspección de archivos y Git, Gradle, ADB y un AVD independiente. Gemini no intervino en esta auditoría.

**Qué generó y verificó:** contraste de contratos, ejecución base como regresión, restauración en un proceso nuevo, CRUD de categorías, navegación con borrador y teclado, promedio, SLA y filtros de soporte. Añadió AUDITORIA.md, resultados y capturas reales, y amplió el informe Word. Main y sus catorce commits se conservaron.

**Qué corrigió:** el número visible con ceros iniciales y la búsqueda # vacía del estudiante; el valor inicial del promedio que confundía carga con ausencia; el consumo de confirmación para restaurar Nuevo. La guía exige mantener autorNombre como foto histórica: se retiró el renombrado de comentarios y se recuperaron dos nombres del respaldo, sin cambiar mensajes ni revertir la base entera. Esa herramienta de mantenimiento fue temporal y se retiró de la entrega.

**Incidencias reales:** se agregó un import ausente de Assert al ampliar el recorrido. La primera prueba nueva del promedio falló durante su limpieza al restablecer Main antes de terminar la cancelación; se corrigió la limpieza, sin cambiar la lógica de la app. La captura del panel se volvió a tomar esperando el resultado de Room. No se inventó un fallo de Gemini ni de acceso a DAO desde la View.

**Respeta MVVM:** sí. Búsqueda y SLA se calculan fuera de la View; PanelViewModel representa los estados de carga y datos; la inicialización de Room personaliza solo la cuenta de práctica. Las pantallas no consultan Repository ni DAO. Los tests pueden consultar Room para disponer y verificar fixtures, lo que no forma parte del flujo productivo.

**Archivos afectados adicionales:** model/BusquedaTicket.kt, model/FiltrosBandeja.kt, MisTicketsViewModel.kt, PanelViewModel.kt, BusquedaTest.kt, PromedioTest.kt; ampliaciones de FlujoCompletoTest, RestauracionTest, NavegacionPerfilTest y BusquedaUiTest; README, PROMPTS y documentación de auditoría con ocho capturas.

**Resultado:** doce unitarias y diez instrumentadas distintas aprobadas. Lint terminó sin errores y con diecisiete advertencias; tres checks personalizados de Navigation se omiten y no se dan por ejecutados. Las repeticiones focalizadas no aumentan el número de casos distintos.

**Seguimiento de evidencia del prompt 8:** la revisión del Word detectó una captura adelantada al dibujo nativo. Codex agregó espera de inactividad y 250 ms antes de capturar; el único recorrido afectado se repitió y aprobó en 37.225 s. Se guardó la captura real de cero coincidencias y una adicional del teclado, conservando la captura anterior válida del resultado combinado. No se atribuye esta corrección a Gemini ni se fabrican imágenes.

## Reflexión (exactamente cinco líneas)
Codex implementó las tres mejoras y Gemini amplió la verificación del SLA con fechas grandes.
La primera propuesta de Gemini no comprobaba el SLA ALTA previo al cambio de prioridad.
Codex pidió corregir las aserciones y Gemini generó la versión con expectativas explícitas.
MVVM mantiene los cálculos fuera de la View y Room comunica los cambios mediante Flow.
La verificación combina datos controlados, cinco pruebas de SLA aprobadas y evidencia real de ambas herramientas.
