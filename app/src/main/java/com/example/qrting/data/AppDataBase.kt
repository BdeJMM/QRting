package com.example.qrting.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class AppDataBase// Define la base de datos, sus tablas (entities) y su versión.
@Database(entities = [UrlHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Proporciona el DAO a la app.
    abstract fun urlHistoryDao(): UrlHistoryDao

    // Este código crea una única instancia de la base de datos para toda la app.
    // Es una buena práctica para evitar problemas de rendimiento.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "qr_history_database" // Nombre del archivo de la base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} {
}