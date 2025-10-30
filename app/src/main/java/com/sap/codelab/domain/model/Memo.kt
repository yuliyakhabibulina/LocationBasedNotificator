package com.sap.codelab.domain.model

data class Memo(
    val id: Long = 0,
    val title: String,
    val description: String,
    val reminderDate: Long = 0,
    val reminderLatitude: Float,
    val reminderLongitude: Float,
    val isDone: Boolean = false
)