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

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

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

    fun showNotification(memo: Memo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                Log.w(
                    "NotificationHelper",
                    "Skipping notify for memo ${memo.id}: no POST_NOTIFICATIONS permission"
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
        Log.i("NotificationHelper", "Notification shown for memo ID: ${memo.id}")
    }

    companion object {
        private const val CHANNEL_ID = "memo_location_channel"
        private const val CHANNEL_NAME = "Location Reminders"
        private const val CHANNEL_DESCRIPTION = "Notifications for location-based memos"
        private const val NUMBER_OF_CHARACTERS = 100
    }
}