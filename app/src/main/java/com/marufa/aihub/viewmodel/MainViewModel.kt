package com.marufa.aihub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marufa.aihub.data.TabRepository
import com.marufa.aihub.data.model.AiTab
import com.marufa.aihub.data.store.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val tabs: List<AiTab> = emptyList(),
    val activeTabId: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TabRepository,
    private val prefs: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedDefaultTabs()
        }

        viewModelScope.launch {
            combine(
                repository.getAllTabs(),
                prefs.activeTabId
            ) { tabs, activeId ->
                val enabledTabs = tabs.filter { it.isEnabled }
                val resolvedActiveId = if (activeId.isEmpty() || enabledTabs.none { it.id == activeId }) {
                    enabledTabs.firstOrNull()?.id ?: ""
                } else activeId

                MainUiState(
                    tabs = enabledTabs,
                    activeTabId = resolvedActiveId
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setActiveTab(tabId: String) {
        viewModelScope.launch {
            prefs.setActiveTabId(tabId)
        }
    }

    fun addTab(tab: AiTab) {
        viewModelScope.launch {
            repository.addTab(tab)
        }
    }

    fun deleteTab(tab: AiTab) {
        viewModelScope.launch {
            repository.deleteTab(tab)
            // Switch to first available tab if deleted tab was active
            if (_uiState.value.activeTabId == tab.id) {
                val next = _uiState.value.tabs.firstOrNull { it.id != tab.id }
                if (next != null) prefs.setActiveTabId(next.id)
            }
        }
    }

    fun toggleTabEnabled(tab: AiTab) {
        viewModelScope.launch {
            repository.updateTab(tab.copy(isEnabled = !tab.isEnabled))
        }
    }

    fun reorderTabs(tabs: List<AiTab>) {
        viewModelScope.launch {
            repository.reorderTabs(tabs)
        }
    }
}
