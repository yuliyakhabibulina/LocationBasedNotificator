package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.GeofenceRepository
import com.sap.codelab.domain.repository.MemoRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke(memo: Memo) {
        val memoId = memoRepository.saveMemo(memo)
        val memoWithId = memo.copy(id = memoId)
        geofenceRepository.addGeofence(memoWithId, GEOFENCE_RADIUS)
    }

    companion object {
        const val GEOFENCE_RADIUS = 200F
    }
}