package com.tecsup.murocampus.viewmodel

sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

internal fun <T> Result<T>.comoUiState(): UiState<T> =
    fold(
        onSuccess = { UiState.Exito(it) },
        onFailure = { UiState.Error(it.message ?: "Error desconocido") }
    )
