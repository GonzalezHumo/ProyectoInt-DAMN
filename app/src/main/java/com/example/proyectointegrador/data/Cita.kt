package com.example.proyectointegrador.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreClienta: String,
    val celular: String,
    val servicio: String,
    val estilista: String,
    val fecha: String,
    val hora: String,
    val anticipo: Boolean = false,
    val montoAnticipo: Double = 0.0,
    val estatus: String = "Pendiente",
    val precioFinal: Double = 0.0
)