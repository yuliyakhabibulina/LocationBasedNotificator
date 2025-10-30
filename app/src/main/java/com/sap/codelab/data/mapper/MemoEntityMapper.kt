package com.sap.codelab.data.mapper

import com.sap.codelab.data.model.MemoEntity
import com.sap.codelab.domain.model.Memo

internal fun MemoEntity.fromEntity(): Memo {
    return Memo(
        id = this.id,
        title = this.title,
        description = this.description,
        reminderDate = this.reminderDate,
        reminderLatitude = this.reminderLatitude,
        reminderLongitude = this.reminderLongitude,
        isDone = this.isDone
    )
}

internal fun Memo.toEntity(): MemoEntity {
    return MemoEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        reminderDate = this.reminderDate,
        reminderLatitude = this.reminderLatitude,
        reminderLongitude = this.reminderLongitude,
        isDone = this.isDone
    )
}