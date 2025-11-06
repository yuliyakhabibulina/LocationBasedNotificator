package com.sap.codelab

import android.app.Application
import com.sap.codelab.presentation.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
internal class App : Application()  {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel()
    }
}