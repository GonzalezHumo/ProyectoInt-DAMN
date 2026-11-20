package com.example.proyectointegrador.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val categoria: String,        // Shampoo/Acondicionador, Accesorio o Snack
    val precioVenta: Double,
    val costo: Double,
    val existencias: Int = 0,
    val stockMinimo: Int = 5      // por debajo de este número se avisará que hay poco
)