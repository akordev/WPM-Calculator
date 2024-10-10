package com.example.typinganalyze.typing.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_event_analytics")
data class KeyEventAnalytics(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val keyCode: Long,
    val keyPressTime: Long,
    val keyReleaseTime: Long?,
    val username: String
)