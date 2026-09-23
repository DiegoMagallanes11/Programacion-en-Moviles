package com.magallanes.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class StudentDetail(
    val id: Int,
    val name: String,
    val career: String,
    val studentCode: String,
    val email: String,
    val faculty: String
)

fun getStudentDetail(itemId: Int): StudentDetail {
    val details = mapOf(
        1 to StudentDetail(1, "Diego Magallanes Linares", "Diseño y Desarrollo de Software", "EST-2024001", "diego.magallanes@tecsup.edu.pe", "Facultad de Tecnología e Informática"),
        2 to StudentDetail(2, "María García López", "Ingeniería de Software", "EST-2024002", "maria.garcia@tecsup.edu.pe", "Facultad de Ingeniería"),
        3 to StudentDetail(3, "Carlos Mendoza Ruiz", "Redes y Comunicaciones", "EST-2024003", "carlos.mendoza@tecsup.edu.pe", "Facultad de Infraestructura IT"),
        4 to StudentDetail(4, "Ana Martínez Torres", "Inteligencia Artificial", "EST-2024004", "ana.martinez@tecsup.edu.pe", "Facultad de Tecnología e Informática"),
        5 to StudentDetail(5, "Luis Fernández Castro", "Ciberseguridad", "EST-2024005", "luis.fernandez@tecsup.edu.pe", "Facultad de Infraestructura IT"),
        6 to StudentDetail(6, "Sofía Torres Vargas", "Sistemas de Información", "EST-2024006", "sofia.torres@tecsup.edu.pe", "Facultad de Ingeniería"),
        7 to StudentDetail(7, "Diego Morales Peña", "Diseño y Desarrollo de Software", "EST-2024007", "diego.morales@tecsup.edu.pe", "Facultad de Tecnología e Informática"),
        8 to StudentDetail(8, "Elena Rojas Silva", "Analítica de Datos", "EST-2024008", "elena.rojas@tecsup.edu.pe", "Facultad de Tecnología e Informática")
    )
    return details[itemId] ?: StudentDetail(
        id = itemId,
        name = "Alumno #$itemId",
        career = "Carrera Académica",
        studentCode = "EST-202400$itemId",
        email = "alumno$itemId@tecsup.edu.pe",
        faculty = "Facultad de Tecnología e Informática"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val student = getStudentDetail(itemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expediente Académico",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A148C)
                )
            )
        },
        containerColor = Color(0xFFF8F5FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera morada con esquinas inferiores redondeadas y avatar circular superpuesto centrado en el medio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                // Cabecera morada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(
                            color = Color(0xFF4A148C),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                )

                // Avatar circular superpuesto centrado en el medio
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.BottomCenter)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF6A1B9A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y carrera del alumno
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1B20),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = student.career,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF6A1B9A),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta blanca con ítems de ID Estudiante, Correo Electrónico y Facultad
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailInfoItem(
                        icon = Icons.Default.Badge,
                        label = "ID Estudiante",
                        value = student.studentCode
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    DetailInfoItem(
                        icon = Icons.Default.Email,
                        label = "Correo Electrónico",
                        value = student.email
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    DetailInfoItem(
                        icon = Icons.Default.School,
                        label = "Facultad",
                        value = student.faculty
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFF3E5F5), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6A1B9A),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1B20)
            )
        }
    }
}
