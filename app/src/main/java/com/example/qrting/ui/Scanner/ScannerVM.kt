package com.example.qrting.ui.Scanner

// Contenido corregido para: ScannerVM.ktpackage com.example.qrting.ui.Scanner

// Contenido corregido para: ScannerVM.ktpackage com.example.qrting.ui.Scanner

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrting.data.HistoryRepository
import kotlinx.coroutines.launch

// Usamos el nombre de clase ScannerVM que preferiste.
class ScannerVM(application: Application, private val repository: HistoryRepository) :
    AndroidViewModel(application) {

    // Esta función será llamada cuando el escáner detecte un QR.
    fun onQrCodeScanned(url: String) {
        // Guarda la URL en segundo plano.
        viewModelScope.launch {
            repository.addUrl(url)
        }
        // Llama a la función para hacer vibrar el teléfono.
        vibratePhone()
    }

    private fun vibratePhone() {
        val vibrator =
            getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // El modo de vibrar cambió a partir de la API 26 (Oreo).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // Versión antigua para APIs anteriores.
            vibrator.vibrate(200)
        }
    }
}
