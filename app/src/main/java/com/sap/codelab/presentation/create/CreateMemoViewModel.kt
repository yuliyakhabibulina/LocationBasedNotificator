package com.sap.codelab.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.R
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
@HiltViewModel
internal class CreateMemoViewModel @Inject constructor(
    private val saveMemoUseCase: SaveMemoUseCase
) : ViewModel() {

    private val _errorMessageId = MutableStateFlow(R.string.empty)
    val errorMessageId = _errorMessageId.asStateFlow()

    private val _navBackEvent = MutableStateFlow(false)
    val navBackEvent = _navBackEvent.asStateFlow()

    fun onSaveMenuClicked(title: String, description: String) {
        if (title.isEmpty()) {
            _errorMessageId.value = R.string.memo_title_empty_error
        } else if (description.isEmpty()) {
            _errorMessageId.value = R.string.memo_text_empty_error
        } else {
            val memo = Memo(
                title = title,
                description = description,
                id = 0,
                reminderDate = 0,
                reminderLatitude = 0F,
                reminderLongitude = 0F,
                isDone = false
            )
            viewModelScope.launch(Dispatchers.IO) {
                saveMemoUseCase.invoke(memo)
                    .also {
                        _navBackEvent.value = true
                    }
            }

        }
    }

}