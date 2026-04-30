package com.marufa.aihub.data.db

import androidx.room.*
import com.marufa.aihub.data.model.AiTab
import kotlinx.coroutines.flow.Flow

@Dao
interface AiTabDao {

    @Query("SELECT * FROM ai_tabs ORDER BY `order` ASC")
    fun getAllTabs(): Flow<List<AiTab>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTab(tab: AiTab)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTabs(tabs: List<AiTab>)

    @Update
    suspend fun updateTab(tab: AiTab)

    @Delete
    suspend fun deleteTab(tab: AiTab)

    @Query("DELETE FROM ai_tabs WHERE id = :id")
    suspend fun deleteTabById(id: String)

    @Query("SELECT COUNT(*) FROM ai_tabs")
    suspend fun getTabCount(): Int

    @Query("UPDATE ai_tabs SET `order` = :order WHERE id = :id")
    suspend fun updateOrder(id: String, order: Int)
}
