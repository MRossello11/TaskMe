package com.tech.feature_tasks.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun TaskItem(
    title: String,
    completed: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = completed,
            onCheckedChange = { onCheckedChange(it) }
        )

        Text(
            text = title,
            style = TextStyle(
                textDecoration = if (completed) TextDecoration.LineThrough else  TextDecoration.None,
                color = if (completed) Color.Gray else Color.Unspecified
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Spacer(Modifier.height(5.dp))
}
