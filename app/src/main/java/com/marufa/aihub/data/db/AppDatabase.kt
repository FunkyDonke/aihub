package com.marufa.aihub.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marufa.aihub.data.model.AiTab

@Database(
    entities = [AiTab::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aiTabDao(): AiTabDao
}
