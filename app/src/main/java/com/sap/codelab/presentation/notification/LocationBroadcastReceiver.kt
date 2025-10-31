package com.sap.codelab.presentation.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.sap.codelab.domain.repository.GeofenceRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationBroadcastReceiver(
) : BroadcastReceiver() {

    @Inject
    lateinit var geofenceRepository : GeofenceRepository

    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent)
        if (event == null || event.hasError()) {
            Log.e("GeofenceReceiver", "Invalid geofence event")
            return
        }

        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val ids = event.triggeringGeofences?.map { it.requestId } ?: return
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                geofenceRepository.handleGeofenceTransition(ids)
                pendingResult.finish()
            }
        }
    }
}