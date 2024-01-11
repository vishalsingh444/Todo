package com.vishalsingh444888.todo.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalsingh444888.todo.data.Priority
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.data.TodoRepository
import com.vishalsingh444888.todo.util.CurrentDate
import com.vishalsingh444888.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {


    val date = CurrentDate.Date
    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var priority by mutableStateOf(Priority.MEDIUM)
        private set
    var dueDate by mutableStateOf(date)
        private set

    var status by mutableStateOf(Status.NEW)
        private set

    var category by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var todoId by mutableStateOf(-1)
        private set
    init {

        todoId = savedStateHandle.get<String>("todoId")!!.toInt()

        if(todoId != -1){
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description
                    dueDate = todo.dueDate
                    status = todo.status
                    category = todo.category
                    this@AddEditTodoViewModel.todo = todo
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent){
        when(event){
            is AddEditTodoEvent.OnCategoryChange -> {
                category = event.category
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddEditTodoEvent.OnDueDateChange -> {
                dueDate = event.dueDate
            }
            AddEditTodoEvent.OnSaveClick -> {
                viewModelScope.launch {
                    if(title.isBlank() || description.isBlank() || category.isBlank()){
                        sendUiEvent(UiEvent.ShowSnackbar(
                            message = "Title,desc and Category can't be empty"
                        ))
                        return@launch
                    }
                    category = category.trim().split(" ").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }.joinToString(" ")
                    if(todoId!=1){
                        repository.insertTodo(
                            Todo(
                                title = title,
                                description = description,
                                dueDate = dueDate,
                                status = status,
                                category = category,
                                priority = priority
                            )
                        )
                    }else{
                        repository.insertTodo(
                            Todo(
                                id = todoId,
                                title = title,
                                description = description,
                                dueDate = dueDate,
                                status = status,
                                category = category,
                                priority = priority
                            )
                        )
                    }
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
            is AddEditTodoEvent.OnStatusChange -> {
                status = event.status
            }
            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title
            }

            is AddEditTodoEvent.OnPriorityChange -> {
                priority = event.priority
            }

            is AddEditTodoEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    todo?.let { todo: Todo ->  repository.deleteTodo(todo) }
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