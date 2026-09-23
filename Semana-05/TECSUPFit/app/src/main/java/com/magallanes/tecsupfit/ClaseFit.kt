package com.magallanes.tecsupfit

data class ClaseFit(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val entrenador: String,
    val duracion: String,
    val descripcion: String,
    val horariosDisponibles: List<String>
)

val listaClasesEjemplo = listOf(
    ClaseFit(1, "Spinning Intenso", "Cardio", "Carlos Ruiz", "45 min", "Sesión de ciclismo de alta intensidad para quemar calorías.", listOf("07:00 AM", "06:00 PM", "08:00 PM")),
    ClaseFit(2, "CrossFit WOD", "Fuerza", "Ana Torres", "60 min", "Entrenamiento funcional variado de alta intensidad.", listOf("08:00 AM", "05:00 PM")),
    ClaseFit(3, "Yoga Flow", "Mente y Cuerpo", "Sofía Mendoza", "50 min", "Secuencia fluida para mejorar flexibilidad y reducir el estrés.", listOf("09:00 AM", "07:00 PM")),
    ClaseFit(4, "Boxeo Fitness", "Cardio", "Marcos Silva", "45 min", "Técnicas de boxeo combinadas con ejercicios cardiovasculares.", listOf("06:00 AM", "06:30 PM"))
)