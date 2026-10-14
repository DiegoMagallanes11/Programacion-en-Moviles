
package com.tecsup.ayudacampus.data.local.entity
import androidx.room.*
@Entity(tableName="usuarios", indices=[Index(value=["correo"],unique=true)])
data class Usuario(@PrimaryKey(autoGenerate=true) val id: Int = 0, val nombre: String, val correo: String, val clave: String, val rol: String)
