package com.tech.feature_tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TaskListEvent {
    object VoiceRecognitionError: TaskListEvent
}

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TaskListState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TaskListEvent>()
    val events: SharedFlow<TaskListEvent> = _events

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
        if (_uiState.value.input.isBlank()) return@launch

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

    fun onTaskCompletedChanged(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.taskList.map {
                if (it.id == task.id) it.copy(completed = !it.completed) else it
            }
            currentState.copy(taskList = updatedTasks)
        }

        taskRepository.updateTask(task.copy(completed = !task.completed))
    }

    fun onTaskDelete(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.taskList.filterNot {
                it.id == task.id
            }
            currentState.copy(taskList = updatedTasks)
        }

        taskRepository.deleteTask(task.id)
    }

    fun onVoiceRecognitionFinished(text: String?, success: Boolean) {
        if (success && text != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    input = text
                )
            }
        } else {
            sendEvent(TaskListEvent.VoiceRecognitionError)
        }
    }

    private fun sendEvent(event: TaskListEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}