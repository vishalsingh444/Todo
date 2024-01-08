package com.vishalsingh444888.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description : String,
    val dueDate: String,
    val priority: Priority,
    val status: Status,
    val category: String
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

enum class Status {
    NEW,
    IN_PROGRESS,
    COMPLETED
}
