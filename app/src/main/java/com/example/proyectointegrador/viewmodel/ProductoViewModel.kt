package com.example.proyectointegrador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegrador.data.AppDatabase
import com.example.proyectointegrador.data.Producto
import com.example.proyectointegrador.data.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductoRepository(
        AppDatabase.getDatabase(application).productoDao()
    )

    // Lista reactiva de productos
    val productos = repository.productos

    fun guardar(producto: Producto) {
        viewModelScope.launch { repository.guardar(producto) }
    }

    fun actualizar(producto: Producto) {
        viewModelScope.launch { repository.actualizar(producto) }
    }

    fun eliminar(producto: Producto) {
        viewModelScope.launch { repository.eliminar(producto) }
    }
}