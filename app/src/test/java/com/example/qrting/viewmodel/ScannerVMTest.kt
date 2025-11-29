package com.example.qrting.viewmodel

import android.app.Application
import com.example.qrting.imports.Imports
import com.example.qrting.repository.HistoryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ScannerVMTest {

    @get:Rule
    val mainDispatcherRule = Imports()

    private val mockApplication = mock(Application::class.java)
    private val mockRepository = mock(HistoryRepository::class.java)
    
    @Test
    fun `onQrCodeScanned con URL VALIDA guarda en repositorio`() = runTest {
        // valor de prueba
        val urlValida = "https://www.google.com"
        val viewModel = ScannerVM(mockApplication, mockRepository)

        // ejecutar metodo, ignorando errores de ui
        try { viewModel.onQrCodeScanned(urlValida) } catch (_: Exception) {}

        // verificar llamada al repositorio
        verify(mockRepository).addUrl(urlValida)
    }

    @Test
    fun `onQrCodeScanned con TEXTO SIMPLE guarda en repositorio`() = runTest {
        // valor de prueba
        val textoSimple = "Hola Mundo"
        val viewModel = ScannerVM(mockApplication, mockRepository)

        // ejecutar metodo, ignorando errores de ui
        try { viewModel.onQrCodeScanned(textoSimple) } catch (_: Exception) {}

        // verificar llamada al repositorio
        verify(mockRepository).addUrl(textoSimple)
    }
}