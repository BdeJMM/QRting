plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    // Esta es la forma correcta y explícita de añadir el plugin del compilador de Compose
    id("org.jetbrains.kotlin.plugin.compose")
}


android {
    namespace = "com.example.qrting"
    // Usamos SDK 34, que es la versión estable más compatible con las librerías que añadimos
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.qrting"
        minSdk = 33
        targetSdk = 34 // La targetSdk debe coincidir con la compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // La versión 1.8 de Java es el estándar más compatible para Android actualmente
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        // Habilitamos Jetpack Compose
        compose = true
    }
    composeOptions {
        // Versión del compilador de Kotlin para Compose
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Dependencias básicas de Compose y del proyecto
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // --- DEPENDENCIAS QUE AÑADIMOS PARA TU APP DE QR ---

    // CameraX para la cámara
    val cameraxVersion = "1.3.3" // Versión actualizada
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    // ML Kit para leer códigos de barras (QR)
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // ViewModel y LiveData para MVVM
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.compose.runtime:runtime-livedata")

    // Room para la base de datos SQLite
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion") // 'kapt' es para el plugin de Room
    implementation("androidx.room:room-ktx:$roomVersion")

    // Navigation Component para navegar entre pantallas con Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")
} // <-- ESTA ES LA LLAVE QUE FALTABA

// El final del archivo debería estar aquí, la línea 102 estaba vacía por error.