package com.example.qrting.ui.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.qrting.data.UrlHistory
import org.junit.Rule
import org.junit.Test

class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun historyList_muestra_items_correctamente() {
        // datos de prueba
        val listaPrueba = listOf(
            UrlHistory(id = 1, url = "https://google.com", timestamp = 1678888888000),
            UrlHistory(id = 2, url = "https://facebook.com", timestamp = 1678888889000)
        )

        // cargar contenido
        composeTestRule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HistoryList(historyList = listaPrueba)
                }
            }
        }
        
        // esperar renderizado
        composeTestRule.waitForIdle()

        // verificar existencia de elementos
        composeTestRule.onNodeWithText("https://google.com", substring = true).assertExists()
        composeTestRule.onNodeWithText("https://facebook.com", substring = true).assertExists()
    }
}