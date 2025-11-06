package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.repository.MemoRepository
import com.sap.codelab.presentation.mapper.toUI
import com.sap.codelab.presentation.model.MemoUI
import javax.inject.Inject

/**
 * UseCase to retrieve a memo by its id from the database.
 */
class GetMemoByIdUseCase @Inject constructor(
    private val repository: MemoRepository
) {
    suspend operator fun invoke(memoId: Long): MemoUI? {
        return repository.getMemoById(memoId)?.toUI()
    }
}