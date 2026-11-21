package com.example.proyectointegrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectointegrador.presentation.CierreScreen
import com.example.proyectointegrador.presentation.FormularioScreen
import com.example.proyectointegrador.presentation.ListaScreen
import com.example.proyectointegrador.presentation.MovimientoScreen
import com.example.proyectointegrador.presentation.ProductoFormScreen
import com.example.proyectointegrador.presentation.ProductosScreen
import com.example.proyectointegrador.ui.theme.ProyectoIntegradorTheme
import com.example.proyectointegrador.viewmodel.CitaViewModel
import com.example.proyectointegrador.viewmodel.ProductoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<CitaViewModel>()
    private val productoViewModel by viewModels<ProductoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoIntegradorTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "lista") {
                    composable("lista") {
                        ListaScreen(
                            viewModel = viewModel,
                            onAgregar = {
                                viewModel.citaEnEdicion = null   // cita nueva: formulario vacío
                                navController.navigate("formulario")
                            },
                            onEditar = { navController.navigate("formulario") },
                            onCerrar = { navController.navigate("cierre") },
                            onProductos = { navController.navigate("productos") }
                        )
                    }
                    composable("formulario") {
                        // Lista de citas actuales, para revisar empalmes al guardar
                        val citas by viewModel.citas.collectAsState(initial = emptyList())
                        FormularioScreen(
                            citaEnEdicion = viewModel.citaEnEdicion,
                            citasExistentes = citas
                        ) { cita ->
                            if (viewModel.citaEnEdicion == null) viewModel.guardar(cita)
                            else viewModel.actualizar(cita)
                            navController.popBackStack()
                        }
                    }
                    composable("cierre") {
                        // La cita que se va a cerrar viene de la variable del ViewModel
                        viewModel.citaEnEdicion?.let { cita ->
                            CierreScreen(cita) { citaCerrada ->
                                viewModel.actualizar(citaCerrada)
                                navController.popBackStack()
                            }
                        }
                    }
                    composable("productos") {
                        ProductosScreen(
                            viewModel = productoViewModel,
                            onAgregar = {
                                productoViewModel.productoEnEdicion = null   // producto nuevo
                                navController.navigate("producto_form")
                            },
                            onEditar = { navController.navigate("producto_form") },
                            onEntrada = { navController.navigate("entrada") },
                            onVenta = { navController.navigate("venta") },
                            onVolver = { navController.popBackStack() }
                        )
                    }
                    composable("producto_form") {
                        ProductoFormScreen(productoViewModel.productoEnEdicion) { producto ->
                            if (productoViewModel.productoEnEdicion == null) productoViewModel.guardar(producto)
                            else productoViewModel.actualizar(producto)
                            navController.popBackStack()
                        }
                    }
                    composable("entrada") {
                        // El producto elegido viene de la variable del ViewModel
                        productoViewModel.productoEnEdicion?.let { producto ->
                            MovimientoScreen(producto, esEntrada = true) { actualizado ->
                                productoViewModel.actualizar(actualizado)
                                navController.popBackStack()
                            }
                        }
                    }
                    composable("venta") {
                        productoViewModel.productoEnEdicion?.let { producto ->
                            MovimientoScreen(producto, esEntrada = false) { actualizado ->
                                productoViewModel.actualizar(actualizado)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}