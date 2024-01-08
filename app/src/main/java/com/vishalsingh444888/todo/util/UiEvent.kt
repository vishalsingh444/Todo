package com.vishalsingh444888.todo.util

sealed class UiEvent{
    data class Navigate(val route: String): UiEvent()

    data class ShowSnackbar(val message: String,val action: String? = null): UiEvent()
}
