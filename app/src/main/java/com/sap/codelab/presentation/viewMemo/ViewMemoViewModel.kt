package com.sap.codelab.presentation.viewMemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.domain.usecases.GetMemoByIdUseCase
import com.sap.codelab.presentation.model.MemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for matching ViewMemo view.
 */
@HiltViewModel
internal class ViewMemoViewModel @Inject constructor(
        private val getMemoByIdUseCase: GetMemoByIdUseCase
) : ViewModel() {

    private val _memo: MutableStateFlow<MemoUI?> = MutableStateFlow(null)
    val memo: StateFlow<MemoUI?> = _memo

    /**
     * Loads the memo whose id matches the given memoId from the database.
     */
    fun loadMemo(memoId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _memo.value = getMemoByIdUseCase.invoke(memoId)
        }
    }
}