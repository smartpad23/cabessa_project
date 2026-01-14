package com.smartpad.cabessa.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme

/**
 * HomeScreen (calendar placeholder)
 *
 * Technical basis:
 * - Placeholder for the calendar/main screen. For now it displays the application name,
 *   occupying the main area. Later this will contain the monthly calendar and the daily pop-ups.
 * - Keeps layout simple and follows Material 3 typography.
 */
@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = androidx.compose.ui.res.stringResource(id = com.smartpad.cabessa.R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
    }
}