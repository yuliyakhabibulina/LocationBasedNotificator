package com.sap.codelab.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import com.sap.codelab.utils.extensions.getEmptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
@HiltViewModel
internal class CreateMemoViewModel @Inject constructor(
    private val saveMemoUseCase: SaveMemoUseCase
) : ViewModel() {

    private var memo = Memo(0, getEmptyString(), getEmptyString(), 0, 0F, 0F, false)

    /**
     * Saves the memo in it's current state.
     */
    fun saveMemo() {
        viewModelScope.launch(Dispatchers.IO) {
            saveMemoUseCase.invoke(memo.copy(isDone = true))
        }
    }

    /**
     * Call this method to update the memo. This is usually needed when the user changed his input.
     */
    fun updateMemo(title: String, description: String) {
        memo = Memo(
            title = title,
            description = description,
            id = 0,
            reminderDate = 0,
            reminderLatitude = 0F,
            reminderLongitude = 0F,
            isDone = false
        )
    }

    /**
     * @return true if the title and content are not blank; false otherwise.
     */
    fun isMemoValid(): Boolean = memo.title.isNotBlank() && memo.description.isNotBlank()

    /**
     * @return true if the memo text is blank, false otherwise.
     */
    fun hasTextError() = memo.description.isBlank()

    /**
     * @return true if the memo title is blank, false otherwise.
     */
    fun hasTitleError() = memo.title.isBlank()
}