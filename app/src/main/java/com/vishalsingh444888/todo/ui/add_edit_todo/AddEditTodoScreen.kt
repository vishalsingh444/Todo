package com.vishalsingh444888.todo.ui.add_edit_todo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vishalsingh444888.todo.data.Priority
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.util.UiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember {
        BringIntoViewRequester()
    }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedStatus by remember{ mutableStateOf(Status.NEW) }
    
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

                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTodoEvent.OnSaveClick)
            }) {
                Text(text = "Save", fontSize = 12.sp)
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .padding(paddingValue)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.dueDate,
                    onValueChange = { viewModel.onEvent(AddEditTodoEvent.OnDueDateChange(it)) },
                    placeholder = {
                        Text(text = "DD-MM-YYYY")
                    },
                    label = {
                        Text(text = "Due-date")    
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = viewModel.category,
                    onValueChange = { viewModel.onEvent(AddEditTodoEvent.OnCategoryChange(it)) },
                    placeholder = {
                        Text(text = "Category")
                    },
                    label = {
                        Text(text = "Category")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true

                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Priority", fontSize = 12.sp,color = Color.Gray,modifier = Modifier.padding(horizontal = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Priority.values().forEach { priority ->
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            RadioButton(
                                selected = selectedPriority == priority,
                                onClick = {
                                    selectedPriority = priority
                                    viewModel.onEvent(AddEditTodoEvent.OnPriorityChange(selectedPriority))
                                }
                            )
                            val name = when(priority.name){
                                "LOW" -> "Low"
                                "MEDIUM" -> "Medium"
                                "HIGH" -> "High"
                                else -> ""
                            }
                            Text(text = name, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
                
                Text(text = "Status", fontSize = 12.sp,color = Color.Gray,modifier = Modifier.padding(horizontal = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                    Status.values().forEach { status ->
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatus == status,
                                onClick = {
                                    selectedStatus = status
                                    viewModel.onEvent(AddEditTodoEvent.OnStatusChange(selectedStatus))
                                }
                            )
                            val name = when(status.name){
                                "NEW" -> "New"
                                "IN_PROGRESS" -> "In progress"
                                "COMPLETED" -> "Completed"
                                else -> ""
                            }
                            Text(text = name, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
                OutlinedTextField(
                    value = viewModel.title,
                    onValueChange = {
                        viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
                    },
                    placeholder = {
                        Text(text = "Title")
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = {
                        viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                                    },
                    placeholder = {
                        Text(text = "Description")
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .height(400.dp)
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    delay(400) // delay to way the keyboard shows up
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        }
                )
            }
        }
    }

}