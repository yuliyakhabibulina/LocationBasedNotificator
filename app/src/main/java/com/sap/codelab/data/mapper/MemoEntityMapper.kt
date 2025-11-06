package com.sap.codelab.data.mapper

import com.sap.codelab.data.model.MemoEntity
import com.sap.codelab.domain.model.Memo

/**
 * @map form entity to domain model
 */
fun MemoEntity.fromEntity(): Memo {
    return Memo(
        id = this.id,
        title = this.title,
        description = this.description,
        reminderLatitude = this.reminderLatitude,
        reminderLongitude = this.reminderLongitude,
        isDone = this.isDone
    )
}

/**
 * @map to entity from domain model
 */
fun Memo.toEntity(): MemoEntity {
    return MemoEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        reminderLatitude = this.reminderLatitude,
        reminderLongitude = this.reminderLongitude,
        isDone = this.isDone
    )
}