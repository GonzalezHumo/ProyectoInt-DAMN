package com.example.proyectointegrador.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectointegrador.data.Cita
import com.example.proyectointegrador.viewmodel.CitaViewModel
import androidx.compose.material3.Button

private val estatusOpciones = listOf("Pendiente", "Confirmada", "Finalizada", "Cancelada")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(viewModel: CitaViewModel, onAgregar: () -> Unit, onEditar: () -> Unit, onCerrar: () -> Unit, onProductos: () -> Unit) {    val citas by viewModel.citas.collectAsState(initial = emptyList())

    var menuFiltro by remember { mutableStateOf(false) }
    var filtro by remember { mutableStateOf("Todas") }

    // Se filtra la lista que viene de Room, igual que en MiniProyecto02
    val citasVisibles = if (filtro == "Todas") citas else citas.filter { it.estatus == filtro }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (filtro == "Todas") "RAM Beauty" else "RAM Beauty - $filtro")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = onProductos) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Productos")
                    }
                    IconButton(onClick = { menuFiltro = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Filtrar")
                    }
                    DropdownMenu(
                        expanded = menuFiltro,
                        onDismissRequest = { menuFiltro = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver todas") },
                            onClick = { filtro = "Todas"; menuFiltro = false }
                        )
                        estatusOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text("Ver ${opcion.lowercase()}s") },
                                onClick = { filtro = opcion; menuFiltro = false }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, contentDescription = "Nueva cita")
            }
        }
    ) { paddingValues ->
        if (citasVisibles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (filtro == "Todas") "Aún no hay citas. ¡Agrega una!"
                    else "No hay citas con estatus $filtro"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(citasVisibles, key = { it.id }) { cita ->
                    CitaItem(
                        cita = cita,
                        onCambiarEstatus = { viewModel.cambiarEstatus(cita, it) },
                        onEditar = {
                            viewModel.citaEnEdicion = cita
                            onEditar()
                        },
                        onCerrar = {
                            viewModel.citaEnEdicion = cita
                            onCerrar()
                        },

                        onEliminar = { viewModel.eliminar(cita) }
                    )
                }
            }
        }
    }
}

@Composable
fun CitaItem(
    cita: Cita,
    onCambiarEstatus: (String) -> Unit,
    onEditar: () -> Unit,
    onCerrar: () -> Unit,
    onEliminar: () -> Unit

) {
    var menuEstatus by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cita.nombreClienta, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${cita.fecha} - ${cita.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Servicios: ${cita.servicio}")
                Text("Atiende: ${cita.estilista}")
                Text("Celular: ${cita.celular}")

                // Al tocar el estatus se abre el menú para cambiarlo
                Box {
                    Text(
                        "Estatus: ${cita.estatus} (toca para cambiar)",
                        modifier = Modifier.clickable { menuEstatus = true }
                    )
                    DropdownMenu(
                        expanded = menuEstatus,
                        onDismissRequest = { menuEstatus = false }
                    ) {
                        estatusOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    onCambiarEstatus(opcion)
                                    menuEstatus = false
                                }
                            )
                        }
                    }
                }
                // Solo se puede cerrar una cita que sigue abierta
                if (cita.estatus != "Finalizada" && cita.estatus != "Cancelada") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(onClick = onCerrar) {
                        Text("Cerrar cita")
                    }
                }
                // Resumen de cobro: solo cuando la cita ya está finalizada
                if (cita.estatus == "Finalizada") {
                    val descuento = if (cita.anticipo) cita.montoAnticipo else 0.0
                    val total = cita.precioFinal - descuento + cita.propina

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Precio final: $${"%.2f".format(cita.precioFinal)}")
                    if (cita.anticipo) {
                        Text("Anticipo descontado: -$${"%.2f".format(descuento)}")
                    }
                    Text("Propina: $${"%.2f".format(cita.propina)}")
                    Text(
                        "Total a cobrar: $${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar cita")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar cita")
            }
        }
    }
}