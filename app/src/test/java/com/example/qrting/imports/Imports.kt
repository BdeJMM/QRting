package com.example.qrting.imports

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
// FIX: Corregido el nombre del paquete (era 'coroutintest' y debe ser 'coroutines.test')
import kotlinx.coroutines.test.TestDispatcher 
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// Esta clase nos ayuda a probar Corutinas y ViewModels simulando el hilo principal.
@OptIn(ExperimentalCoroutinesApi::class)
class Imports(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}