package com.example.proyectointegrador.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

private val categorias = listOf("Shampoo/Acondicionador", "Accesorio", "Snack")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(productoEnEdicion: Producto?, onGuardar: (Producto) -> Unit) {
    val context = LocalContext.current

    // Si hay un producto en edición, los campos arrancan con sus datos; si no, vacíos
    var nombre by remember { mutableStateOf(productoEnEdicion?.nombre ?: "") }
    var categoria by remember { mutableStateOf(productoEnEdicion?.categoria ?: "") }
    var precio by remember { mutableStateOf(productoEnEdicion?.precioVenta?.toString() ?: "") }
    var costo by remember { mutableStateOf(productoEnEdicion?.costo?.toString() ?: "") }
    var existencias by remember { mutableStateOf(productoEnEdicion?.existencias?.toString() ?: "") }
    var minimo by remember { mutableStateOf(productoEnEdicion?.stockMinimo?.toString() ?: "5") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productoEnEdicion == null) "Nuevo producto" else "Editar producto") },
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
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomSpinner(
                etiqueta = "Categoría",
                opciones = categorias,
                seleccionado = categoria,
                onSeleccion = { categoria = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio de venta") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = costo,
                onValueChange = { costo = it },
                label = { Text("Costo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = existencias,
                onValueChange = { existencias = it },
                label = { Text("Existencias (piezas)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = minimo,
                onValueChange = { minimo = it },
                label = { Text("Stock mínimo (para avisar)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val precioNumero = precio.toDoubleOrNull()
                    val costoNumero = costo.toDoubleOrNull()
                    val existenciasNumero = existencias.toIntOrNull()
                    val minimoNumero = minimo.toIntOrNull()
                    when {
                        nombre.isBlank() ->
                            Toast.makeText(context, "Escribe el nombre", Toast.LENGTH_SHORT).show()
                        categoria.isBlank() ->
                            Toast.makeText(context, "Elige la categoría", Toast.LENGTH_SHORT).show()
                        precioNumero == null || precioNumero < 0 ->
                            Toast.makeText(context, "Escribe un precio válido", Toast.LENGTH_SHORT).show()
                        costoNumero == null || costoNumero < 0 ->
                            Toast.makeText(context, "Escribe un costo válido", Toast.LENGTH_SHORT).show()
                        existenciasNumero == null || existenciasNumero < 0 ->
                            Toast.makeText(context, "Escribe las existencias (número entero)", Toast.LENGTH_SHORT).show()
                        minimoNumero == null || minimoNumero < 0 ->
                            Toast.makeText(context, "Escribe un stock mínimo válido", Toast.LENGTH_SHORT).show()
                        else -> onGuardar(
                            Producto(
                                // Si se edita, se conserva el id para que Room actualice el mismo producto
                                id = productoEnEdicion?.id ?: 0,
                                nombre = nombre,
                                categoria = categoria,
                                precioVenta = precioNumero,
                                costo = costoNumero,
                                existencias = existenciasNumero,
                                stockMinimo = minimoNumero
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productoEnEdicion == null) "Guardar producto" else "Guardar cambios")
            }
        }
    }
}