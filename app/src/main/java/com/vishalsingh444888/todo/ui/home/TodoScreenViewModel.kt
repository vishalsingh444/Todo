package com.vishalsingh444888.todo.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.data.TodoRepository
import com.vishalsingh444888.todo.util.Routes
import com.vishalsingh444888.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TodoScreenViewModel @Inject constructor(
    private val repository: TodoRepository
): ViewModel() {

    private val _currentDate = mutableStateOf("")
    val currentDate: State<String> = _currentDate

    init {
        getCurrentDate()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    val todayTodos = repository.getTodayTodo(currentDate.value)
    val allTodos = repository.getAllTodo()

    private val allCategory = mutableListOf("Work","Personal","Health","Education","Family")

    private var deletedTodo: Todo? = null

    fun onEvent(event: TodoScreenEvent){
        when(event){
            is TodoScreenEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }

            TodoScreenEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }

            is TodoScreenEvent.OnCategoryClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CATEGORY_SCREEN + "?category=${event.category}"))
            }

            is TodoScreenEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        message = "Todo deleted",
                        action = "Undo"
                    ))
                }
            }

            is TodoScreenEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            status = Status.COMPLETED
                        )
                    )
                }

            }

            TodoScreenEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }













    private fun getCurrentDate(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currDate = Date()
        _currentDate.value = dateFormat.format(currDate)
    }



}