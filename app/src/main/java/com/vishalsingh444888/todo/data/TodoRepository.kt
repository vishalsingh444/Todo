package com.vishalsingh444888.todo.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow


interface TodoRepository {
    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)
    
    suspend fun updateTodo(todo: Todo)

    suspend fun getTodoById(id: Int): Todo?

    fun getAllTodo(): Flow<List<Todo>>

    fun getTodoByCategory(category: String): Flow<List<Todo>>

    fun getTodoByPriority(priority: Priority): Flow<List<Todo>>

    fun getTodoByDueDate(): Flow<List<Todo>>

    fun getTodayTodo(date: String): Flow<List<Todo>>

    fun getAllCategories(): Flow<List<String>>

    suspend fun getTodoByTitle(title: String): Flow<List<Todo>>
}