package com.sap.codelab.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sap.codelab.R
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import com.sap.codelab.presentation.model.MemoUI
import com.sap.codelab.utils.extensions.isEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private val _locationState = MutableStateFlow(LatLng(0.0, 0.0))

    /**
     * Handles the save menu click and adds a new memo to the database.
     */
    fun onSaveMenuClicked(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val errorMessage = when {
                title.isEmpty() -> R.string.memo_title_empty_error
                description.isEmpty() -> R.string.memo_text_empty_error
                _locationState.value.isEmpty() -> R.string.memo_location_empty_error
                else -> null
            }
            if (errorMessage != null) {
                _errorMessageId.send(errorMessage)
            } else {
                val memo = MemoUI(
                    title = title,
                    description = description,
                    id = 0,
                    reminderLatitude = _locationState.value.latitude,
                    reminderLongitude = _locationState.value.longitude,
                    isDone = false
                )
                saveMemoUseCase.invoke(memo)
                    .also {
                        _navBackEvent.send(true)
                    }
            }
        }
    }

    /**
     * Handles the location selection.
     */
    fun onLocationSelected(lng: LatLng) {
        _locationState.update { LatLng(lng.latitude, lng.longitude) }
    }

}