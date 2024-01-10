package com.vishalsingh444888.todo.ui.add_edit_todo

import com.vishalsingh444888.todo.data.Priority
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.util.CurrentDate

sealed class AddEditTodoEvent{


    data class OnTitleChange(val title: String): AddEditTodoEvent()

    data class OnDescriptionChange(val description: String): AddEditTodoEvent()

    data class OnPriorityChange(val priority: Priority): AddEditTodoEvent()

    data class OnDueDateChange(val dueDate: String): AddEditTodoEvent()

    data class OnStatusChange(val status: Status): AddEditTodoEvent()

    data class OnCategoryChange(val category: String): AddEditTodoEvent()

    object OnSaveClick: AddEditTodoEvent()

    object OnDeleteClick: AddEditTodoEvent()
}
