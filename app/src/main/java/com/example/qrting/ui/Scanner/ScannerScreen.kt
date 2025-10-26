package com.example.qrting.ui.Scanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

class ScannerScreen// Contenido para: ui/Scanner/ScannerScreen.kt
// La pantalla del escáner. Recibe el ScannerVM.
@Composable
fun ScannerScreen(viewModel: ScannerVM) { // <-- ¡Importante! Usamos ScannerVM aquí.
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher para pedir el permiso si no lo tenemos.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    // Se ejecuta una sola vez al entrar en la pantalla para pedir el permiso.
    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // Si tenemos permiso, mostramos la cámara.
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // Configuración del Preview (lo que ve el usuario).
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        // Configuración del Analizador de Imágenes para procesar el QR.
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val image = imageProxy.image
                            if (image != null) {
                                val inputImage = InputImage.fromMediaImage(
                                    image,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                // Opciones para que el escáner solo busque códigos QR.
                                val options = BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                    .build()
                                val scanner = BarcodeScanning.getClient(options)

                                // Procesamos la imagen.
                                scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        // Si detecta un código...
                                        if (barcodes.isNotEmpty()) {
                                            // ...y tiene un valor...
                                            barcodes.firstOrNull()?.rawValue?.let { url ->
                                                // ...se lo pasamos a nuestro ViewModel!
                                                viewModel.onQrCodeScanned(url)
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Log.e("ScannerScreen", "Error al escanear", it)
                                    }
                                    .addOnCompleteListener {
                                        // ¡Muy importante cerrar el imageProxy para que la cámara siga funcionando!
                                        imageProxy.close()
                                    }
                            }
                        }

                        // Usamos la cámara trasera por defecto.
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            // Desvinculamos todo antes de volver a vincular.
                            cameraProvider.unbindAll()
                            // Vinculamos el preview y el analizador al ciclo de vida.
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.e("ScannerScreen", "Error al vincular la cámara", e)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
            // ... inside the Box composable
        } else {
            // Si no tenemos permiso, mostramos un texto.
            Text(
                text = "Se necesita permiso de la cámara para escanear.",
                modifier = Modifier.align(Alignment.Center)            )
        }
    }
} // This is the correct final closing brace for the ScannerScreen composable.
