package com.vishalsingh444888.todo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CurrentDate {
    val Date = getCurrentDate()
    private fun getCurrentDate():String{
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currDate = Date()
        return dateFormat.format(currDate)
    }
}