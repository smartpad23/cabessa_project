package com.smartpad.cabessa.modules.drugs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Drugs module home screen (distinct from app-level home).
 * For now this can show a summary or quick actions. We'll keep it simple.
 */
@Composable
fun DrugsModuleHomeScreen(navController: androidx.navigation.NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Drugs Module Home")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Quick access to medications and module features.")
        }
    }
}