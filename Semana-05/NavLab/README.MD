# Laboratorio 05: Navegación en Jetpack Compose (NavLab)

* **Estudiante:** Diego Magallanes
* **Curso:** Programación Móviles
* **Docente:** Juan León
* **Institución:** Tecsup

---

## Descripción del Proyecto
Proyecto desarrollado en Android Studio con **Jetpack Compose** y **Material 3**. Implementa el flujo de navegación entre pantallas utilizando `NavHost`, `NavController` y el manejo de rutas mediante una `sealed class`.

En la segunda fase del laboratorio se mejoró el diseño visual transformándolo en un **Portal Académico** (estilo morado con tarjetas y expedientes) manteniendo la misma lógica de navegación y paso de argumentos (`itemId`).
## Actividad 1:

![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)

## Actividad 2:
### Primer intento usando IA
### Promp utilizado:
Imagina que eres un programador en móviles y además tienes conocimientos desarrollados sobre interfaces, quiero que evalúes mi aplicación y mejores el diseño visual de esta.

![img_4.png](img_4.png)
### Segundo intento
### Prompt utilizado:
Imagina que eres un programador de móviles y tienes conocimientos desarrollados sobre interfaces, quiero que evalúes y mejores el diseño visual de mi programa. Sin embargo, no modifiques ni elimines los parámetros de navegación, intenta usar degrades para la paleta de colores y dale un diseño más profesional dirigido como para un centro educativo.

![img_6.png](img_6.png)

### Tercer intento
### Prompt utilizado:
Actúa como dev Android experto en Jetpack Compose. Rediseña 4 pantallas estilo "Portal Académico" en tonos morados respetando estrictamente los parámetros de navegación (`navController`, `itemId`, `popUpTo`):
1. HomeScreen: Fondo degradado morado, saludo "Bienvenido, Diego Magallanes Linares", 2 tarjetas blancas ("Directorio de Alumnos" y "Mi Perfil Académico").
2. ListScreen: TopBar "Directorio de Alumnos" y LazyColumn con tarjetas blancas de estudiantes (avatar circular morado, nombre, carrera y flecha a la derecha).
3. DetailScreen: TopBar morada "Expediente Académico", avatar centrado superpuesto sobre cabecera curva morada y tarjeta blanca con ID, Correo y Facultad.
4. ProfileScreen: TopBar "Configuración de Perfil", cabecera morada con avatar y nombre ("Diego Magallanes Linares"), datos en secciones ("INFORMACIÓN PERSONAL" y "ACADÉMICO").

![img_8.png](img_8.png)
![img_9.png](img_9.png)
![img_10.png](img_10.png)
![img_11.png](img_11.png)
