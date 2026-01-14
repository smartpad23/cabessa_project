package com.smartpad.cabessa.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartpad.cabessa.R
import com.smartpad.cabessa.core.domain.model.AppModule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun GlobalTopAppBar(
//    currentModule: AppModule,
//    onMenuClick: () -> Unit,
//    onModuleSwitch: (AppModule) -> Unit
//) {
//    // Control visibility of the dropdown menu
//    var showModuleMenu by remember { mutableStateOf(false) }
//
//    CenterAlignedTopAppBar(
//        navigationIcon = {
//            IconButton(onClick = onMenuClick) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_menu), // fallback; replace with your menu icon resource if desired
//                    contentDescription = stringResource(R.string.ok) // replace with "Open menu" string if available
//                )
//            }
//        },
//        title = {
//            // Center title shows the MODULE name and icon (not the app name).
//            Surface(
//                onClick = { showModuleMenu = true },
//                color = currentModule.color.copy(alpha = 0.12f),
//                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
//                border = BorderStroke(1.dp, currentModule.color.copy(alpha = 0.4f)),
//                modifier = Modifier
//            ) {
//                Row(
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Module icon
//                    val iconPainter = if (currentModule.iconRes != 0) {
//                        // painterResource is a @Composable function; call it directly
//                        painterResource(id = currentModule.iconRes)
//                    } else {
//                        // fallback to a platform icon (resource guaranteed to exist)
//                        painterResource(id = android.R.drawable.sym_def_app_icon)
//                    }
//
//                    Icon(
//                        painter = iconPainter,
//                        contentDescription = stringResource(currentModule.nameRes),
//                        modifier = Modifier.size(18.dp),
//                        tint = currentModule.color
//                    )
//
//                    Spacer(modifier = Modifier.size(8.dp))
//
//                    // Module name (uses module's nameRes so it's translated and updates on module change)
//                    Text(
//                        text = stringResource(currentModule.nameRes),
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }

//            // Dropdown menu to switch modules
//            DropdownMenu(
//                expanded = showModuleMenu,
//                onDismissRequest = { showModuleMenu = false }
//            ) {
//                val modules = listOf(AppModule.Drugs, AppModule.Money)
//                modules.forEach { module ->
//                    DropdownMenuItem(
//                        text = { Text(stringResource(module.nameRes)) },
//                        leadingIcon = {
//                            Icon(
//                                painter = painterResource(id = module.iconRes),
//                                contentDescription = null,
//                                modifier = Modifier.size(20.dp),
//                                tint = module.color
//                            )
//                        },
//                        onClick = {
//                            onModuleSwitch(module)
//                            showModuleMenu = false
//                        }
//                    )
//                }
//            }
//        },
//        actions = {
//            IconButton(onClick = { /* navigate to profile or settings */ }) {
//                Icon(
//                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
//                    contentDescription = "User"
//                )
//            }
//        }
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalTopAppBar(
    onMenuClick: () -> Unit,
) {

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.menu),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            // Center title shows the MODULE name and icon (not the app name).
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)),
                modifier = Modifier
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_drugs),
                        contentDescription = stringResource(R.string.drugs),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = stringResource(R.string.drugs),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /* navigate to profile or settings */ }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    )
}

