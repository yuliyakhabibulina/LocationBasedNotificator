package com.sap.codelab.presentation.model

data class MemoUI(
    val id : Long = 0,
    val title: String = "",
    val description: String = "",
    val reminderLatitude: Double = 0.0,
    val reminderLongitude: Double = 0.0,
    val isDone: Boolean = false
)