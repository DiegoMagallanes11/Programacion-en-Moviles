# Mejoras con IA

## Prompt para la Fase 2

> Trabaja solo en la rama mejora-ia-semana-15 de AvisosCampus. En Kotlin y Jetpack Compose, añade el canal Urgentes para los recordatorios, muestra el texto completo con BigTextStyle y agrega un switch para avisar cuando se conecte o desconecte el cargador. Mantén MVVM y el historial, deja el ViewModel sin Context y da de baja el receptor en onDispose. Separa las tres mejoras en commits y explica cómo probar cada una.

Prompt redactado para reproducir las mejoras. El registro de abajo corresponde al trabajo asistido con ChatGPT.

| Mejora del prompt | Propuesta de ChatGPT | Decisión aplicada | Motivo |
| --- | --- | --- | --- |
| Canal Urgentes | Seleccionar el canal con el origen del aviso. | Usar Urgentes para recordatorios y Avisos para el resto. | Centraliza la elección en Notificador. |
| Texto expandible | Pasar el texto completo a BigTextStyle. | Añadir un recordatorio largo para poder verificar la mejora. | Mantiene el contenido en el ViewModel. |
| Avisos del cargador | Registrar un receptor dinámico con un switch propio. | Dar de baja el receptor en onDispose. | El switch controla el registro sin acoplar el ViewModel a Android. |
