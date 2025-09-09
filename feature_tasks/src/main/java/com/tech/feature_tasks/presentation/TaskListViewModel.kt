package com.tech.feature_tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TaskListState())
    val uiState: StateFlow<TaskListState> = _uiState.asStateFlow()

    init {
        getTasks()
    }

    private fun getTasks() = viewModelScope.launch(Dispatchers.IO) {
        val tasks = taskRepository.getTasks()
        _uiState.update { currentState ->
            currentState.copy(
                taskList = tasks
            )
        }
    }

    fun createTask() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.createTask(
            Task(
                title = _uiState.value.input
            )
        )

        // reset input
        _uiState.update { currentState ->
            currentState.copy(
                input = ""
            )
        }
        getTasks()
    }

    fun onTaskTextChanged(newInput: String) {
        _uiState.update { currentState ->
            currentState.copy(
                input = newInput
            )
        }
    }
}