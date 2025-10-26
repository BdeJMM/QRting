package com.example.qrting.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface UrlHistoryDao// Le dice a Room que esta es una interfaz para acceder a los datos.
@Dao
interface UrlHistoryDao {

    // Define una función para insertar un nuevo URL.
    @Insert
    suspend fun insertUrl(urlHistory: UrlHistory)

    // Define una consulta para obtener todos los URLs, ordenados por fecha.
    // Flow<> hace que los datos se actualicen solos en la pantalla.
    @Query("SELECT * FROM url_history ORDER BY timestamp DESC")
    fun getAllUrls(): Flow<List<UrlHistory>>
} {
}