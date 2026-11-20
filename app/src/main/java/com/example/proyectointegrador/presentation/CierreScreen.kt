package com.example.proyectointegrador.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectointegrador.data.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CierreScreen(cita: Cita, onConfirmar: (Cita) -> Unit) {
    val context = LocalContext.current

    // Si la cita ya tenía montos, se muestran; si no, los campos arrancan vacíos
    var precio by remember {
        mutableStateOf(if (cita.precioFinal > 0) cita.precioFinal.toString() else "")
    }
    var propina by remember {
        mutableStateOf(if (cita.propina > 0) cita.propina.toString() else "")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cerrar cita") },
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
            // Datos de la cita, solo para consultar
            Text(cita.nombreClienta, style = MaterialTheme.typography.titleLarge)
            Text("Servicios: ${cita.servicio}")
            Text("Atendió: ${cita.estilista}")
            if (cita.anticipo) {
                Text("Anticipo recibido: $${cita.montoAnticipo}")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio final") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = propina,
                onValueChange = { propina = it },
                label = { Text("Propina (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val precioNumero = precio.toDoubleOrNull()
                    // Si la propina se deja vacía se toma como 0
                    val propinaNumero = if (propina.isBlank()) 0.0 else propina.toDoubleOrNull()
                    when {
                        precioNumero == null || precioNumero <= 0 ->
                            Toast.makeText(context, "Escribe un precio final válido", Toast.LENGTH_SHORT).show()
                        propinaNumero == null ->
                            Toast.makeText(context, "Escribe una propina válida", Toast.LENGTH_SHORT).show()
                        else -> onConfirmar(
                            cita.copy(
                                precioFinal = precioNumero,
                                propina = propinaNumero,
                                estatus = "Finalizada"
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar cita")
            }
        }
    }
}