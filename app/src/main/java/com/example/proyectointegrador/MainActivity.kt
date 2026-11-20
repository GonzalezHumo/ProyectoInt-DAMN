package com.example.proyectointegrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectointegrador.presentation.CierreScreen
import com.example.proyectointegrador.presentation.FormularioScreen
import com.example.proyectointegrador.presentation.ListaScreen
import com.example.proyectointegrador.ui.theme.ProyectoIntegradorTheme
import com.example.proyectointegrador.viewmodel.CitaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<CitaViewModel>()

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
                            onCerrar = { navController.navigate("cierre") }
                        )
                    }
                    composable("formulario") {
                        FormularioScreen(citaEnEdicion = viewModel.citaEnEdicion) { cita ->
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
                }
            }
        }
    }
}