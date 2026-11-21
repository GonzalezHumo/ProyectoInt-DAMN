package com.example.proyectointegrador.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectointegrador.data.Cita

// Porcentaje de comisión de ejemplo; se cambia aquí si el salón usa otro (0.30 = 30%)
private const val PORCENTAJE_COMISION = 0.30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComisionesScreen(citas: List<Cita>, onVolver: () -> Unit) {
    // Solo cuentan las citas que ya se finalizaron
    val finalizadas = citas.filter { it.estatus == "Finalizada" }

    // Mapas que se van llenando: nombre de la empleada -> total
    val ventas = mutableMapOf<String, Double>()
    val propinas = mutableMapOf<String, Double>()
    val atendidas = mutableMapOf<String, Int>()

    finalizadas.forEach { cita ->
        // "Karen, Ana" -> ["Karen", "Ana"]
        val empleadas = cita.estilista.split(", ").filter { it.isNotBlank() }
        if (empleadas.isNotEmpty()) {
            empleadas.forEach { nombre ->
                // El precio y la propina se reparten en partes iguales entre las que atendieron
                ventas[nombre] = (ventas[nombre] ?: 0.0) + cita.precioFinal / empleadas.size
                propinas[nombre] = (propinas[nombre] ?: 0.0) + cita.propina / empleadas.size
                atendidas[nombre] = (atendidas[nombre] ?: 0) + 1
            }
        }
    }
    val nombres = ventas.keys.sorted()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comisiones") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (nombres.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay citas finalizadas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(nombres) { nombre ->
                    val totalVentas = ventas[nombre] ?: 0.0
                    val comision = totalVentas * PORCENTAJE_COMISION

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(nombre, style = MaterialTheme.typography.titleMedium)
                            Text("Citas atendidas: ${atendidas[nombre] ?: 0}")
                            Text("Ventas: $${"%.2f".format(totalVentas)}")
                            Text(
                                "Comisión (${(PORCENTAJE_COMISION * 100).toInt()}%): $${"%.2f".format(comision)}",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text("Propinas: $${"%.2f".format(propinas[nombre] ?: 0.0)}")
                        }
                    }
                }
            }
        }
    }
}