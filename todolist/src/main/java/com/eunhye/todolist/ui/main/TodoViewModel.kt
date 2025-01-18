package com.eunhye.todolist.ui.main

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eunhye.todolist.domain.model.Todo
import kotlinx.coroutines.launch

class TodoViewModel(
    application: Application,
    private val repository: com.eunhye.todolist.domain.repository.TodoRepository
) : AndroidViewModel(application) {

    private val _items = mutableStateOf(emptyList<Todo>())
    var items: State<List<Todo>> = _items

    private var recentlyDeleteTodo: Todo? = null

    init {
        viewModelScope.launch {
            repository.observeTodos()
                .collect { todos ->
                    _items.value = todos
                }
        }
    }

    fun addTodo(text: String) {
        viewModelScope.launch {
            repository.addTodo(Todo(title = text))
        }
    }

    fun toogle(index: Int) {
        val todo = _items.value.find { todo -> todo.uid == index }
        todo?.let {
            viewModelScope.launch {
                repository.updateTodo(
                    it.copy(isDone = !it.isDone).apply { this.uid = it.uid }
                )
            }
        }
    }

    fun delete(index: Int) {
        val todo = _items.value.find { todo -> todo.uid == index }
        todo?.let {
            viewModelScope.launch {
                repository.deleteTodo(it)
                recentlyDeleteTodo = it
            }
        }
    }

    fun restoreTodo() {
        viewModelScope.launch {
            repository.addTodo(recentlyDeleteTodo ?: return@launch)
            recentlyDeleteTodo = null
        }
    }
}
