package com.sap.codelab.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.domain.usecases.GetAllMemoUseCase
import com.sap.codelab.domain.usecases.GetOpenUseCase
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import com.sap.codelab.presentation.model.MemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase,
    private val getOpenUseCase: GetOpenUseCase,
    private val saveMemoUseCase: SaveMemoUseCase
) : ViewModel() {

    private var isShowAll = false

    private val _memos: MutableStateFlow<List<MemoUI>> = MutableStateFlow(listOf())
    val memos: StateFlow<List<MemoUI>> = _memos

    /**
     * Loads all memos from the database.
     */
    fun loadAllMemos() {
        isShowAll = true
        viewModelScope.launch(Dispatchers.IO) {
            getAllMemoUseCase.invoke()
                .collect {memo -> _memos.value = memo }
        }
    }

    /**
     * Loads the open memos from the database.
     */
    fun loadOpenMemos() {
        isShowAll = false
        viewModelScope.launch(Dispatchers.IO) {
            getOpenUseCase.invoke()
                .collect {memo -> _memos.value = memo }
        }
    }

    /**
     * Updates the given memo, marking it as done if isChecked is true.
     *
     * @param memo      - the memo to update.
     * @param isChecked - whether the memo has been checked (marked as done).
     */
    fun updateMemo(memo: MemoUI, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
             if (isChecked) {
                saveMemoUseCase.invoke(memo.copy(isDone = true))
            }
        }

    }
}