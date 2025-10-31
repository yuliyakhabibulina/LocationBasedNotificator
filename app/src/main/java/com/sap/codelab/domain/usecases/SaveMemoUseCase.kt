package com.sap.codelab.domain.usecases

import com.sap.codelab.domain.repository.GeofenceRepository
import com.sap.codelab.domain.repository.MemoRepository
import com.sap.codelab.presentation.mapper.fromUI
import com.sap.codelab.presentation.model.MemoUI
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
    private val geofenceRepository: GeofenceRepository
) {
    suspend operator fun invoke(memo: MemoUI) {
        val memoId = memoRepository.saveMemo(memo.fromUI())
        val memoWithId = memo.fromUI().copy(id = memoId)
        geofenceRepository.addGeofence(memoWithId, GEOFENCE_RADIUS)
    }

    companion object {
        const val GEOFENCE_RADIUS = 200F
    }
}