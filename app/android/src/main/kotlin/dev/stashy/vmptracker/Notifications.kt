package dev.stashy.vmptracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService

const val PersistentNotificationChannelId: String = "PERSISTENT_NOTIFICATION"
private const val PersistentNotificationId: Int = 100

fun Context.setupNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            PersistentNotificationChannelId,
            getString(R.string.notification_category_persistent_name),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = getString(R.string.notification_category_persistent_description)
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

fun Context.serviceNotification() =
    NotificationCompat.Builder(this, PersistentNotificationChannelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setSilent(true)
        .setContentTitle(getString(R.string.notification_persistent_title))
        .setContentText(getString(R.string.notification_persistent_description))
        .setOngoing(true)
        .build()

fun LifecycleService.startCameraForeground() {
    val types = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
    } else {
        0
    }

    ServiceCompat.startForeground(
        this,
        PersistentNotificationId,
        serviceNotification(),
        types,
    )
}
