package com.eunhye.todolist.domain.repository

import com.eunhye.todolist.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun observeTodos() : Flow<List<Todo>>

    suspend fun addTodo(todo : Todo)

    suspend fun updateTodo(todo : Todo)

    suspend fun deleteTodo(todo : Todo)
}
