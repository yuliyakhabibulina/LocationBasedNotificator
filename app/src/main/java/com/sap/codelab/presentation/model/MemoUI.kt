package com.sap.codelab.presentation.model

/**
 * Data class representing a memo UI object.
 */
data class MemoUI(
    val id : Long = 0,
    val title: String = "",
    val description: String = "",
    val reminderLatitude: Double = 0.0,
    val reminderLongitude: Double = 0.0,
    val isDone: Boolean = false
)