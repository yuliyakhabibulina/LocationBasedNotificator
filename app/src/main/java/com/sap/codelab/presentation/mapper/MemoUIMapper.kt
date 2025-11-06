package com.sap.codelab.presentation.mapper

import com.sap.codelab.domain.model.Memo
import com.sap.codelab.presentation.model.MemoUI

/**
 *maps memo ui to memo
 */
fun MemoUI.fromUI(): Memo {
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
 *maps memo ui from memo
 */
fun Memo.toUI(): MemoUI {
    return MemoUI(
        id = this.id,
        title = this.title,
        description = this.description,
        reminderLatitude = this.reminderLatitude,
        reminderLongitude = this.reminderLongitude,
        isDone = this.isDone
    )
}