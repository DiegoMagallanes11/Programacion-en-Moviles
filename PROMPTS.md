# Registro de asistencia de IA

La implementación se trabajó con ChatGPT. La tabla recoge las instrucciones de trabajo derivadas del pedido de completar la guía y los resultados de cada cambio. Gemini y la revisión del estudiante quedan pendientes.

| Instrucción de trabajo | Resultado | Decisión aplicada | Motivo |
| --- | --- | --- | --- |
| Agregar `Accept: application/json` a las llamadas. | Un interceptor de OkHttp agrega el encabezado. | El interceptor usa `header` y se ejecuta antes del registro de tráfico. | Las solicitudes llevan un solo valor para `Accept` y el registro puede mostrar el encabezado real. |
| Buscar por título entre publicaciones descargadas. | El ViewModel combina el texto de búsqueda con el estado de la lista. | Las coincidencias ignoran mayúsculas y conservan el filtro de usuario. | Escribir una búsqueda usa los datos disponibles y evita peticiones repetidas. |
| Permitir eliminar una publicación desde su detalle. | La interfaz Retrofit declara `DELETE` con `Response<Unit>` y el repositorio revisa el código HTTP. | La pantalla pide confirmar y la lista oculta la publicación durante la sesión tras una respuesta exitosa. | JSONPlaceholder simula el borrado y la confirmación evita pulsaciones accidentales. |
