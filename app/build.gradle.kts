plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.qrting"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.qrting"
        minSdk = 33
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11" plugins {
            alias(libs.plugins.android.application)
            alias(libs.plugins.kotlin.android)
            // ELIMINA la siguiente línea si existe, ya que la reemplazaremos por la nueva
            // alias(libs.plugins.kotlin.compose)

            // AÑADE ESTOS PLUGINS:
            id("org.jetbrains.kotlin.plugin.compose") version "1.6.10" // Versión explícita para compatibilidad
            id("kotlin-kapt")
        }

        android {
            namespace = "com.example.qrting"
            compileSdk =
                34 // Cambiado a 34, que es la versión estable más reciente y compatible con las librerías.

            defaultConfig {
                applicationId = "com.example.qrting"
                minSdk = 33
                targetSdk = 34 // Ajustado a compileSdk
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
                sourceCompatibility =
                    JavaVersion.VERSION_1_8 // Cambiado a 1.8 por compatibilidad con muchas librerías de Android
                targetCompatibility = JavaVersion.VERSION_1_8
            }
            kotlinOptions {
                jvmTarget = "1.8" // Cambiado a 1.8
            }
            buildFeatures {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion =
                    "1.5.1" // Especifica la versión del compilador de Compose
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }

        dependencies {

            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.ui.graphics)
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.androidx.compose.material3)
            testImplementation(libs.junit)
            androidTestImplementation(libs.androidx.junit)
            androidTestImplementation(libs.androidx.espresso.core)
            androidTestImplementation(platform(libs.androidx.compose.bom))
            androidTestImplementation(libs.androidx.compose.ui.test.junit4)
            debugImplementation(libs.androidx.compose.ui.tooling)
            debugImplementation(libs.androidx.compose.ui.test.manifest)

            // --- DEPENDENCIAS AÑADIDAS PARA TU APP DE QR ---

            // CameraX para la cámara
            val cameraxVersion = "1.3.1"
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
            implementation("androidx.compose.runtime:runtime-livedata") // Para observar LiveData en Compose

            // Room para la base de datos SQLite
            val roomVersion = "2.6.1"
            implementation("androidx.room:room-runtime:$roomVersion")
            kapt("androidx.room:room-compiler:$roomVersion")
            implementation("androidx.room:room-ktx:$roomVersion") // Soporte para Coroutines

            // Navigation Component para navegar entre pantallas con Compose
            implementation("androidx.navigation:navigation-compose:2.7.7")
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}