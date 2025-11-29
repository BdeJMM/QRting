package com.example.qrting.repository

import com.example.qrting.data.UrlHistory
import com.example.qrting.data.UrlHistoryDao
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.argumentCaptor

class HistoryRepositoryTest {

    private val mockDao = mock(UrlHistoryDao::class.java)

    @Test
    fun `addUrl llama al dao para insertar`() = runTest {
        // inicializar repositorio
        val repository = HistoryRepository(mockDao)
        // url de prueba
        val testUrl = "https://example.com"

        // ejecutar metodo
        repository.addUrl(testUrl)

        // capturar argumento
        val captor = argumentCaptor<UrlHistory>()
        // verificar llamada al dao
        verify(mockDao).insertUrl(captor.capture())

        // validar url guardada
        assertEquals(testUrl, captor.firstValue.url)
    }

    @Test
    fun `allUrls obtiene el flow del dao`() = runTest {
        // lista de prueba
        val lista = listOf(UrlHistory(url = "A"), UrlHistory(url = "B"))
        // simular respuesta del dao
        `when`(mockDao.getAllUrls()).thenReturn(flowOf(lista))

        // inicializar repositorio
        val repository = HistoryRepository(mockDao)

        // recolectar y validar datos
        repository.allUrls.collect { resultados ->
            assertEquals(2, resultados.size)
            assertEquals("A", resultados[0].url)
        }
    }
}
