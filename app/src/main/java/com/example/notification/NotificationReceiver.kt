package com.example.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    lateinit var notificationManager: NotificationManager
    override fun onReceive(p0: Context?, p1: Intent?) {
        notificationManager =
            p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (p1?.hasExtra("ID") == true) {
            val noteId = p1.getIntExtra("ID", 0)
            notificationManager.cancel(noteId)
        }
    }
}