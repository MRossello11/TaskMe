package com.tech.feature_tasks.domain.model

data class Task(
    val id: String = "",
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val deleted: Boolean = false
)