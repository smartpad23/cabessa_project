package com.smartpad.cabessa.core.ui.components

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smartpad.cabessa.core.domain.model.AppModule
import com.smartpad.cabessa.core.navigation.AppNavigationHost
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.smartpad.cabessa.core.domain.model.AppModule.Drugs.route
import com.smartpad.cabessa.core.navigation.Routes

/**
 * Main scaffold that wires top app bar, drawer and optional bottom bar (module shortcuts).
 *
 * Technical basis:
 * - Uses currentBackStackEntryAsState to determine currentRoute and currentModule dynamically.
 * - Shows DynamicBottomNavigation only when current module provides bottomNavItems (e.g., Drugs).
 * - Drawer content is module-aware and receives navController to reflect selected item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Observe current backstack entry to determine currentRoute
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    // Determine module by route prefix. If we are on the app-level "home" we should still show Drugs selected visually.
    val currentModule = when {
        currentRoute.startsWith("drugs_root") -> AppModule.Drugs
        currentRoute.startsWith("money_root") -> AppModule.Money
        currentRoute == Routes.HOME -> AppModule.Drugs // Default selected module visually
        else -> AppModule.Drugs
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Pass a lambda that performs the navigation with safety checks and fallback
            DynamicDrawerContent(currentModule, navController) { route ->
                scope.launch {
                    // Close drawer first to improve UX; navigation after closing avoids overlapping animations
                    try {
                        drawerState.close()
                    } catch (t: Throwable) {
                        // ignore
                    }

                    // Then attempt navigation
                    try {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    } catch (ex: Exception) {
                        Log.e("MainAppScaffold", "Navigation to $route failed: ${ex.message}")
                        // Fallback: navigate to module home if possible
                        val fallback = when (currentModule) {
                            is AppModule.Drugs -> Routes.DRUGS_HOME
                            is AppModule.Money -> Routes.MONEY_HOME
                            else -> Routes.HOME
                        }
                        try {
                            navController.navigate(fallback) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        } catch (secondEx: Exception) {
                            Log.e("MainAppScaffold", "Fallback navigation to $fallback also failed: ${secondEx.message}")
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                GlobalTopAppBar(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )


//                GlobalTopAppBar(
//                    currentModule = currentModule,
//                    onMenuClick = { scope.launch { drawerState.open() } },
//                    onModuleSwitch = { selectedModule ->
//                        // When switching modules via center selector: navigate to module home using Routes constants
//                        val target = when (selectedModule) {
//                            is AppModule.Drugs -> Routes.DRUGS_HOME
//                            is AppModule.Money -> Routes.MONEY_HOME
//                            else -> Routes.DRUGS_HOME
//                        }
//                        navController.navigate(target) {
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    }
//                )
            },
            bottomBar = {
                if (currentModule.bottomNavItems.isNotEmpty() && currentRoute.startsWith(currentModule.route)) {
                    DynamicBottomNavigation(currentModule = currentModule, navController = navController)
                }
            }
        ) { padding ->
            androidx.compose.foundation.layout.Box(modifier = Modifier.padding(padding)) {
                AppNavigationHost(navController = navController)
            }
        }
    }
}