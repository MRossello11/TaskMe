package com.tech.feature_tasks.presentation

import com.tech.feature_tasks.domain.model.Task

data class TaskListState(
    val taskList: List<Task> = listOf(),
    val input: String = "",
)
