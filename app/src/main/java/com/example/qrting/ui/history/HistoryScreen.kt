package com.example.qrting.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrting.data.UrlHistory
import java.text.SimpleDateFormat
import java.util.*

// Contenido para: ui/history/HistoryScreen.kt
// La pantalla del historial. Recibe el HistoryVM.
@Composable
fun HistoryScreen(viewModel: HistoryVM) { // <-- ¡Importante! Usamos HistoryVM aquí.
    // Observa los datos del VM. La UI se actualiza sola cuando los datos cambian.
    val historyList by viewModel.urlHistory.observeAsState(initial = emptyList())

    // LazyColumn es una lista eficiente.
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        // Crea un HistoryItem para cada elemento en la lista.
        items(historyList) { urlHistory ->
            HistoryItem(urlHistory = urlHistory)
        }
    }
}

// Composable para un solo elemento de la lista.
@Composable
fun HistoryItem(urlHistory: UrlHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Muestra la URL
            Text(
                text = urlHistory.url,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            // Muestra la fecha formateada
            Text(
                text = formatTimestamp(urlHistory.timestamp),
                fontSize = 12.sp
            )
        }
    }
}

// Función para convertir milisegundos a una fecha legible.
private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}