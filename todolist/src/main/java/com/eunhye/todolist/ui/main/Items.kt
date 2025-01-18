package com.eunhye.todolist.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eunhye.todolist.R
import com.eunhye.todolist.domain.model.Todo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Items(
    todo: Todo,
    onClick: (uid: Int) -> Unit,
    onDeleteClick: (uid: Int) -> Unit,
) {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        Modifier
            .padding(8.dp)
            .clickable { onClick(todo.uid) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onDeleteClick(todo.uid) },
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = null,
                tint = Color(0xFFA51212),
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None),
                )
                Text(
                    text = format.format(Date(todo.date)),
                    style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None),
                )
            }
            if (todo.isDone.not()) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = null,
                    tint = Color(0xFF00BCD4),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsPreview() {
    Items(
        todo = Todo(
            "양치하기",
            Date().time,
            false,
        ),
        onClick = {},
        onDeleteClick = {}
    )
}
