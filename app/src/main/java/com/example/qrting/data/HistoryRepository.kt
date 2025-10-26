package com.example.qrting.data

import kotlinx.coroutines.flow.Flow

// The repository is a normal class that takes the DAO as a parameter.
// It acts as the single source of truth for the app's data.
class HistoryRepository(private val urlHistoryDao: UrlHistoryDao) {
    // Exposes the Flow of the list of URLs directly from the DAO.
    val allUrls: Flow<List<UrlHistory>> = urlHistoryDao.getAllUrls()

    // Creates a function for the ViewModel to add a new URL.
    // This function calls the DAO's 'suspend' function.
    suspend fun addUrl(url: String) {
        val newUrl = UrlHistory(url = url)
        urlHistoryDao.insertUrl(newUrl)
    }
}