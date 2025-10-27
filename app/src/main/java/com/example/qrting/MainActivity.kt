package com.example.qrting

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.qrting.data.AppDatabase
import com.example.qrting.data.HistoryRepository
import com.example.qrting.ui.Scanner.ScannerScreen
import com.example.qrting.ui.Scanner.ScannerVM
import com.example.qrting.ui.VideoBackground
import com.example.qrting.ui.history.HistoryScreen
import com.example.qrting.ui.history.HistoryVM
import com.example.qrting.ui.theme.QRtingTheme

class MainActivity : ComponentActivity() {

    private val viewModelFactory by lazy {
        val database = AppDatabase.getDatabase(this)
        val repository = HistoryRepository(database.urlHistoryDao())
        ViewModelFactory(application, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRtingTheme {
                MainAppScreen(factory = viewModelFactory)
            }
        }
    }
}

class ViewModelFactory(
    private val application: Application,
    private val repository: HistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ScannerVM::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ScannerVM(application, repository) as T
            }
            modelClass.isAssignableFrom(HistoryVM::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HistoryVM(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

@Composable
fun MainAppScreen(factory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        // Video de fondo que ocupa toda la pantalla
        VideoBackground()

        // Interfaz de la app encima del video
        Scaffold(
            containerColor = Color.Transparent, // Hacemos el contenedor principal transparente
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationHost(
                    navController = navController,
                    factory = factory
                )
            }
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    factory: ViewModelProvider.Factory
) {
    NavHost(
        navController = navController,
        startDestination = "scanner",
        // Hacemos el fondo del NavHost transparente
        modifier = Modifier.fillMaxSize()
    ) {
        composable("scanner") {
            val scannerVM: ScannerVM = viewModel(factory = factory)
            ScannerScreen(viewModel = scannerVM)
        }
        composable("history") {
            val historyVM: HistoryVM = viewModel(factory = factory)
            HistoryScreen(viewModel = historyVM)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        // Hacemos la barra de navegación semi-transparente
        containerColor = Color.Black.copy(alpha = 0.5f)
    ) {
        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent // Ocultamos el indicador de selección
            ),
            icon = { Icon(Icons.Filled.QrCodeScanner, contentDescription = "Escanear QR", tint = Color.White) },
            label = { Text("Escanear", color = Color.White) },
            selected = currentRoute == "scanner",
            onClick = {
                if (currentRoute != "scanner") {
                    navController.navigate("scanner") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            ),
            icon = { Icon(Icons.Filled.History, contentDescription = "Historial", tint = Color.White) },
            label = { Text("Historial", color = Color.White) },
            selected = currentRoute == "history",
            onClick = {
                if (currentRoute != "history") {
                    navController.navigate("history") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}
