package com.example.typinganalyze.typing.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [KeyEventAnalytics::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keyEventAnalyticsDao(): KeyEventAnalyticsDao
}