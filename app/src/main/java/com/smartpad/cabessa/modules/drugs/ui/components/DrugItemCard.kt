package com.smartpad.cabessa.modules.drugs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity
import com.smartpad.cabessa.R
import com.smartpad.cabessa.modules.drugs.common.enums.EnumUnitTypes
import java.text.DecimalFormat

/**
 * Small card to visualize a drug in the list.
 *
 * Technical basis:
 * - Simple reusable component; operations are callbacks to keep stateless.
 */
@Composable
fun DrugItemCard(
    drug: DrugEntity,
    onEdit: (DrugEntity) -> Unit,
    onDelete: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Row with icon + title (name bold) and active substance in parentheses (normal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_drug),
                    contentDescription = stringResource(id = R.string.drugs),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Title: name (bold) and active substance in parentheses (normal)
                Text(
                    text = drug.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize * 0.90f
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.36f)
            )

            // Stock with "(per box X)" appended: format numbers and units depending on unitType
            val stockText = formatWithUnit(value = drug.stockUnits, unit = drug.unitType)
            val perBoxText = formatWithUnit(value = drug.quantityPerBox, unit = drug.unitType)
            val perBoxFormatted = stringResource(id = R.string.per_box_format, perBoxText)
            val stockWithPerBox = stringResource(id = R.string.stock_with_per_box_format, stockText, perBoxFormatted)
            LabeledRow(
                label = stringResource(id = R.string.stock),
                value = stockWithPerBox,
                labelStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                valueStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            LabeledRow(
                label = stringResource(id = R.string.active_substance),
                value = drug.activeSubstance,
                labelStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                valueStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Brand (inline) — larger typography
            LabeledRow(
                label = stringResource(id = R.string.brand),
                value = drug.brand,
                labelStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                valueStyle = MaterialTheme.typography.bodyLarge
            )

            // AQUÍ FALTARÍA
            // 1) Próxima compra
            // 2) Tratamientos activos asociados

            Spacer(modifier = Modifier.height(12.dp))

            // Comments: label above value (exception) — larger typography
            // Comments: show only if there are comments
            if (!drug.comments.isNullOrBlank()) {
                Text(
                    text = stringResource(id = R.string.comments) + ":",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = drug.comments!!, // safe because we checked isNullOrBlank()
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // Small spacer when there are no comments to keep breathing room above buttons
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Buttons aligned to the right, slightly larger labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onDelete(drug.id) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { onEdit(drug) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.edit),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

/**
 * Render label and value on a single row (label: value).
 * Accepts custom text styles so caller can increase sizes.
 */
@Composable
private fun LabeledRow(
    label: String,
    value: String,
    labelStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
    valueStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = labelStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(
            text = value,
            style = valueStyle,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Composable helper to format a numeric value and append a localized unit suffix.
 *  - UNITS: no decimals, localized suffix unit_units
 *  - MG: up to 2 decimals, localized suffix unit_mg
 *  - ML: up to 2 decimals, localized suffix unit_ml
 */
@Composable
private fun formatWithUnit(value: Double, unit: EnumUnitTypes): String {
    val df = DecimalFormat("#.##") // up to 2 decimals
    return when (unit) {
        EnumUnitTypes.UNITS -> {
            val intVal = value.toLong()
            val unitStr = stringResource(id = R.string.unit_units)
            stringResource(id = R.string.unit_with_value_format, intVal.toString(), unitStr)
        }
        EnumUnitTypes.MG -> {
            val formatted = df.format(value)
            val unitStr = stringResource(id = R.string.unit_mg)
            stringResource(id = R.string.unit_with_value_format, formatted, unitStr)
        }
        EnumUnitTypes.ML -> {
            val formatted = df.format(value)
            val unitStr = stringResource(id = R.string.unit_ml)
            stringResource(id = R.string.unit_with_value_format, formatted, unitStr)
        }
    }
}