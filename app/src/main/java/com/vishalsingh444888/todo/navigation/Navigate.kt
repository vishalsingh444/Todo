package com.vishalsingh444888.todo.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.util.newStringBuilder
import com.vishalsingh444888.todo.ui.add_edit_todo.AddEditTodoScreen
import com.vishalsingh444888.todo.ui.categorized_todo.CategorizedTodoScreen
import com.vishalsingh444888.todo.ui.home.TodoScreen
import com.vishalsingh444888.todo.util.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigate() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.TODO_SCREEN){
        composable(route = Routes.TODO_SCREEN){
            TodoScreen(onNavigate = { navController.navigate(it.route) })
        }
        composable(
            route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
            arguments = listOf(
                navArgument(name = "todoID"){
                    type = NavType.StringType
                    defaultValue = "-1"
                }
            )
        ){
            AddEditTodoScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
        composable(
            route = Routes.CATEGORY_SCREEN + "?category={category}",
            arguments = listOf(
                navArgument(name = "category"){
                    type = NavType.StringType
                }
            )
        ){
            CategorizedTodoScreen(onNavigate = {navController.navigate(it.route)}, onPopBackStack = {navController.popBackStack()})
        }
    }
}