package com.example.notification

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageReceiver : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
//        triggerNotification(
//            remoteMessage.notification?.title.toString(),
//            remoteMessage.notification?.body.toString()
//        )
        Log.d("FCM", remoteMessage.notification.toString())
        Log.d("FCM", remoteMessage.data.toString())
        var dataMap: Map<String, String> = HashMap()
        var noteType: String = ""
        if (remoteMessage != null) {
            dataMap = remoteMessage.data
            noteType = remoteMessage.data["type"].toString()
        }
        when (noteType) {
            "BIGPIC" -> {
                bigPictureNotification(dataMap)
            }
            "ACTIONS" -> {
                notificationAction(dataMap)
            }

        }
    }

    fun triggerNotification(title: String, content: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_launcher_background
                )
            )
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(getString(R.string.CHANNEL_ID))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(12, builder.build())
    }


    fun bigPictureNotification(dataMap: Map<String, String>) {
        val title = dataMap["title"]
        val message = dataMap["message"]
        val imageUrl = dataMap["imageUrl"]
        val builder = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
        val style: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle()
        style.setBigContentTitle(title)
        style.setSummaryText(message)
        style.bigPicture(Glide.with(this).asBitmap().load(imageUrl).submit().get())
        builder.setContentTitle(title)
            .setContentText(message)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_baseline_10k_24)
            .setStyle(style)
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(0, builder.build())
    }

    fun notificationAction(dataMap: Map<String, String>) {
        val title = dataMap["title"]
        val message = dataMap["message"]
        val intent = Intent(this, MainActivity2::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val cancelIntent = Intent(applicationContext, NotificationReceiver::class.java)
        cancelIntent.putExtra("ID", 0)
        val cancelPendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent, 0)
        val builder = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
        builder.setSmallIcon(R.drawable.ic_baseline_10k_24)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(Color.BLUE)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent)
            .addAction(android.R.drawable.ic_delete, "DISMISS", cancelPendingIntent)
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(0, builder.build())
    }

}