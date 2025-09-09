package com.tech.feature_tasks.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val completed: Boolean = false
)