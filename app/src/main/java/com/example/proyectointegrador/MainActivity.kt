package com.example.proyectointegrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectointegrador.presentation.ListaScreen
import com.example.proyectointegrador.ui.theme.ProyectoIntegradorTheme
import com.example.proyectointegrador.viewmodel.CitaViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CitaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoIntegradorTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "lista") {
                    composable("lista") {
                        ListaScreen(viewModel) {
                            // Aquí irá la navegación al formulario (paso 4)
                        }
                    }
                }
            }
        }
    }
}