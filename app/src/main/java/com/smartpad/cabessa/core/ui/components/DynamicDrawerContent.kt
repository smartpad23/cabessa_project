package com.smartpad.cabessa.core.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smartpad.cabessa.R
import com.smartpad.cabessa.core.domain.model.AppModule
import com.smartpad.cabessa.core.domain.model.DrawerItem
import com.smartpad.cabessa.core.navigation.Routes

/**
 * Drawer content adapts to the current module.
 *
 * Technical choices:
 * - Uses NavigationDrawerItem which exposes `selected` and `onClick`.
 * - Accepts navController so that the drawer can reflect the selected item based on the current back stack.
 */

@Composable
fun DynamicDrawerContent(
    currentModule: AppModule,
    navController: NavHostController,
    onNavigate: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    ModalDrawerSheet(
        modifier = Modifier.widthIn(max = 220.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp)
        ) {
            // Header: module name (module-aware)
            Text(
                text = stringResource(id = currentModule.nameRes),
                modifier = Modifier.padding(18.dp),
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Divider()

            // Always show app Home
            NavigationDrawerItem(
                label = { Text(stringResource(id = com.smartpad.cabessa.R.string.home)) },
                selected = currentRoute == Routes.HOME,
                onClick = { onNavigate(Routes.HOME) },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = NavigationDrawerItemDefaults.colors(),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            Divider()

            val items = listOf(
                DrawerItem(
                    route = Routes.DRUGS_LIST,
                    label = stringResource(id = R.string.drug_module_name),
                    icon = Icons.Default.MedicalServices
                ),
                DrawerItem(
                    route = Routes.DRUGS_TREATMENTS,
                    label = stringResource(id = R.string.treatments),
                    icon = Icons.Default.Assignment
                ),
                DrawerItem(
                    route = Routes.DRUGS_STOCK,
                    label = stringResource(id = R.string.stock),
                    icon = Icons.Default.Inventory2
                ),
                DrawerItem(
                    route = Routes.DRUGS_DOSES,
                    label = stringResource(id = R.string.doses),
                    icon = Icons.Default.Medication
                ),
                DrawerItem(
                    route = Routes.DRUGS_STATS,
                    label = stringResource(id = R.string.stats),
                    icon = Icons.Default.BarChart
                )
            )

            items.forEach { (route, label, icon) ->
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = { Text(label) },
                    selected = currentRoute == route,
                    onClick = {
                        // Call parent-provided navigation handler. Parent will perform navController.navigate(...)
                        onNavigate(route)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    colors = NavigationDrawerItemDefaults.colors()
                )
            }




            // Module specific items
//            when (currentModule) {
//                is AppModule.Drugs -> {
//                    val items = listOf(
//                        Routes.DRUGS_LIST to androidx.compose.ui.res.stringResource(id = R.string.drug_module_name),
//                        Routes.DRUGS_TREATMENTS to androidx.compose.ui.res.stringResource(id = R.string.treatments),
//                        Routes.DRUGS_STOCK to androidx.compose.ui.res.stringResource(id = R.string.stock),
//                        Routes.DRUGS_DOSES to androidx.compose.ui.res.stringResource(id = R.string.doses),
//                        Routes.DRUGS_STATS to androidx.compose.ui.res.stringResource(id = R.string.stats)
//                    )
//
//                    items.forEach { (route, label) ->
//                        NavigationDrawerItem(
//                            label = { Text(label) },
//                            selected = currentRoute == route,
//                            onClick = {
//                                val routeExists = try {
//                                    navController.graph.findNode(route) != null
//                                } catch (t: Throwable) {
//                                    false
//                                }
//
//                                if (routeExists) {
//                                    try {
//                                        onNavigate(route)
//                                    } catch (ex: Exception) {
//                                        Log.e("DrawerNav", "Navigate to $route failed: ${ex.message}")
//                                        onNavigate(Routes.DRUGS_HOME)
//                                    }
//                                } else {
//                                    Log.w("DrawerNav", "Route $route not found in graph - navigating to ${Routes.DRUGS_HOME}")
//                                    onNavigate(Routes.DRUGS_HOME)
//                                }
//                            },
//                            modifier = Modifier.padding(horizontal = 8.dp),
//                            colors = NavigationDrawerItemDefaults.colors()
//                        )
//                    }
//                }

//                is AppModule.Money -> {
//                    val items = listOf(
//                        Routes.MONEY_HOME to androidx.compose.ui.res.stringResource(id = com.smartpad.cabessa.R.string.money_module_name),
//                        Routes.MONEY_INCOME to androidx.compose.ui.res.stringResource(id = com.smartpad.cabessa.R.string.incomes),
//                        Routes.MONEY_EXPENSES to androidx.compose.ui.res.stringResource(id = com.smartpad.cabessa.R.string.expenses),
//                        Routes.MONEY_REPORTS to androidx.compose.ui.res.stringResource(id = com.smartpad.cabessa.R.string.reports)
//                    )
//
//                    items.forEach { (route, label) ->
//                        NavigationDrawerItem(
//                            label = { Text(label) },
//                            selected = currentRoute == route,
//                            onClick = {
//                                val routeExists = try {
//                                    navController.graph.findNode(route) != null
//                                } catch (t: Throwable) {
//                                    false
//                                }
//
//                                if (routeExists) {
//                                    try {
//                                        onNavigate(route)
//                                    } catch (ex: Exception) {
//                                        Log.e("DrawerNav", "Navigate to $route failed: ${ex.message}")
//                                        onNavigate(Routes.MONEY_HOME)
//                                    }
//                                } else {
//                                    Log.w("DrawerNav", "Route $route not found in graph - navigating to ${Routes.MONEY_HOME}")
//                                    onNavigate(Routes.MONEY_HOME)
//                                }
//                            },
//                            modifier = Modifier.padding(horizontal = 8.dp),
//                            colors = NavigationDrawerItemDefaults.colors()
//                        )
//                    }
//                }
//            }
        }
    }
}