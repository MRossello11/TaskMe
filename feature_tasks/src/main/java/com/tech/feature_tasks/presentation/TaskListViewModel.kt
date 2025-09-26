package com.tech.feature_tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.use_cases.CreateTaskUseCase
import com.tech.feature_tasks.domain.use_cases.DeleteTaskUseCase
import com.tech.feature_tasks.domain.use_cases.GetTasksUseCase
import com.tech.feature_tasks.domain.use_cases.SyncTasksUseCase
import com.tech.feature_tasks.domain.use_cases.UpdateTaskUseCase
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
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    syncTasksUseCase: SyncTasksUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(TaskListState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TaskListEvent>()
    val events: SharedFlow<TaskListEvent> = _events

    init {
        viewModelScope.launch(Dispatchers.IO) {
            syncTasksUseCase()
            getTasks()
        }
    }

    private fun getTasks() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { currentState ->
            currentState.copy(
                taskList = getTasksUseCase()
            )
        }
    }

    fun createTask() = viewModelScope.launch(Dispatchers.IO) {
        if (_uiState.value.input.isBlank()) return@launch

        val task = createTaskUseCase(_uiState.value.input)

        val newTaskList = arrayListOf<Task>()
        newTaskList.addAll(_uiState.value.taskList)
        newTaskList.add(task)

        // reset input
        _uiState.update { currentState ->
            currentState.copy(
                input = "",
                taskList = newTaskList
            )
        }
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

        updateTaskUseCase(task.copy(completed = !task.completed))
    }

    fun onTaskDelete(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.taskList.filterNot {
                it.id == task.id
            }
            currentState.copy(taskList = updatedTasks)
        }

        deleteTaskUseCase(task)
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