package com.tech.feature_tasks.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun TaskItemSwipeable(
    title: String,
    completed: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false, // only allow right-to-left
        backgroundContent = {
            if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            }
        },
        content = {
            TaskItem(
                title = title,
                completed = completed,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
