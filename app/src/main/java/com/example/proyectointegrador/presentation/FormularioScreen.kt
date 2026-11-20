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
import com.example.proyectointegrador.data.Cita

private val servicios = listOf("Uñas", "Maquillaje", "Peinado", "Tinte", "Tratamiento", "Keratina")

// Horarios cada media hora, de 09:00 a 19:30
private val horarios = (9..19).flatMap { h ->
    listOf("%02d:00".format(h), "%02d:30".format(h))
}

private val maquillistas = listOf("Ana", "Fernanda Cruz", "Adriana")
private val estilistas = listOf("Rocio", "Alba", "Karen", "Ana")
private val manicuristas = listOf("Karen", "Fernanda Olgin")

private val empleadasPorServicio = mapOf(
    "Uñas" to manicuristas,
    "Maquillaje" to maquillistas,
    "Peinado" to estilistas,
    "Tinte" to estilistas,
    "Tratamiento" to estilistas,
    "Keratina" to estilistas
)

private val todasLasEmpleadas = empleadasPorServicio.values.flatten().distinct().sorted()

private fun empleadasDisponibles(serviciosElegidos: List<String>): List<String> {
    if (serviciosElegidos.isEmpty()) return todasLasEmpleadas
    return serviciosElegidos
        .flatMap { servicio -> empleadasPorServicio[servicio] ?: emptyList() }
        .distinct()
        .sorted()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(citaEnEdicion: Cita?, onGuardar: (Cita) -> Unit) {
    val context = LocalContext.current

    // Si hay una cita en edición, los campos arrancan con sus datos; si no, vacíos
    var nombre by remember { mutableStateOf(citaEnEdicion?.nombreClienta ?: "") }
    var celular by remember { mutableStateOf(citaEnEdicion?.celular ?: "") }
    var serviciosElegidos by remember {
        mutableStateOf(citaEnEdicion?.servicio?.split(", ")?.filter { it.isNotBlank() } ?: emptyList())
    }
    var empleadasElegidas by remember {
        mutableStateOf(citaEnEdicion?.estilista?.split(", ")?.filter { it.isNotBlank() } ?: emptyList())
    }
    var fecha by remember { mutableStateOf(citaEnEdicion?.fecha ?: "") }
    var hora by remember { mutableStateOf(citaEnEdicion?.hora ?: "") }
    var anticipo by remember { mutableStateOf(citaEnEdicion?.anticipo ?: false) }
    var monto by remember {
        mutableStateOf(if (citaEnEdicion != null && citaEnEdicion.anticipo) citaEnEdicion.montoAnticipo.toString() else "")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (citaEnEdicion == null) "Nueva cita" else "Editar cita") },
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
                label = { Text("Nombre de la clienta") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = celular,
                onValueChange = { celular = it },
                label = { Text("Celular") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomSpinnerMultiple(
                etiqueta = "Servicios",
                opciones = servicios,
                seleccionados = serviciosElegidos,
                onCambio = { nuevos ->
                    serviciosElegidos = nuevos
                    // Se quitan las empleadas que ya no corresponden a los servicios elegidos
                    empleadasElegidas = empleadasElegidas.filter { it in empleadasDisponibles(nuevos) }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomSpinnerMultiple(
                etiqueta = "Atiende",
                opciones = empleadasDisponibles(serviciosElegidos),
                seleccionados = empleadasElegidas,
                onCambio = { empleadasElegidas = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomDatePicker(
                fecha = fecha,
                onFechaSeleccionada = { fecha = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomSpinner(
                etiqueta = "Hora",
                opciones = horarios,
                seleccionado = hora,
                onSeleccion = { hora = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomSwitch(
                texto = "Dejó anticipo",
                checked = anticipo,
                onCheckedChange = { anticipo = it }
            )
            if (anticipo) {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto del anticipo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val montoNumero = monto.toDoubleOrNull()
                    when {
                        nombre.isBlank() -> Toast.makeText(context, "Escribe el nombre", Toast.LENGTH_SHORT).show()
                        celular.isBlank() -> Toast.makeText(context, "Escribe el celular", Toast.LENGTH_SHORT).show()
                        serviciosElegidos.isEmpty() -> Toast.makeText(context, "Elige al menos un servicio", Toast.LENGTH_SHORT).show()
                        empleadasElegidas.isEmpty() -> Toast.makeText(context, "Elige quién atiende", Toast.LENGTH_SHORT).show()
                        fecha.isBlank() -> Toast.makeText(context, "Elige la fecha", Toast.LENGTH_SHORT).show()
                        hora.isBlank() -> Toast.makeText(context, "Elige la hora", Toast.LENGTH_SHORT).show()
                        anticipo && montoNumero == null -> Toast.makeText(context, "Escribe un monto válido", Toast.LENGTH_SHORT).show()
                        else -> onGuardar(
                            Cita(
                                // Si se edita, se conservan el id, el estatus y el precio final
                                id = citaEnEdicion?.id ?: 0,
                                nombreClienta = nombre,
                                celular = celular,
                                servicio = serviciosElegidos.joinToString(", "),
                                estilista = empleadasElegidas.joinToString(", "),
                                fecha = fecha,
                                hora = hora,
                                anticipo = anticipo,
                                montoAnticipo = if (anticipo) montoNumero ?: 0.0 else 0.0,
                                estatus = citaEnEdicion?.estatus ?: "Pendiente",
                                precioFinal = citaEnEdicion?.precioFinal ?: 0.0,
                                propina = citaEnEdicion?.propina ?: 0.0
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (citaEnEdicion == null) "Guardar cita" else "Guardar cambios")
            }
        }
    }
}