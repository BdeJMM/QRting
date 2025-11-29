package com.example.qrting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.qrting.data.UrlHistory
import com.example.qrting.repository.HistoryRepository

// Usamos el nombre de clase HistoryVM que preferiste.
class HistoryVM(repository: HistoryRepository) : ViewModel() {

    // Expone la lista de URLs como LiveData para que la UI la observe.
    val urlHistory: LiveData<List<UrlHistory>> = repository.allUrls.asLiveData()
}