package com.example.proyectointegrador.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Query("SELECT * FROM citas ORDER BY fecha, hora") //trae todas las citas
    fun obtenerTodas(): Flow<List<Cita>> //actualiza lista

    @Insert
    suspend fun insertar(cita: Cita)

    @Update
    suspend fun actualizar(cita: Cita)

    @Delete
    suspend fun eliminar(cita: Cita)
}