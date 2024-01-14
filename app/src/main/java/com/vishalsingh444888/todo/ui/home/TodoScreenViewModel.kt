package com.vishalsingh444888.todo.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.data.TodoRepository
import com.vishalsingh444888.todo.util.CurrentDate
import com.vishalsingh444888.todo.util.Routes
import com.vishalsingh444888.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TodoScreenViewModel @Inject constructor(
    private val repository: TodoRepository
): ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)

    private val _currentTime = mutableStateOf("")
    @RequiresApi(Build.VERSION_CODES.O)
    val CurrentTime:State<String> = _currentTime

    init{
        startUpdatingTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startUpdatingTime() {
        GlobalScope.launch {
            while (true){
                delay(1000)
                _currentTime.value = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            }
        }
    }

    val date = CurrentDate.Date

    val categories = repository.getAllCategories()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    val todayTodos = repository.getTodayTodo(date)
    val allTodos = repository.getAllTodo()

    private var deletedTodo: Todo? = null

    private val _searchedTodo = mutableStateOf<List<Todo>>(emptyList())
    val searchTodo: State<List<Todo>> = _searchedTodo

    fun onEvent(event: TodoScreenEvent){
        when(event){
            is TodoScreenEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id.toString()}"))
            }

            TodoScreenEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=-1"))
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
                    val isCompleted = event.todo.status==Status.NEW||event.todo.status==Status.IN_PROGRESS
                    val status = if (isCompleted) Status.COMPLETED else Status.IN_PROGRESS
                    repository.insertTodo(
                        event.todo.copy(
                            status = status
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

            is TodoScreenEvent.OnSearchClick -> {
                viewModelScope.launch {
                    repository.getTodoByTitle(event.title).collect{
                        _searchedTodo.value = it
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
}