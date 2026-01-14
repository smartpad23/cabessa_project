package com.smartpad.cabessa.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.smartpad.cabessa.R

// Definición de colores por módulo para identificar visualmente dónde está el usuario
sealed class AppModule(val name: Int, val color: Color, val icon: Int) {
    object Drugs : AppModule(R.string.drug_module_name, Color(0xFFC8B6FF), R.drawable.ic_drugs)
    object Money : AppModule(R.string.money_module_name, Color(0xFFFFB997), R.drawable.ic_money)
    object Notes : AppModule(R.string.notes_module_name, Color(0xFF2196F3), R.drawable.ic_notes)
}