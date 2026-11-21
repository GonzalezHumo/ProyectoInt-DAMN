package com.example.proyectointegrador.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectointegrador.data.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComprobanteScreen(cita: Cita, onVolver: () -> Unit) {
    // Mismo cálculo que el resumen de cobro de la tarjeta
    val descuento = if (cita.anticipo) cita.montoAnticipo else 0.0
    val total = cita.precioFinal - descuento + cita.propina

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comprobante") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("RAM Beauty", style = MaterialTheme.typography.headlineSmall)
                    Text("Comprobante de servicio", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Folio: ${cita.id}")
                    Text("Fecha: ${cita.fecha} - ${cita.hora}")
                    Text("Clienta: ${cita.nombreClienta}")
                    Text("Servicios: ${cita.servicio}")
                    Text("Atendió: ${cita.estilista}")
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Precio final: $${"%.2f".format(cita.precioFinal)}")
                    if (cita.anticipo) {
                        Text("Anticipo descontado: -$${"%.2f".format(descuento)}")
                    }
                    Text("Propina: $${"%.2f".format(cita.propina)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total pagado: $${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("¡Gracias por tu visita!", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Regresar")
            }
        }
    }
}