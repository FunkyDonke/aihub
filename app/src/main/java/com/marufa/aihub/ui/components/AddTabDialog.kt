package com.marufa.aihub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marufa.aihub.data.model.AiTab
import com.marufa.aihub.data.model.AiTool
import com.marufa.aihub.data.model.AiTools

@Composable
fun AddTabDialog(
    onDismiss: () -> Unit,
    onAddTab: (AiTab) -> Unit
) {
    var selectedTool by remember { mutableStateOf<AiTool?>(null) }
    var tabName by remember { mutableStateOf("") }
    var customUrl by remember { mutableStateOf("") }
    var accountLabel by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add AI Tab") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Tool picker
                Text("Choose AI", style = MaterialTheme.typography.labelMedium)
                LazyColumn(modifier = Modifier.height(200.dp)) {
                    items(AiTools.all) { tool ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    selectedTool = tool
                                    if (tabName.isEmpty()) tabName = tool.displayName
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTool?.key == tool.key,
                                onClick = {
                                    selectedTool = tool
                                    if (tabName.isEmpty()) tabName = tool.displayName
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(tool.displayName)
                        }
                    }
                }

                // Custom URL (only for custom tool)
                if (selectedTool?.key == "custom") {
                    OutlinedTextField(
                        value = customUrl,
                        onValueChange = { customUrl = it },
                        label = { Text("URL") },
                        placeholder = { Text("https://example.com") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Tab name
                OutlinedTextField(
                    value = tabName,
                    onValueChange = { tabName = it },
                    label = { Text("Tab Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Account label (optional)
                OutlinedTextField(
                    value = accountLabel,
                    onValueChange = { accountLabel = it },
                    label = { Text("Account Label (optional)") },
                    placeholder = { Text("e.g. Work, Personal") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val tool = selectedTool ?: return@Button
                    val url = if (tool.key == "custom") customUrl else tool.url
                    if (url.isBlank() || tabName.isBlank()) return@Button
                    onAddTab(
                        AiTab(
                            name = tabName,
                            url = url,
                            toolKey = tool.key,
                            accountLabel = accountLabel
                        )
                    )
                    onDismiss()
                },
                enabled = selectedTool != null && tabName.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
