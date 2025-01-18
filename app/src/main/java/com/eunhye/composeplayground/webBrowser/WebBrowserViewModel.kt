package com.eunhye.composeplayground.webBrowser

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WebBrowserViewModel : ViewModel() {
    val url = mutableStateOf("https://www.google.com")

    private val _undoSharedFlow = MutableSharedFlow<Boolean>()
    val undoSharedFlowv = _undoSharedFlow.asSharedFlow()

    private val _redoSharedFlow = MutableSharedFlow<Boolean>()
    val redoSharedFlowv = _redoSharedFlow.asSharedFlow()

    fun undo(){
        viewModelScope.launch {
            _undoSharedFlow.emit(true)
        }
    }

    fun redo() {
        viewModelScope.launch {
            _redoSharedFlow.emit(true)
        }
    }
}
