package com.smartpad.cabessa.core.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.smartpad.cabessa.core.domain.model.AppModule

/**
 * DynamicBottomNavigation creates a Bottom Navigation bar based on the current module.
 *
 * Technical basis:
 * - bottom nav items are module-specific and navigates to "${module.route}/${item.route}".
 * - Uses NavController.currentBackStackEntryAsState to determine the selected route.
 */
@Composable
fun DynamicBottomNavigation(
    currentModule: AppModule,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        currentModule.bottomNavItems.forEach { item ->
            val fullRoute = "${currentModule.route}/${item.route}"
            val selected = currentRoute == fullRoute
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(fullRoute) {
                        launchSingleTop = true
                        restoreState = true
                        // stay within module stack: do not pop to root automatically to preserve local state
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (selected) currentModule.color else Color.Unspecified
                    )
                },
                label = { Text(stringResource(item.labelRes)) }
            )
        }
    }
}