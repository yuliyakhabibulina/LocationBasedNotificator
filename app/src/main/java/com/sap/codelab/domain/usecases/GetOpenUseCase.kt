package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.repository.MemoRepository
import com.sap.codelab.presentation.mapper.toUI
import com.sap.codelab.presentation.model.MemoUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * UseCase to retrieve all open memos from the database.
 */
class GetOpenUseCase @Inject constructor(
    private val repository: MemoRepository
) {
    operator fun invoke(): Flow<List<MemoUI>> {
        return repository.getOpen().map { memoEntity ->
            memoEntity.map { it.toUI() }
        }
    }
}