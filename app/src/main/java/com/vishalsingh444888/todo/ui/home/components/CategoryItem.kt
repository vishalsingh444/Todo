package com.vishalsingh444888.todo.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishalsingh444888.todo.R
import com.vishalsingh444888.todo.ui.home.TodoScreenEvent
import com.vishalsingh444888.todo.ui.theme.CheckGreen
import com.vishalsingh444888.todo.ui.theme.Purple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(
    categoryName: String,
    onEvent: (TodoScreenEvent) -> Unit,
    index: Int,
    totalTask: Int,
    completedTask: Int,
    modifier: Modifier = Modifier
) {
    val imageId = getImageId(index)
    val progress = if (totalTask > 0) completedTask.toFloat() / totalTask else 0f
    Card(
        modifier = modifier
            .size(width = 140.dp, height = 140.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(Color.Red),
        onClick = { onEvent(TodoScreenEvent.OnCategoryClick(category = categoryName)) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.a_black_image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = categoryName,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "$totalTask Tasks", fontSize = 14.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.White
                )
            }
        }

    }
}

fun getImageId(index: Int): Int {
    val imageIds = listOf(
        R.drawable.confetti_doodles,
        R.drawable.dragon_scales,
        R.drawable.diamond_sunset,
        R.drawable.flat_mountains,
        R.drawable.endless_constellation,
        R.drawable.liquid_cheese,
        R.drawable.subtle_prism
    )
    return imageIds[index % imageIds.size]
}

@Preview
@Composable
fun CategoryItemPreview() {
    CategoryItem(categoryName = "Work", {}, 6, 40, 30)
}