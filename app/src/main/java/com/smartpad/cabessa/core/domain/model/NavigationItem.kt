package com.smartpad.cabessa.core.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * NavigationItem represents a destination inside a module (not a top-level module route).
 * route: the sub-route name (e.g., "treatments"). Full route = "${module.route}/${item.route}"
 */
data class NavigationItem(
    val labelRes: Int,
    val icon: ImageVector,
    val route: String
)