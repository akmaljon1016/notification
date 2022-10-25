package com.example.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessaging

class App : Application() {

    var fcmToken: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        FirebaseMessaging.getInstance().subscribeToTopic("PushNotification")
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", it.exception)
            } else {
                Log.d("FCM", "Fetching FCM :" + it.result)
                fcmToken.value = it.result
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                getString(R.string.CHANNEL_ID),
                getString(R.string.NAME),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Salom hammaga"
            notificationChannel.setShowBadge(true)

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}