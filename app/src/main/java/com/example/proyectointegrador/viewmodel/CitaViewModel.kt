package com.example.proyectointegrador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegrador.data.AppDatabase
import com.example.proyectointegrador.data.Cita
import com.example.proyectointegrador.data.CitaRepository
import kotlinx.coroutines.launch

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
}

