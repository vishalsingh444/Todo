package com.vishalsingh444888.todo.ui.home

import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo

sealed class TodoScreenEvent {
    data class OnDeleteTodoClick(val todo: Todo): TodoScreenEvent()

    data class OnDoneChange(val todo:Todo,val isCompleted: Boolean): TodoScreenEvent()

    object OnUndoDeleteClick: TodoScreenEvent()

    data class OnTodoClick(val todo:Todo):TodoScreenEvent()

    object OnAddTodoClick: TodoScreenEvent()

    data class OnCategoryClick(val category: String): TodoScreenEvent()
}
