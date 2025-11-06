package com.sap.codelab.presentation.notification

import android.content.BroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sap.codelab.domain.repository.GeofenceRepository
import com.sap.codelab.domain.usecases.GetOpenUseCase
import com.sap.codelab.domain.usecases.SaveMemoUseCase
import com.sap.codelab.presentation.mapper.fromUI
import kotlinx.coroutines.Dispatchers

/**
 * Receiver for boot event.
 *
 * The application must re-register geofenceы because the system deletes all registered geofences after a reboot.
 */
@AndroidEntryPoint
class BootEventReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getOpenUseCase: GetOpenUseCase

    @Inject
    lateinit var geofenceRepository: GeofenceRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        val pendingResult: PendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val openMemos = getOpenUseCase().first()
                if (openMemos.isEmpty()) {
                    return@launch
                }
                for (memo in openMemos) {
                    geofenceRepository.addGeofence(
                        memo.fromUI(),
                        SaveMemoUseCase.GEOFENCE_RADIUS
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "An error occurred in Boot Event receiver.", e)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private companion object {
        const val TAG = "BootEventReceiver"
    }
}