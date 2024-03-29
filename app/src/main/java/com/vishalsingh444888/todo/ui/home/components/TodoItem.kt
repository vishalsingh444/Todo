package com.vishalsingh444888.todo.ui.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishalsingh444888.todo.R
import com.vishalsingh444888.todo.data.Priority
import com.vishalsingh444888.todo.data.Status
import com.vishalsingh444888.todo.data.Todo
import com.vishalsingh444888.todo.ui.categorized_todo.CategorizedTodoEvent
import com.vishalsingh444888.todo.ui.home.TodoScreenEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TodoItem(
    todo: Todo,
    modifier:Modifier = Modifier,
    onDone: () -> Unit = {},
    onTodoClick: () -> Unit = {}
) {
    val priority = when(todo.priority){
        Priority.LOW -> "Low"
        Priority.MEDIUM -> "Medium"
        Priority.HIGH -> "High"
    }
    Card(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(1.dp, color = Color.White, shape = RoundedCornerShape(16.dp))
             ,
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.background),
        onClick = {onTodoClick()}

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
            
        ) {
            IconButton(onClick = { onDone() }) {
                if(todo.status != Status.COMPLETED){
                    Icon(painter = painterResource(id = R.drawable.circle_24px), contentDescription = "Task incomplete", tint = MaterialTheme.colorScheme.secondary)
                }else{
                    Icon(painter = painterResource(id = R.drawable.check_circle_filled), contentDescription = "Task completed", tint = MaterialTheme.colorScheme.secondary)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = todo.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,modifier = Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis, maxLines = 1)
                    Text(text = todo.dueDate, fontSize = 12.sp,)
                }
                Spacer(Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = todo.category, fontSize = 12.sp)
                    Text(text = "priority: $priority", fontSize = 12.sp)
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun  TodoItemPreview(){
//    val todo = Todo(
//        title = "Go to Gymdfdsjfdslfdskjflskdjsdjfslkdfkhdsjfhsdjkdfjdk",
//        description = "Go to Gym from 7 - 9",
//        dueDate = "09/01/2024",
//        priority = Priority.HIGH,
//        category = "Health",
//        status = Status.COMPLETED
//    )
//    TodoItem(todo = todo, {})
//}