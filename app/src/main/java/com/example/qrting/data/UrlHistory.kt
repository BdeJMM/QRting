package com.example.qrting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

class UrlHistory// Le dice a Room que esta clase es una tabla en la base de datos.
@Entity(tableName = "url_history")
data class UrlHistory(
    // 'id' es la clave única. Se genera automáticamente.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Columna para guardar el URL.
    val url: String,

    // Columna para guardar la fecha y hora.
    val timestamp: Long = System.currentTimeMillis()
) {
}