package com.example.qrting.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UrlHistoryDaoTest {

    private lateinit var urlHistoryDao: UrlHistoryDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Usamos una base de datos en memoria: se borra al terminar el test.
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        urlHistoryDao = db.urlHistoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUrl() = runBlocking {
        // GIVEN: Un historial de prueba
        val historyItem = UrlHistory(url = "https://www.prueba.com", timestamp = System.currentTimeMillis())
        
        // WHEN: Lo insertamos en la base de datos
        urlHistoryDao.insertUrl(historyItem)
        
        // THEN: Leemos la base de datos y verificamos que esté ahí
        // 'first()' obtiene el primer valor emitido por el Flow
        val allUrls = urlHistoryDao.getAllUrls().first()
        
        assertEquals(1, allUrls.size)
        assertEquals("https://www.prueba.com", allUrls[0].url)
    }
}