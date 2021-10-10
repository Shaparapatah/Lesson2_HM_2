package com.shaparapatah.lesson2_hm_2.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shaparapatah.lesson2_hm_2.R

class MyService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()) {
            val title = remoteMessageData[PUSH_KEY_TITLE]
            val message = remoteMessageData[PUSH_KEY_MESSAGE]
            if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
                pushNotifications(title, message)
            }
        }
    }

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val NOTIFICATION_ID_1 = 1
        private const val CHANNEL_ID_1 = "channel_id_1"
    }

    private fun pushNotifications(title: String, message: String) {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilderOne = NotificationCompat.Builder(this, CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_earth)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannelOne = "Name ${CHANNEL_ID_1}"
            val descChannelOne = "Description ${CHANNEL_ID_1}"
            val importanceChannelOne = NotificationManager.IMPORTANCE_MIN
            val channelOne =
                NotificationChannel(CHANNEL_ID_1, nameChannelOne, importanceChannelOne).apply {
                    description = descChannelOne
                }
            notificationManager.createNotificationChannel(channelOne)
        }
        notificationManager.notify(NOTIFICATION_ID_1, notificationBuilderOne.build())
    }
}