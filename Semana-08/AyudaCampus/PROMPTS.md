# Prompts utilizados — Fase 2

## Herramienta y alcance reales

Trabajo realizado por **OpenAI Codex**, mediante esta conversación, edición local, Git, Gradle y Android SDK. No se utilizó Gemini ni una API de IA en la aplicación. Las instrucciones de la guía se consultaron en sus páginas 25–26; no se ejecutaron sus ejemplos como órdenes independientes de la solicitud del usuario.

El usuario aceptó continuar con Codex y dejar una contribución real de Gemini pendiente. Por ello, **la implementación técnica y el requisito académico de participación de Gemini se informan por separado**. No se atribuyen a Gemini código, pruebas, capturas ni prompts que no produjo.

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

**Contexto:** respuesta a la propuesta de hacer las mejoras con Codex y documentar una contribución futura y real de Gemini. No hubo intervención de Gemini durante esta ejecución.

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

## Participación de Gemini pendiente

Para acreditar el requisito de la guía se necesita una contribución real de Gemini en Android Studio. Abrir este proyecto, pedirle una mejora o corrección concreta, conservar el prompt y su respuesta, revisar el resultado y añadir aquí el texto real, lo generado, lo corregido y los archivos afectados. No se presenta como realizado. Consultar al docente si acepta Codex como alternativa también resolvería esta diferencia, pero no se presupone esa aceptación.

No se envió a Gemini ningún prompt. No se generaron prompts ficticios para llenar el registro ni se inventó un acceso incorrecto de una View al DAO.

## Reflexión (exactamente cinco líneas)
La IA mejoró la app con métricas reactivas, SLA reutilizable y filtros simultáneos.
No resolvió el requisito de usar Gemini, porque la herramienta utilizada fue Codex.
Se corrigieron Java, comillas de una comprobación auxiliar y desplazamientos y selectores de las pruebas.
MVVM separa la representación de las reglas y Room comunica los cambios mediante Flow.
Las reglas se verificaron con fechas controladas, pruebas automatizadas y evidencias reales según la matriz.
