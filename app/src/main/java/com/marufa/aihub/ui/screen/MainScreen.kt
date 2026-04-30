package com.marufa.aihub.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.marufa.aihub.data.model.AiTab
import com.marufa.aihub.ui.components.*
import com.marufa.aihub.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddTabDialog by remember { mutableStateOf(false) }
    var tabToDelete by remember { mutableStateOf<AiTab?>(null) }

    // Handle hardware back button — go back in WebView first
    BackHandler(enabled = uiState.activeTabId.isNotEmpty()) {
        if (canGoBack(uiState.activeTabId)) {
            goBack(uiState.activeTabId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val activeTab = uiState.tabs.find { it.id == uiState.activeTabId }
                    Text(
                        text = activeTab?.let {
                            if (it.accountLabel.isNotEmpty()) "${it.name} · ${it.accountLabel}"
                            else it.name
                        } ?: "AI Hub"
                    )
                },
                actions = {
                    // Reload button
                    IconButton(onClick = { reload(uiState.activeTabId) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reload")
                    }
                    // Delete current tab
                    IconButton(onClick = {
                        uiState.tabs.find { it.id == uiState.activeTabId }?.let {
                            tabToDelete = it
                        }
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close Tab")
                    }
                }
            )
        },
        bottomBar = {
            TabBar(
                tabs = uiState.tabs,
                activeTabId = uiState.activeTabId,
                onTabSelected = { viewModel.setActiveTab(it.id) },
                onAddTab = { showAddTabDialog = true }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Render all WebViews but only show the active one
            // This keeps sessions alive without destroying inactive tabs
            uiState.tabs.forEach { tab ->
                TabWebView(
                    tabId = tab.id,
                    sessionId = tab.sessionId,
                    url = tab.url,
                    isActive = tab.id == uiState.activeTabId,
                    modifier = if (tab.id == uiState.activeTabId)
                        Modifier.fillMaxSize()
                    else
                        Modifier.size(width = androidx.compose.ui.unit.Dp(0f), height = androidx.compose.ui.unit.Dp(0f))
                )
            }
        }
    }

    // Add Tab Dialog
    if (showAddTabDialog) {
        AddTabDialog(
            onDismiss = { showAddTabDialog = false },
            onAddTab = { tab ->
                viewModel.addTab(tab)
                viewModel.setActiveTab(tab.id)
            }
        )
    }

    // Delete Tab Confirmation
    tabToDelete?.let { tab ->
        AlertDialog(
            onDismissRequest = { tabToDelete = null },
            title = { Text("Remove Tab") },
            text = { Text("Remove \"${tab.name}\"? This will clear its session and login.") },
            confirmButton = {
                Button(
                    onClick = {
                        destroyWebView(tab.id)
                        viewModel.deleteTab(tab)
                        tabToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Remove") }
            },
            dismissButton = {
                TextButton(onClick = { tabToDelete = null }) { Text("Cancel") }
            }
        )
    }
}
