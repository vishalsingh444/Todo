package com.vishalsingh444888.todo.ui.categorized_todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.vishalsingh444888.todo.ui.home.TodoList
import com.vishalsingh444888.todo.ui.home.TodoScreenEvent
import com.vishalsingh444888.todo.ui.home.TodoScreenViewModel
import com.vishalsingh444888.todo.ui.home.getCategoryProperties
import com.vishalsingh444888.todo.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizedTodoScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () ->Unit,
    viewModel: CategorizedTodoViewModel = hiltViewModel(),
    todoScreenViewModel: TodoScreenViewModel = hiltViewModel()
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
            }) {
                Text(text = "Save")
            }
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
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
                color = Color.Blue,
                trackColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "TODOS", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

            TodoList(todos = todos.value, onEvent =  todoScreenViewModel::onEvent)
        }
    }
}
