package com.vishalsingh444888.todo.ui.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.ui.home.components.CategoryItem
import com.vishalsingh444888.todo.ui.home.components.TodoItem
import com.vishalsingh444888.todo.util.UiEvent
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoScreenViewModel = hiltViewModel()
) {
    val allCategory = viewModel.categories.collectAsState(emptyList())

    val todayTodo = viewModel.todayTodos.collectAsState(emptyList()).value

    val allTodo = viewModel.allTodos.collectAsState(emptyList()).value

    val snackbarHostState = remember { SnackbarHostState() }

    val currentTime by viewModel.CurrentTime

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoScreenEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(TodoScreenEvent.OnAddTodoClick)
                }, shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),

            ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hello,",
                    fontSize = 42.sp,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    fontSize = 42.sp,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = "CATEGORIES",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            CategoryList(
                allTodo = allTodo,
                onEvent = viewModel::onEvent,
                allCategory = allCategory.value
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (todayTodo.isNotEmpty()) {
                    Text(
                        text = "TODAY'S TODO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,

                        )
                    Spacer(modifier = Modifier.height(16.dp))
                    TodoList(todos = todayTodo, onEvent = viewModel::onEvent)
                    Spacer(Modifier.height(16.dp))
                }
                Text(
                    text = "ALL TODOS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,

                    )
                Spacer(Modifier.height(16.dp))
                if (allTodo.isNotEmpty()) {
                    TodoList(todos = allTodo, onEvent = viewModel::onEvent)
                } else {
                    NoTodo()
                }
            }
        }
    }
}

@Composable
fun NoTodo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NO TODOS",
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun TodoList(todos: List<Todo>, onEvent: (TodoScreenEvent) -> Unit) {
    LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
        items(todos) { todo ->
            TodoItem(
                todo = todo,
                modifier = Modifier
                    .padding(vertical = 8.dp),
                onDone = {onEvent(TodoScreenEvent.OnDoneChange(todo))},
                onTodoClick = {onEvent(TodoScreenEvent.OnTodoClick(todo))}
            )
        }
    }
}

@Composable
fun CategoryList(
    allTodo: List<Todo>,
    allCategory: List<String>,
    onEvent: (TodoScreenEvent) -> Unit
) {
    LazyRow {
        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
        items(allCategory) { category ->
            val properties = getCategoryProperties(allTodo, category)
            val index = allCategory.indexOf(category)
            CategoryItem(
                categoryName = category,
                onEvent = onEvent,
                index = index,
                totalTask = properties[0],
                completedTask = properties[1],
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

fun getCategoryProperties(allTodo: List<Todo>, category: String): Array<Int> {
    val totalTodos = allTodo.filter { todo -> todo.category == category }
    val completedTodos = totalTodos.filter { todo -> todo.status == Status.COMPLETED }
    return arrayOf(totalTodos.size, completedTodos.size)
}


//@Preview
//@Composable
//fun TodoScreenPreview() {
//    TodoScreen(onNavigate = {})
//}