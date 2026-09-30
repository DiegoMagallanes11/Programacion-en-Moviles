# Laboratorio 06 - Menú Navegación (TECSUP Store)

**Estudiante:** Diego Magallanes  
**Curso:** Programación en Móviles

---

## Preguntas de reflexión

### 1. ¿Por qué el DropdownMenu se declara dentro de un Box junto al icono que lo activa, y no en cualquier parte de la pantalla?
Porque el menú necesita saber exactamente en qué posición desplegarse. Al poner el icono y el `DropdownMenu` juntos dentro del mismo `Box`, el menú toma como referencia el botón de los tres puntos y se abre justo debajo de él. Si lo pusiera en cualquier otro lado, se mostraría desalineado o en una parte de la pantalla que no corresponde.

### 2. ¿Qué diferencia de alcance hay entre las opciones del DropdownMenu y las del NavigationDrawer?
* **DropdownMenu:** Tiene un alcance local. Sus opciones solo afectan al producto específico sobre el que se hizo clic (como marcarlo de favorito o compartirlo).
* **NavigationDrawer:** Tiene un alcance global. Sirve para la navegación general de toda la app y nos permite cambiar entre diferentes pantallas (Inicio, Mis pedidos, Perfil, etc.).

### 3. ¿Cómo tuviste que estructurar tu código para que el contador de favoritos del drawer "se entere" de lo que pasa en el Dropdown Menu de cada producto?
Tuve que subir el estado a la pantalla principal (`AppNavegacion`). Ahí declaré la lista de productos con `mutableStateListOf`. Cuando el usuario presiona "Favoritos" en el menú de la tarjeta, se activa una función que modifica ese producto en la lista principal. Luego, contamos cuántos favoritos hay en total y ese número se lo pasamos al Drawer para que muestre el contador actualizado.

### 4. ¿Qué tuviste que corregir del código que te generó la IA para la mejora del badge de favoritos?
El código que daba la IA al inicio modificaba una lista normal de Kotlin, por lo que Jetpack Compose no se daba cuenta del cambio y el contador del menú lateral no se actualizaba solo. Lo corregí usando una lista reactiva (`mutableStateListOf`) y actualizando el elemento usando `.copy(esFavorito = ...)` para que la pantalla se vuelva a dibujar sola cuando cambia un favorito.

---

## Observaciones y conclusiones

### Observaciones
Al principio tuve algunos problemas con los imports de los iconos de Material 3, ya que algunos nombres cambiaban y no se mostraban correctamente hasta importar la librería adecuada.


### Conclusiones
La IA en la Fase 2 sirvió para avanzar más rápido con el Badge, pero me di cuenta de que no se puede copiar el código a ciegas porque suele fallar en el manejo de estados reactivos.