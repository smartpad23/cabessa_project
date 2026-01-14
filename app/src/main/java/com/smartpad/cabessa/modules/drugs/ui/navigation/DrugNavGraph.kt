package com.smartpad.cabessa.modules.drugs.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.smartpad.cabessa.core.navigation.Routes
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugDosesScreen
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugListScreen
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugStatsScreen
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugStockScreen
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugTreatmentsScreen
import com.smartpad.cabessa.modules.drugs.ui.screens.DrugsModuleHomeScreen

/**
 * drugGraph registers all routes for the Drugs module under the "drugs_root" root.
 *
 * Routes:
 * - drugs_root/home
 * - drugs_root/drugs
 * - drugs_root/treatments
 * - drugs_root/stock
 * - drugs_root/doses
 * - drugs_root/stats
 *
 * Technical basis:
 * - Use full route strings so that DynamicBottomNavigation can navigate to them.
 */

fun NavGraphBuilder.drugGraph(navController: NavHostController) {
    composable(Routes.DRUGS_HOME) {
        DrugsModuleHomeScreen(navController)
    }
    composable(Routes.DRUGS_LIST) {
        DrugListScreen(navController = navController)
    }
    composable(Routes.DRUGS_TREATMENTS) {
        DrugTreatmentsScreen()
    }
    composable(Routes.DRUGS_STOCK) {
        DrugStockScreen()
    }
    composable(Routes.DRUGS_DOSES) {
        DrugDosesScreen()
    }
    composable(Routes.DRUGS_STATS) {
        DrugStatsScreen()
    }
}