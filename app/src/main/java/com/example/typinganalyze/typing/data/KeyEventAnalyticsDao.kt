package com.example.typinganalyze.typing.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyEventAnalyticsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyEvent(analytics: KeyEventAnalytics)

    @Query("SELECT * FROM key_event_analytics " +
            "WHERE keyCode = :keyCode " +
            "AND keyReleaseTime IS NULL " +
            "ORDER BY keyPressTime DESC LIMIT 1")
    suspend fun findLastPressedEvent(keyCode: Long): KeyEventAnalytics?

    @Query("SELECT * FROM key_event_analytics ORDER BY keyPressTime ASC")
    fun getAllEvents(): Flow<List<KeyEventAnalytics>>

    @Query("DELETE FROM key_event_analytics")
    suspend fun clearAllKeyEvents()
}