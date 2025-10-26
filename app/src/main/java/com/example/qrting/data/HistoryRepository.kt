package com.example.qrting.data

import kotlinx.coroutines.flow.Flow

class HistoryRepository// Contenido para: HistoryRepository.kt
// El repositorio es una clase normal que toma el DAO como parámetro.
// Actúa como única fuente de verdad para los datos de la app.
class HistoryRepository(private val urlHistoryDao: UrlHistoryDao) {

    // Expone el Flow de la lista de URLs directamente desde el DAO.
    val allUrls: Flow<List<UrlHistory>> = urlHistoryDao.getAllUrls()

    // Crea una función para que el ViewModel pueda añadir un nuevo URL.
    // Esta función llama a la función 'suspend' del DAO.
    suspend fun addUrl(url: String) {
        val newUrl = UrlHistory(url = url)
        urlHistoryDao.insertUrl(newUrl)
    }
} {
}