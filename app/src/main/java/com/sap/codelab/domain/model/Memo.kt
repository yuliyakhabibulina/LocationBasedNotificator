package com.sap.codelab.domain.model

data class Memo(
    val id: Long = 0,
    val title: String,
    val description: String,
    val reminderLatitude: Double,
    val reminderLongitude: Double,
    val isDone: Boolean = false
)