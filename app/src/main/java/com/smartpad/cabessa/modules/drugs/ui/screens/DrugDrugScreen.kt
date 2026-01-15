package com.smartpad.cabessa.modules.drugs.ui.screens

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.smartpad.cabessa.core.database.AppDatabase
import com.smartpad.cabessa.modules.drugs.data.repositories.DrugRepository
import com.smartpad.cabessa.modules.drugs.data.repositoryImpl.DrugRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.smartpad.cabessa.modules.drugs.ui.viewmodel.DrugViewModel
import com.smartpad.cabessa.modules.drugs.ui.components.AddEditDrugDialog
import com.smartpad.cabessa.modules.drugs.ui.components.DrugItemCard
import com.smartpad.cabessa.modules.drugs.ui.viewmodel.DrugUiEvent
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity

/**
 * DrugListScreen shows the list of drugs and allows add/edit/delete.
 *
 * Technical basis:
 * - This composable collects the ViewModel StateFlow using lifecycle aware collection in Compose.
 * - We use a simple dialog for create/edit to keep the example focused.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugListScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current

    val repository = remember { provideDrugRepository(context) }
    val factory = remember { DrugViewModelFactory(repository) }
    val viewModel: DrugViewModel = viewModel(factory = factory)

    val drugs by viewModel.drugs.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingDrug by remember { mutableStateOf<DrugEntity?>(null) }

    // Delete confirmation state
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var deleteTargetId by remember { mutableStateOf<Long?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is DrugUiEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is DrugUiEvent.Created -> {
                    snackbarHostState.showSnackbar("Drug created (id=${event.id})")
                }
                else -> { /* handle others */ }
            }
        }
    }

    // NOTA: aquí no usamos Scaffold interior (dejamos que el Scaffold superior en MainAppScaffold
    // gestione el TopAppBar / insets). Solo usamos un SnackbarHost y el FAB visual se gestiona
    // desde el MainAppScaffold's UI pattern — si quieres que el FAB sea local a ésta pantalla,
    // mantén el FloatingActionButton aquí como antes. (Mantengo FAB aquí por conveniencia).
    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Título: pequeño y pegado a la parte superior (sin padding extra por tener otro Scaffold)
            Text(
                text = "Drugs List",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 14.dp)
            )

            if (drugs.isEmpty()) {
                Text(
                    text = "No drugs available. Click + to add.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(drugs, key = { it.id }) { drug ->
                        DrugItemCard(
                            drug = drug,
                            onEdit = { selected ->
                                editingDrug = selected
                                showAddDialog = true
                            },
                            onDelete = { id ->
                                // open confirmation dialog instead of deleting immediately
                                deleteTargetId = id
                                showDeleteConfirmDialog = true
                            }
                        )
                    }
                }
            }
        }

        // FloatingActionButton (sobre el contenido)
        FloatingActionButton(
            onClick = {
                editingDrug = null
                showAddDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add drug")
        }

        // Add / Edit dialog
        if (showAddDialog) {
            AddEditDrugDialog(
                existing = editingDrug,
                onDismiss = {
                    showAddDialog = false
                    editingDrug = null
                },
                onSave = { newDrug ->
                    showAddDialog = false
                    val toSave = if (editingDrug != null) {
                        newDrug.copy(id = editingDrug!!.id)
                    } else newDrug

                    scope.launch {
                        viewModel.createDrug(toSave)
                    }
                    editingDrug = null
                }
            )
        }

        // Delete confirmation dialog
        if (showDeleteConfirmDialog && deleteTargetId != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteConfirmDialog = false
                    deleteTargetId = null
                },
                title = { Text("Confirm delete") },
                text = { Text("Are you sure you want to delete this drug?") },
                confirmButton = {
                    TextButton(onClick = {
                        val idToDelete = deleteTargetId
                        showDeleteConfirmDialog = false
                        deleteTargetId = null
                        if (idToDelete != null) {
                            scope.launch {
                                try {
                                    viewModel.softDeleteDrug(idToDelete)
                                    snackbarHostState.showSnackbar("Drug deleted")
                                } catch (t: Throwable) {
                                    snackbarHostState.showSnackbar("Delete failed: ${t.message}")
                                }
                            }
                        }
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteConfirmDialog = false
                        deleteTargetId = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


/**
 * Simple provider to create the repository. We fetch the AppDatabase singleton and create
 * the DrugRepositoryImpl using the DAOs. Use remember in the composable to avoid recreating it.
 */
private fun provideDrugRepository(context: Context): DrugRepository {
    val db = AppDatabase.getInstance(context)
    return DrugRepositoryImpl(db.drugDao(), db.registryDao())
}

/**
 * ViewModelProvider.Factory that constructs DrugViewModel with a DrugRepository.
 * This enables Compose to create the ViewModel with the required constructor args.
 */
private class DrugViewModelFactory(
    private val repository: DrugRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrugViewModel::class.java)) {
            return DrugViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}