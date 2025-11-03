package org.darthacheron.fitbe.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun <T> DropdownSelection(
    initialState: Boolean,
    items: List<T>,
    isEnabled: Boolean = true,
    selectedIndex: Int = 0,
    title: String,
    itemContent: @Composable (T, () -> Unit) -> Unit,
    itemToString: @Composable (T) -> String,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(initialState) }
    val selectedOption = items[selectedIndex]

    val isExpanded = expanded && isEnabled
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = itemToString(selectedOption),
            onValueChange = {},
            readOnly = true,
            label = { Text(title) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, selectionOption ->
                itemContent(selectionOption) {
                    onItemSelected(index)
                    expanded = false
                }
            }
        }
    }
}