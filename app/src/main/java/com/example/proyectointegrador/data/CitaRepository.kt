package com.example.proyectointegrador.data

import kotlinx.coroutines.flow.Flow

class CitaRepository(private val dao: CitaDao) {
    val citas: Flow<List<Cita>> = dao.obtenerTodas()

    suspend fun guardar(cita: Cita) = dao.insertar(cita)

    suspend fun eliminar(cita: Cita) = dao.eliminar(cita)
}