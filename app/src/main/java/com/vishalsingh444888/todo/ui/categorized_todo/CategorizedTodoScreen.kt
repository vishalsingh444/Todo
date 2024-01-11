package com.vishalsingh444888.todo.ui.categorized_todo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.ui.home.components.TodoItem
import com.vishalsingh444888.todo.ui.home.getCategoryProperties
import com.vishalsingh444888.todo.util.UiEvent

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizedTodoScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () ->Unit,
    viewModel: CategorizedTodoViewModel = hiltViewModel(),
) {
    val todos = viewModel.todos.collectAsState()

    val properties = getCategoryProperties(todos.value, category = viewModel.category)

    val progress = if (properties[0] > 0) properties[1].toFloat() / properties[0] else 0f

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
//                        actionLabel = event.action
                    )
                }

                is UiEvent.Navigate -> onNavigate(event)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(CategorizedTodoEvent.OnSaveClick)
            }, shape = RoundedCornerShape(16.dp)) {
                Text(text = "Save", fontSize = 16.sp)
            }
        }
    ){ paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.category,
                onValueChange = {viewModel.onEvent(CategorizedTodoEvent.OnCategoryTitleChange(it))},
                singleLine = true,
                placeholder = {
                    Text(text = "Category", fontSize = 42.sp, fontWeight = FontWeight.SemiBold)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                textStyle = TextStyle(fontSize = 42.sp, fontWeight = FontWeight.Bold)
            )

            Text(text = "${properties[1]} | ${properties[0]} Tasks", color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "TODOS", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

            TodoList(todos = todos.value, onEvent =  viewModel::onEvent)
        }
    }
}

@Composable
fun TodoList(todos: List<Todo>, onEvent: (CategorizedTodoEvent) -> Unit) {
    LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
        items(todos) { todo ->
            TodoItem(todo = todo, modifier = Modifier
                .padding(vertical = 8.dp),
                onTodoClick = {onEvent(CategorizedTodoEvent.OnTodoClick(todo))},
                onDone = {onEvent(CategorizedTodoEvent.OnDoneClick(todo))}
            )
        }
    }
}
