package com.marufa.aihub.data

import com.marufa.aihub.data.db.AiTabDao
import com.marufa.aihub.data.model.AiTab
import com.marufa.aihub.data.model.AiTools
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TabRepository @Inject constructor(
    private val dao: AiTabDao
) {
    fun getAllTabs(): Flow<List<AiTab>> = dao.getAllTabs()

    suspend fun addTab(tab: AiTab) = dao.insertTab(tab)

    suspend fun updateTab(tab: AiTab) = dao.updateTab(tab)

    suspend fun deleteTab(tab: AiTab) = dao.deleteTab(tab)

    suspend fun deleteTabById(id: String) = dao.deleteTabById(id)

    suspend fun reorderTabs(tabs: List<AiTab>) {
        tabs.forEachIndexed { index, tab ->
            dao.updateOrder(tab.id, index)
        }
    }

    // Seed default tabs on first launch
    suspend fun seedDefaultTabs() {
        if (dao.getTabCount() == 0) {
            val defaults = AiTools.all
                .filter { it.key != "custom" }
                .mapIndexed { index, tool ->
                    AiTab(
                        name = tool.displayName,
                        url = tool.url,
                        toolKey = tool.key,
                        order = index
                    )
                }
            dao.insertTabs(defaults)
        }
    }
}
