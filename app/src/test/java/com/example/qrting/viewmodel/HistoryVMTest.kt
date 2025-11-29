package com.example.qrting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.qrting.data.UrlHistory
import com.example.qrting.imports.Imports
import com.example.qrting.repository.HistoryRepository
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class HistoryVMTest {

    @get:Rule
    val mainDispatcherRule = Imports()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mock(HistoryRepository::class.java)

    @Test
    fun `init carga datos del repositorio`() {
        // datos de prueba
        val listaPrueba = listOf(UrlHistory(url = "test1"), UrlHistory(url = "test2"))
        // simular respuesta del repositorio
        `when`(mockRepository.allUrls).thenReturn(flowOf(listaPrueba))

        // inicializar viewmodel
        val viewModel = HistoryVM(mockRepository)

        // observar livedata para activarlo
        viewModel.urlHistory.observeForever { }

        // verificar datos cargados
        assertEquals(listaPrueba, viewModel.urlHistory.value)
    }
}