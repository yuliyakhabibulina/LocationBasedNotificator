package com.sap.codelab.domain.repository

import com.sap.codelab.domain.model.Memo

interface GeofenceRepository {
    suspend fun handleGeofenceTransition(geofenceIds: List<String>)
    suspend fun addGeofence(memo: Memo, radius: Float) : Result<Unit>
    suspend fun removeGeofence(memoId: Long) : Result<Unit>
}