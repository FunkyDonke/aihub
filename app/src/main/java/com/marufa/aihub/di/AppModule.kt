package com.marufa.aihub.di

import android.content.Context
import androidx.room.Room
import com.marufa.aihub.data.db.AppDatabase
import com.marufa.aihub.data.db.AiTabDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aihub_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAiTabDao(db: AppDatabase): AiTabDao = db.aiTabDao()
}
