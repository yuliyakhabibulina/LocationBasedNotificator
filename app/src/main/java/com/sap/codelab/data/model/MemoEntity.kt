package com.sap.codelab.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a memo.
 */
@Entity(tableName = "memo")
data class MemoEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "reminderDate")
    var reminderDate: Long,         // for deleting
    @ColumnInfo(name = "reminderLatitude")
    var reminderLatitude: Float,
    @ColumnInfo(name = "reminderLongitude")
    var reminderLongitude: Float,
    @ColumnInfo(name = "isDone")
    var isDone: Boolean = false
)