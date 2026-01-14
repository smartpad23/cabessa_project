package com.smartpad.cabessa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smartpad.cabessa.core.ui.components.MainAppScaffold
import com.smartpad.cabessa.core.ui.theme.CabessaTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application.
 * Technical Basis: Single Activity Architecture. This activity hosts the Global Scaffold
 * and delegates all navigation and UI logic to Compose.
 */
@AndroidEntryPoint // Requerido para que Hilt pueda inyectar los ViewModels en las pantallas
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Technical Basis: Habilita el diseño de borde a borde (detrás de las barras de sistema)
        // Es la práctica recomendada por Google para Android 15+ y aplicaciones en 2026.
        enableEdgeToEdge()

        setContent {
            CabessaTheme {
                // Invocamos el Scaffold principal que orquesta los módulos (Drugs, Money, etc.)
                // No necesitamos pasarle parámetros aquí, ya que el estado se gestiona internamente
                MainAppScaffold()
            }
        }
    }
}