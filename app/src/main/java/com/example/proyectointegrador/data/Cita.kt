package com.example.proyectointegrador.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreClienta: String,
    val celular: String,
    val servicio: String,           // varios servicios separados por coma
    val estilista: String,          // varias empleadas separadas por coma
    val fecha: String,              // dd/MM/yyyy
    val hora: String,
    val anticipo: Boolean = false,
    val montoAnticipo: Double = 0.0,
    val estatus: String = "Pendiente",
    val precioFinal: Double = 0.0,
    val propina: Double = 0.0       // nuevo: se captura al cerrar la cita
)