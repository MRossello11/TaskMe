package com.tech.feature_tasks.presentation

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tech.core.R
import com.tech.feature_tasks.presentation.components.TaskItemSwipeable
import java.util.Locale

@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val voiceLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.onVoiceRecognitionFinished(
            text = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0),
            success = it.resultCode == Activity.RESULT_OK
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                TaskListEvent.VoiceRecognitionError -> {
                    snackbarHostState.showSnackbar("There was an error during voice recognition")
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(16.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Voice Input Button
                IconButton(onClick = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
                    voiceLauncher.launch(intent)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.mic),
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
}
