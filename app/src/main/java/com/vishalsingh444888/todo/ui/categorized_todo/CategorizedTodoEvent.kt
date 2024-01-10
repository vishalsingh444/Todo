package com.vishalsingh444888.todo.ui.categorized_todo

import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.ui.home.TodoScreenEvent

sealed class CategorizedTodoEvent {
    data class OnTodoClick(val todo: Todo): CategorizedTodoEvent()

    data class OnDoneClick(val todo: Todo): CategorizedTodoEvent()

    data class OnCategoryTitleChange(val newTitle: String): CategorizedTodoEvent()

    object OnSaveClick: CategorizedTodoEvent()

}
