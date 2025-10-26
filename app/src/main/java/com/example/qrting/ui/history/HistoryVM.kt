// Contenido corregido para: HistoryVM.kt
package com.example.qrting.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.qrting.data.HistoryRepository
import com.example.qrting.data.UrlHistory

// Usamos el nombre de clase HistoryVM que preferiste.
class HistoryVM(repository: HistoryRepository) : ViewModel() {

    // Expone la lista de URLs como LiveData para que la UI la observe.
    val urlHistory: LiveData<List<UrlHistory>> = repository.allUrls.asLiveData()
}
