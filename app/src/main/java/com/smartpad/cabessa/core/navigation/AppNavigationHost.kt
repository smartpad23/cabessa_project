package com.smartpad.cabessa.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.smartpad.cabessa.modules.drugs.ui.navigation.drugGraph
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import com.smartpad.cabessa.core.ui.components.HomeScreen
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugsModuleHomeScreen

/**
 * AppNavigationHost
 *
 * - startDestination is "home" (app-level home / calendar placeholder).
 * - Each module registers a navigation graph rooted at "<module>_root".
 *
 * Note: Box comes from androidx.compose.foundation.layout.Box (not material3).
 */

@Composable
fun AppNavigationHost(navController: NavHostController) {
    //Si se quisiera una pantalla general de la app se podría empezar por:
//    NavHost(navController = navController, startDestination = Routes.HOME) {
//        // App home (calendar placeholder)
//        composable(Routes.HOME) {
//            HomeScreen()
//        }


    NavHost(navController = navController, startDestination = Routes.DRUGS_HOME) {
        // App home (calendar placeholder)
        composable(Routes.DRUGS_HOME) {
            DrugsModuleHomeScreen(navController)
        }

        // Drugs module graph rooted at Routes.DRUGS_ROOT with start = Routes.DRUGS_HOME
        navigation(startDestination = Routes.DRUGS_HOME, route = Routes.DRUGS_ROOT) {
            // Register all drugs subroutes
            drugGraph(navController)
        }

//        PARA FUTURAS APLICACIONES. ESTAS SON LAS OPCIONES

//        // Money module (simple example)
//        navigation(startDestination = Routes.MONEY_HOME, route = Routes.MONEY_ROOT) {
//            composable(Routes.MONEY_HOME) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Money Home (placeholder)", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//            composable(Routes.MONEY_INCOME) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Income (placeholder)", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//            composable(Routes.MONEY_EXPENSES) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Expenses (placeholder)", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//            composable(Routes.MONEY_REPORTS) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Reports (placeholder)", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//        }
    }
}