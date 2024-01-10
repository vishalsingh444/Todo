package com.vishalsingh444888.todo.util

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

object Category {
    private val _allCategory = mutableStateOf(listOf("Work","Personal","Health","Education","Family"))
    val allCategory: State<List<String>> = _allCategory

    fun updateCategories(newCategory: String){
        val newCategories = allCategory.value.toMutableList()
        if(!newCategory.contains(newCategory)){
            newCategories.add(newCategory)
            _allCategory.value = newCategories
            Log.d("category" ,"${_allCategory.value}")
        }
    }
}