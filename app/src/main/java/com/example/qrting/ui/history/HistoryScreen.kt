package com.example.qrting.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.qrting.viewmodel.HistoryVM
import java.text.SimpleDateFormat
import java.util.*

// La pantalla principal (Con estado): Habla con el ViewModel
@Composable
fun HistoryScreen(viewModel: HistoryVM) { 
    val historyList by viewModel.urlHistory.observeAsState(initial = emptyList())
    // Llama a la versión "tonta" que solo pinta datos
    HistoryList(historyList = historyList)
}

// La lista visual (Sin estado): Ideal para probar porque solo pide una lista.
@Composable
fun HistoryList(historyList: List<UrlHistory>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(items = historyList) { urlHistory ->
            HistoryItem(urlHistory = urlHistory)
        }
    }
}

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
            Text(
                text = urlHistory.url,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = formatTimestamp(urlHistory.timestamp),
                fontSize = 12.sp
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}