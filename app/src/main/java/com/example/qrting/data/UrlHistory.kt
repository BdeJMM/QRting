package com.example.qrting.data

// Make sure you have this import
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_history_table") // The tableName is optional but good practice
data class UrlHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val timestamp: Long
)
