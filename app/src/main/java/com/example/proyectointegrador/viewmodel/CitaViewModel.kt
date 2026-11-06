package com.example.proyectointegrador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.proyectointegrador.data.AppDatabase
import com.example.proyectointegrador.data.CitaRepository

class CitaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CitaRepository(
        AppDatabase.getDatabase(application).citaDao()
    )

    val citas = repository.citas
}