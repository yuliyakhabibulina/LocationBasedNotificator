package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.MemoRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(memo: Memo) {
         memoRepository.saveMemo(memo)
    }
}