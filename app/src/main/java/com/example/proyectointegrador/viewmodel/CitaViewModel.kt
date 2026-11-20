package com.example.proyectointegrador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegrador.data.AppDatabase
import com.example.proyectointegrador.data.Cita
import com.example.proyectointegrador.data.CitaRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CitaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CitaRepository(
        AppDatabase.getDatabase(application).citaDao()
    )

    val citas = repository.citas

    fun guardar(cita: Cita) {
        viewModelScope.launch {
            repository.guardar(cita)
        }
    }

    fun eliminar(cita: Cita) {
        viewModelScope.launch {
            repository.eliminar(cita)
        }
    }

    fun cambiarEstatus(cita: Cita, estatus: String) {
        viewModelScope.launch {
            repository.actualizar(cita.copy(estatus = estatus))
        }
    }

    // Cita que se está editando; null significa que el formulario es para una cita nueva
    var citaEnEdicion by mutableStateOf<Cita?>(null)

    fun actualizar(cita: Cita) {
        viewModelScope.launch { repository.actualizar(cita) }
    }
}

