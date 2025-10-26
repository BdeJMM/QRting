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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.qrting.data.AppDatabase
import com.example.qrting.data.HistoryRepository
import com.example.qrting.ui.Scanner.ScannerScreen
import com.example.qrting.ui.Scanner.ScannerVM
import com.example.qrting.ui.history.HistoryScreen
import com.example.qrting.ui.history.HistoryVM
import com.example.qrting.ui.theme.QRtingTheme

// Contenido final y corregido para: MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creamos una única instancia de la base de datos y el repositorio para toda la app.
        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { HistoryRepository(database.urlHistoryDao()) }

        enableEdgeToEdge()
        setContent {
            QRtingTheme {
                // Le pasamos el repositorio a nuestra pantalla principal.
                MainAppScreen(repository = repository)
            }
        }
    }
}

// Esta clase especial le enseña a la app cómo crear nuestros VMs,
// ya que ahora necesitan el 'repository' para funcionar.
class ViewModelFactory(
    private val application: Application,
    private val repository: HistoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Cuando se pida un ScannerVM...
            modelClass.isAssignableFrom(ScannerVM::class.java) -> {
                // ...lo creamos pasándole la 'application' y el 'repository'.
                ScannerVM(application, repository) as T
            }
            // Cuando se pida un HistoryVM...
            modelClass.isAssignableFrom(HistoryVM::class.java) -> {
                // ...lo creamos pasándole el 'repository'.
                HistoryVM(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@Composable
fun MainAppScreen(repository: HistoryRepository) {
    val navController = androidx.navigation.compose.rememberNavController()

    // Obtenemos el contexto de la aplicación para pasárselo a la Factory.
    val context = LocalContext.current.applicationContext as Application
    // Creamos una instancia de nuestra Factory.
    val viewModelFactory = ViewModelFactory(context, repository)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Conectamos las pantallas reales y los VMs.
            NavigationHost(
                navController = navController,
                viewModelFactory = viewModelFactory // Le pasamos la factory al navegador.
            )
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModelFactory: ViewModelProvider.Factory // Recibe la factory.
) {
    androidx.navigation.NavHost(
        navController = navController,
        startDestination = "scanner"
    ) {
        composable("scanner") {
            // Usamos viewModel() para obtener una instancia del ScannerVM.
            // La factory que creamos le enseñará cómo construirlo.
            val scannerVM: ScannerVM = viewModel(factory = viewModelFactory)
            // ¡Mostramos la pantalla real del escáner!
            ScannerScreen(viewModel = scannerVM)
        }
        composable("history") {
            // Hacemos lo mismo para el HistoryVM.
            val historyVM: HistoryVM = viewModel(factory = viewModelFactory)
            // ¡Mostramos la pantalla real del historial!
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
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
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
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}