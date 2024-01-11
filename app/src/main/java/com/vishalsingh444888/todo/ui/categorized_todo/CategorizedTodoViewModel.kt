package com.vishalsingh444888.todo.ui.categorized_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.data.TodoRepository
import com.vishalsingh444888.todo.util.Routes
import com.vishalsingh444888.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CategorizedTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) :ViewModel() {

    var category by mutableStateOf("")
        private set

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos:StateFlow<List<Todo>> = _todos

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    init {
        category = savedStateHandle.get<String>("category")!!
        viewModelScope.launch {
            repository.getTodoByCategory(category).collect{newTodos ->
                _todos.value = newTodos
            }
        }
    }



    fun onEvent(event: CategorizedTodoEvent){
        when(event){
            is CategorizedTodoEvent.OnCategoryTitleChange -> {
                category = event.newTitle
//                viewModelScope.launch {
//                    todos.value.forEach { todo: Todo ->
//                        repository.insertTodo(todo.copy(
//                            category = event.newTitle
//                        ))
//                    }
//                    categories.updateCategories(event.newTitle)
//                }
            }
            is CategorizedTodoEvent.OnDoneClick -> {
                viewModelScope.launch {
                    val isCompleted = event.todo.status== Status.NEW||event.todo.status== Status.IN_PROGRESS
                    val status = if (isCompleted) Status.COMPLETED else Status.IN_PROGRESS
                    repository.insertTodo(
                        event.todo.copy(
                            status = status
                        )
                    )
                }
            }
            is CategorizedTodoEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id.toString()}"))
            }

            is CategorizedTodoEvent.OnSaveClick -> {
                viewModelScope.launch {
                    if(category.isBlank()){
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "Category can't be empty"
                        ))
                        return@launch
                    }
                    category = category.trim().split(" ").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }.joinToString(" ")
                    todos.value.forEach { todo: Todo ->
                        repository.insertTodo(todo.copy(
                            category = category
                        ))
                    }

                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}