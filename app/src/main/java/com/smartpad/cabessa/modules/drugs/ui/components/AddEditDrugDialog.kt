package com.smartpad.cabessa.modules.drugs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartpad.cabessa.modules.drugs.common.enums.EnumUnitTypes
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity

/**
 * Simple dialog for adding/editing a drug.
 *
 * Technical basis:
 * - For production you may use a BottomSheet or a separate route.
 * - This dialog returns a DrugEntity (without id); repository will assign global id.
 */
@Composable
fun AddEditDrugDialog(
    onDismiss: () -> Unit,
    onSave: (DrugEntity) -> Unit,
    existing: DrugEntity? = null
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var brand by remember { mutableStateOf(existing?.brand ?: "") }
    var quantityPerBox by remember { mutableStateOf(existing?.quantityPerBox?.toString() ?: "0.0") }
    var activeSubstance by remember { mutableStateOf(existing?.activeSubstance ?: "") }
    var unitType by remember { mutableStateOf(existing?.unitType ?: EnumUnitTypes.UNITS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "New drug" else "Edit drug") },
        text = {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") })
                OutlinedTextField(value = quantityPerBox, onValueChange = { quantityPerBox = it }, label = { Text("Qty per box") })
                OutlinedTextField(value = activeSubstance, onValueChange = { activeSubstance = it }, label = { Text("Active substance") })
                OutlinedTextField(value = unitType.name, onValueChange = { /* ignore */ }, label = { Text("Unit Type (choose in production)") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val qty = quantityPerBox.toDoubleOrNull() ?: 0.0
                val drug = DrugEntity(
                    id = existing?.id ?: 0L,
                    name = name,
                    brand = brand,
                    quantityPerBox = qty,
                    activeSubstance = activeSubstance,
                    unitType = unitType
                )
                onSave(drug)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}