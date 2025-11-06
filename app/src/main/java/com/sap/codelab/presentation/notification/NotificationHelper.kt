package com.sap.codelab.presentation.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.sap.codelab.R
import com.sap.codelab.domain.model.Memo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Helper class for showing notifications.
 */
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    /**
     * Creates a notification channel.
     */
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESCRIPTION
        }
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Shows a notification for the given memo.
     */
    fun showNotification(memo: Memo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                Log.w(
                    TAG,
                    "Notification permission not granted. Cannot show notification"
                )
                return
            }
        }

        val notificationId = memo.id.toInt()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(memo.title)
            .setContentText(memo.description.take(NUMBER_OF_CHARACTERS))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        notificationManager.notify(notificationId, builder.build())
    }

    private companion object {
        const val CHANNEL_ID = "memo_location_channel"
        const val CHANNEL_NAME = "Location Reminders"
        const val CHANNEL_DESCRIPTION = "Notifications for location-based memos"
        const val NUMBER_OF_CHARACTERS = 100
        const val TAG = "NotificationHelper"
    }
}