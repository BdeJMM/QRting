package com.example.qrting.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.webkit.URLUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrting.repository.HistoryRepository
import kotlinx.coroutines.launch

class ScannerVM(application: Application, private val repository: HistoryRepository) :
    AndroidViewModel(application) {

    fun onQrCodeScanned(scannedText: String) {
        // 1. Guarda el texto escaneado en segundo plano.
        viewModelScope.launch {
            repository.addUrl(scannedText)
        }
        // 2. Vibra para dar feedback.
        vibratePhone()

        // 3. Si el texto es una URL válida, la abre en el navegador.
        if (URLUtil.isValidUrl(scannedText)) {
            openUrlInBrowser(scannedText)
        }
    }

    private fun openUrlInBrowser(url: String) {
        val context = getApplication<Application>().applicationContext
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Intenta abrir con Chrome específicamente.
        browserIntent.setPackage("com.android.chrome")
        try {
            context.startActivity(browserIntent)
        } catch (_: Exception) {
            // Si Chrome no está, quita el paquete para que Android elija cualquier navegador.
            browserIntent.setPackage(null)
            try {
                context.startActivity(browserIntent)
            } catch (_: Exception) {
                // Si no hay ningún navegador, no se hace nada para evitar un crash.
            }
        }
    }

    private fun vibratePhone() {
        // Forma moderna de obtener el servicio (funciona perfectamente en tu minSdk 33)
        val vibrator = getApplication<Application>().getSystemService(Vibrator::class.java)
        
        // Verificamos que el vibrador exista (por seguridad)
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(200)
            }
        }
    }
}
