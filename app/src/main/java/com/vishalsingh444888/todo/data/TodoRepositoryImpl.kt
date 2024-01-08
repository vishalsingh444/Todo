package com.vishalsingh444888.todo.data

import kotlinx.coroutines.flow.Flow

class TodoRepositoryImp(
    private val dao: TodoDao
): TodoRepository {
    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)
    }

    override suspend fun getAllTodo(): Flow<List<Todo>> {
        return dao.getAllTodo()
    }

    override suspend fun getTodoByCategory(category: Category): Flow<List<Todo>> {
        return dao.getTodoByCategory(category);
    }

    override suspend fun getTodoByPriority(priority: Priority): Flow<List<Todo>> {
        return dao.getTodoByPriority(priority)
    }

    override suspend fun getTodoByDueDate(): Flow<List<Todo>> {
        return dao.getTodoByDueDate()
    }

    override suspend fun getTodayTodo(date: String): Flow<List<Todo>> {
        return dao.getTodayTodo(date)
    }
}