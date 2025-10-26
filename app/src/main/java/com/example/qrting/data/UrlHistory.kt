package com.example.qrting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Le dice a Room que esta es una tabla en la base de datos.
@Entity(tableName = "url_history")
data class UrlHistory(
    // ¡AÑADIDO! Esta es la clave primaria que Room necesita.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Columna para guardar la URL.
    val url: String,

    // Columna para guardar la fecha y hora.
    val timestamp: Long = System.currentTimeMillis()
)