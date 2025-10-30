package com.sap.codelab.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.codelab.model.Memo
import com.sap.codelab.repository.IMomoRepositoryImpl
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
        private val repository: IMomoRepositoryImpl
) : ViewModel() {

    private var isShowAll = false
    private val _memos: MutableStateFlow<List<Memo>> = MutableStateFlow(listOf())
    val memos: StateFlow<List<Memo>> = _memos

    /**
     * Loads all memos.
     */
    fun loadAllMemos() {
        isShowAll = true
        viewModelScope.launch(Dispatchers.Default) {
            _memos.value = repository.getAll()
        }
    }

    /**
     * Loads all open (not done) memos.
     */
    fun loadOpenMemos() {
        isShowAll = false
        viewModelScope.launch(Dispatchers.Default) {
            _memos.value = repository.getOpen()
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
     * @param memo      - the memo to update.
     * @param isChecked - whether the memo has been checked (marked as done).
     */
    fun updateMemo(memo: Memo, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // We'll only forward the update if the memo has been checked, since we don't offer to uncheck memos right now
            if (isChecked) {
                repository.saveMemo(memo.copy(isDone = true))
            }
        }
    }
}