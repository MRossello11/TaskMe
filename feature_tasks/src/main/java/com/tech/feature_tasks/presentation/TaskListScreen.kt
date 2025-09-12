package com.tech.feature_tasks.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tech.feature_tasks.presentation.components.TaskItemSwipeable

@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Voice Input Button
            IconButton(onClick = {  }) { // todo
                Icon(
                    imageVector = Icons.Default.Phone, // todo
                    contentDescription = "Voice Input"
                )
            }

            // Task Input Field
            TextField(
                value = uiState.input,
                onValueChange = { it ->
                    viewModel.onTaskTextChanged(it)
                },
                placeholder = { Text("Enter a task") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                singleLine = true
            )

            // Add Task Button
            IconButton(onClick = { viewModel.createTask() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }

        // Task List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = uiState.taskList, key = { it.id }) { task ->
                TaskItemSwipeable(
                    title = task.title,
                    completed = task.completed,
                    onCheckedChange = { checked ->
                        viewModel.onTaskCompletedChanged(task)
                    },
                    onDelete = {
                        viewModel.onTaskDelete(task)
                    }
                )
            }
        }
    }
}
