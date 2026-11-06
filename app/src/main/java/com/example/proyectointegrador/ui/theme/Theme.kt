package com.example.proyectointegrador.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaColores = lightColorScheme(
    primary = Turquesa,
    onPrimary = Negro,
    primaryContainer = Turquesa,
    onPrimaryContainer = Negro,
    secondary = Negro,
    onSecondary = Blanco,
    background = Blanco,
    onBackground = Negro,
    surface = Blanco,
    onSurface = Negro,
    surfaceContainerHighest = Blanco
)

@Composable
fun ProyectoIntegradorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaColores,
        typography = Typography,
        content = content
    )
}