package com.sap.codelab.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.R
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching CreateMemo view. Handles user interactions.
 */
@HiltViewModel
internal class CreateMemoViewModel @Inject constructor(
    private val saveMemoUseCase: SaveMemoUseCase
) : ViewModel() {

    private val _errorMessageId = Channel<Int>()
    val errorMessageId = _errorMessageId.receiveAsFlow()

    private val _navBackEvent = Channel<Boolean>()
    val navBackEvent = _navBackEvent.receiveAsFlow()

    fun onSaveMenuClicked(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (title.isEmpty()) {
                _errorMessageId.send(R.string.memo_title_empty_error)
            } else if (description.isEmpty()) {
                _errorMessageId.send(R.string.memo_text_empty_error)
            } else {
                val memo = Memo (
                    title = title,
                    description = description,
                    id = 0,
                    reminderLatitude = 0.0,
                    reminderLongitude = 0.0,
                    isDone = false
                )
                saveMemoUseCase.invoke(memo)
                    .also {
                        _navBackEvent.send(true)
                    }
            }
        }
    }

}