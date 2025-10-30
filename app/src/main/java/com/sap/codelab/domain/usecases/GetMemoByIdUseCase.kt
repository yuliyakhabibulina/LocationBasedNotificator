package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.MemoRepository
import javax.inject.Inject

class GetMemoByIdUseCase @Inject constructor(
    private val repository: MemoRepository
) {
    suspend operator fun invoke(memoId: Long): Memo? {
        return repository.getMemoById(memoId)
    }
}