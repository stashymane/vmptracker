package dev.stashy.vtracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build

const val persistentNotificationChannelId: String = "PERSISTENT_NOTIFICATION"

fun Context.setupNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel.
        val name = getString(dev.stashy.vtracker.R.string.notification_persistent_name)
        val descriptionText =
            getString(dev.stashy.vtracker.R.string.notification_persistent_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(persistentNotificationChannelId, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}