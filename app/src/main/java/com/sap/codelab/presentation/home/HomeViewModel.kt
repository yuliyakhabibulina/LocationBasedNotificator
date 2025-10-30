package com.sap.codelab.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.usecases.GetAllMemoUseCase
import com.sap.codelab.domain.usecases.GetOpenUseCase
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home Activity.
 */

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase,
    private val getOpenUseCase: GetOpenUseCase,
    private val saveMemoUseCase: SaveMemoUseCase
) : ViewModel() {

    private var isShowAll = false

    private val _memos: MutableStateFlow<List<Memo>> = MutableStateFlow(listOf())
    val memos: StateFlow<List<Memo>> = _memos


    /**
     * Loads all memos.
     */
    fun loadAllMemos() {
        isShowAll = true
        viewModelScope.launch(Dispatchers.IO) {
            getAllMemoUseCase.invoke()
                .collect {memo -> _memos.value = memo }
        }
    }

    /**
     * Loads all open (not done) memos.
     */
    fun loadOpenMemos() {
        isShowAll = false
        viewModelScope.launch(Dispatchers.IO) {
            getOpenUseCase.invoke()
                .collect {memo -> _memos.value = memo }
        }
    }

    fun refreshMemos() {
        if (isShowAll) {
            loadAllMemos()
        } else {
            loadOpenMemos()
        }
    }

    /**
     * Updates the given memo, marking it as done if isChecked is true.
     *
     * @param memoEntity      - the memo to update.
     * @param isChecked - whether the memo has been checked (marked as done).
     */
    fun updateMemo(memoEntity: Memo, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // We'll only forward the update if the memo has been checked, since we don't offer to uncheck memos right now
            if (isChecked) {
                saveMemoUseCase.invoke(memoEntity.copy(isDone = true))
            }
        }

    }
}