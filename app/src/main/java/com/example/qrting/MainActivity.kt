package com.example.qrting

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.qrting.data.AppDatabase
import com.example.qrting.data.HistoryRepository
import com.example.qrting.ui.Scanner.ScannerScreen
import com.example.qrting.ui.Scanner.ScannerVM
import com.example.qrting.ui.history.HistoryScreen
import com.example.qrting.ui.history.HistoryVM
import com.example.qrting.ui.theme.QRtingTheme

class MainActivity : ComponentActivity() {

    // Define the factory as a lazy property to be created once.
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
                // Pass the factory to the main screen.
                MainAppScreen(factory = viewModelFactory)
            }
        }
    }
}

// A simpler, correct ViewModelFactory.
// It doesn't need a companion object or access to composable contexts.
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
fun MainAppScreen(factory: ViewModelProvider.Factory) { // Receive the factory
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationHost(
                navController = navController,
                factory = factory // Pass the factory down
            )
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    factory: ViewModelProvider.Factory // Receive the factory
) {
    NavHost(
        navController = navController,
        startDestination = "scanner"
    ) {
        composable("scanner") {
            // Use the factory to create the ViewModel instance.
            val scannerVM: ScannerVM = viewModel(factory = factory)
            ScannerScreen(viewModel = scannerVM)
        }
        composable("history") {
            // Use the same factory for the HistoryVM.
            val historyVM: HistoryVM = viewModel(factory = factory)
            HistoryScreen(viewModel = historyVM)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.QrCodeScanner, contentDescription = "Escanear QR") },
            label = { Text("Escanear") },
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
            icon = { Icon(Icons.Filled.History, contentDescription = "Historial") },
            label = { Text("Historial") },
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
