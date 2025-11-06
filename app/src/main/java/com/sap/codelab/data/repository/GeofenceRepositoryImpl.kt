package com.sap.codelab.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.sap.codelab.data.database.MemoDao
import com.sap.codelab.data.mapper.fromEntity
import com.sap.codelab.data.model.GeofenceLimitExceededException
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.domain.repository.GeofenceRepository
import com.sap.codelab.presentation.notification.LocationBroadcastReceiver
import com.sap.codelab.presentation.notification.NotificationHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofenceRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao,
    private val notificationHelper: NotificationHelper,
    @ApplicationContext private val context: Context
) : GeofenceRepository {

    private val geofencingClient by lazy { LocationServices.getGeofencingClient(context) }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, LocationBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            GEOFENCE_INTENT_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    override suspend fun handleGeofenceTransition(geofenceIds: List<String>) = withContext(
        Dispatchers.IO
    ) {
        geofenceIds.forEach { id ->
            val memoId = id.toLongOrNull() ?: return@forEach
            val memoEntity = memoDao.getMemoById(memoId)
            if (memoEntity != null) {
                val memo = memoEntity.fromEntity()
                notificationHelper.showNotification(memo)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun addGeofence(memo: Memo, radius: Float): Result<Unit> = runCatching {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("GeofenceHelper", "Fine location permission not granted.")
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("GeofenceHelper", "Background location permission not granted.")
        }

        val activeGeofenceCount = memoDao.getActiveGeofenceCount()

        if (activeGeofenceCount >= GEOFENCE_LIMIT) {
            val oldestMemoId = memoDao.getOldestActiveGeofenceMemoId()
            if (oldestMemoId != null) {
                Log.i(
                    TAG,
                    "Geofence limit reached. Removing oldest geofence with memo ID: $oldestMemoId"
                )
                removeGeofence(oldestMemoId)
            } else {
                throw GeofenceLimitExceededException("Limit reached, but no oldest geofence found to remove.")
            }
        }

        val geofence = Geofence.Builder()
            .setRequestId(memo.id.toString())
            .setCircularRegion(
                memo.reminderLatitude,
                memo.reminderLongitude,
                radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).await()
        memoDao.setGeofenceActive(memo.id, true)

    }.onFailure { exception ->
        Log.e(TAG, "Failed to process geofence. Error: ${exception.message}", exception)
    }

    override suspend fun removeGeofence(memoId: Long): Result<Unit> = runCatching {
        geofencingClient.removeGeofences(listOf(memoId.toString())).await()
        memoDao.setGeofenceActive(memoId, false)

    }.onFailure { exception ->
        Log.e(
            TAG,
            "Failed to remove geofence for memo ID: $memoId. Error: ${exception.message}",
            exception
        )
    }

    companion object {
        private const val TAG = "GeofenceRepository"
        private const val GEOFENCE_INTENT_REQUEST_CODE = 0
        private const val GEOFENCE_LIMIT = 100
    }
}