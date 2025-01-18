package com.eunhye.todolist.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eunhye.todolist.data.repository.TodoRepositoryImpl
import com.eunhye.todolist.domain.repository.TodoRepository
import com.eunhye.todolist.ui.main.TodoViewModel

class TodoAndroidViewModelFactory(
    private val application: Application,
    private val todoRepository: TodoRepository = TodoRepositoryImpl(application),
    ) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(application, todoRepository) as T
        }
        return super.create(modelClass)
    }
}
