package com.smartpad.cabessa.core.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color

import com.smartpad.cabessa.R

/**
 * AppModule describes a top-level module of the app.
 *
 * Technical choices:
 * - Each module has a unique root route (e.g., "drugs_root").
 * - bottomNavItems contain sub-route names (not full routes) so they are resolved
 *   relative to the module root when navigating from the DynamicBottomNavigation.
 */
sealed class AppModule(
    val nameRes: Int,
    val color: Color,
    val iconRes: Int,
    val route: String,
    val bottomNavItems: List<NavigationItem>
) {
    object Drugs : AppModule(
        nameRes = R.string.drug_module_name,
        color = Color(0xFF4CAF50),
        iconRes = R.drawable.ic_drugs,
        route = "drugs_root",
        bottomNavItems = listOf(
//            NavigationItem(R.string.drugs, Icons.Default.Animation, "drugs"),
            NavigationItem(R.string.treatments, Icons.Default.Assignment, "treatments"),
            NavigationItem(R.string.stock, Icons.Default.Inventory2, "stock"),
            NavigationItem(R.string.doses, Icons.Default.Medication, "doses"),
//            NavigationItem(R.string.stats, Icons.Default.BarChart, "stats")
        )
    )

    object Money : AppModule(
        nameRes = R.string.money_module_name,
        color = Color(0xFFE91E63),
        iconRes = R.drawable.ic_money,
        route = "money_root",
        bottomNavItems = listOf(
            NavigationItem(R.string.incomes, Icons.Default.Money, "income"),
            NavigationItem(R.string.expenses, Icons.Default.Inventory2, "expenses"),
            NavigationItem(R.string.reports, Icons.Default.BarChart, "reports")
        )
    )
}