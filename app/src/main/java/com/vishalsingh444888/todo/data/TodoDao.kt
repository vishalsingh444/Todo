package com.vishalsingh444888.todo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Query("SELECT * FROM todo")
    fun getAllTodo(): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE category = :category")
    fun getTodoByCategory(category: String): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE priority = :priority")
    fun getTodoByPriority(priority: Priority): Flow<List<Todo>>

    @Query("SELECT * FROM todo ORDER BY dueDate ASC")
    fun getTodoByDueDate(): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    @Query("SELECT * FROM todo WHERE dueDate = :date")
    fun getTodayTodo(date: String): Flow<List<Todo>>
}