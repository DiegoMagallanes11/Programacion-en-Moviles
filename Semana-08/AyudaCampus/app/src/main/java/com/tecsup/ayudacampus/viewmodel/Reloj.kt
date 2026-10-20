package com.tecsup.ayudacampus.viewmodel

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.flow

// Un único flujo por pantalla; stateIn + collectAsStateWithLifecycle detienen su colección.
fun observarReloj(ahora: () -> Long = System::currentTimeMillis) = flow {
 while (currentCoroutineContext().isActive) {
  emit(ahora())
  delay(30_000L)
 }
}
