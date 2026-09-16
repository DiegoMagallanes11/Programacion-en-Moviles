# Laboratorio 04: Mi Carrito TECSUP

**Alumno:** Diego Magallanes  
**Curso:** Programación en Móviles  
**Docente:** Juan José León Suiyon  
**Rama:** rama-semana-04

## Descripción
Aplicación desarrollada en Jetpack Compose para gestionar un carrito de compras. Permite ingresar productos (nombre, precio y cantidad) mediante un formulario, mostrarlos en una lista dinámica con `LazyColumn`, eliminar elementos individuales y calcular en tiempo real el subtotal, el IGV (18%) y el total a pagar.

## Capturas de pantalla
## Carrito vacío:
![img1.png](img1.png)
## Agregando al carrito:
![img2.png](img2.png)
## Carrito con elementos:
![img3.png](img3.png)
## Eliminando productos:
![img4.png](img4.png)


## Preguntas conceptuales

### 1. ¿Por qué usar `mutableStateListOf` y no una `MutableList` normal?
Porque `mutableStateListOf` es una lista observable por Jetpack Compose. Cuando agregamos o quitamos elementos, Compose se entera del cambio y redibuja automáticamente la pantalla (`LazyColumn` y totales). Con una `MutableList` común los datos cambiarían internamente, pero la interfaz no se actualizaría.

### 2. ¿Por qué la lista se declara con `val` si se pueden agregar elementos?
Porque la palabra `val` protege la referencia de la lista (la dirección de memoria donde reside), la cual no cambia durante la ejecución. Lo que se modifica es el contenido interno de la lista (agregar o quitar items), no la variable en sí.

### 3. ¿Qué función cumple `weight(1f)` en la `LazyColumn`?
Obliga a la `LazyColumn` a ocupar todo el espacio vertical disponible que queda libre entre el formulario y el panel inferior. De esta forma la lista puede hacer scroll en la zona central mientras el panel de totales se mantiene fijo abajo.