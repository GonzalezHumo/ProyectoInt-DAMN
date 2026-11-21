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
import com.example.proyectointegrador.data.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientoScreen(producto: Producto, esEntrada: Boolean, onConfirmar: (Producto) -> Unit) {
    val context = LocalContext.current

    var cantidad by remember { mutableStateOf("") }
    // En una entrada el costo arranca con el costo actual del producto
    var costo by remember { mutableStateOf(producto.costo.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEntrada) "Entrada de mercancía" else "Venta") },
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
            Text(producto.nombre, style = MaterialTheme.typography.titleLarge)
            Text("Existencias actuales: ${producto.existencias}")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad de piezas") },
                modifier = Modifier.fillMaxWidth()
            )

            if (esEntrada) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = costo,
                    onValueChange = { costo = it },
                    label = { Text("Costo por pieza") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // En una venta se muestra cuánto se cobra, según el precio del producto
                val total = (cantidad.toIntOrNull() ?: 0) * producto.precioVenta
                Spacer(modifier = Modifier.height(8.dp))
                Text("Total de la venta: $${"%.2f".format(total)}")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val cantidadNumero = cantidad.toIntOrNull()
                    val costoNumero = costo.toDoubleOrNull()
                    when {
                        cantidadNumero == null || cantidadNumero <= 0 ->
                            Toast.makeText(context, "Escribe una cantidad válida", Toast.LENGTH_SHORT).show()
                        !esEntrada && cantidadNumero > producto.existencias ->
                            Toast.makeText(context, "Solo hay ${producto.existencias} piezas", Toast.LENGTH_SHORT).show()
                        esEntrada && (costoNumero == null || costoNumero < 0) ->
                            Toast.makeText(context, "Escribe un costo válido", Toast.LENGTH_SHORT).show()
                        esEntrada -> onConfirmar(
                            producto.copy(
                                existencias = producto.existencias + cantidadNumero,
                                costo = costoNumero ?: producto.costo
                            )
                        )
                        else -> onConfirmar(
                            producto.copy(existencias = producto.existencias - cantidadNumero)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (esEntrada) "Registrar entrada" else "Registrar venta")
            }
        }
    }
}