package com.isaquesoft.rastreiocorreios.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.isaquesoft.rastreiocorreios.R
import com.isaquesoft.rastreiocorreios.ui.activity.EncomendasActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "Gerenciador FCM"

class RastreioCorreiosFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            Log.i(TAG, "onMessageReceived: ${message.notification!!.title}")
            Log.i(TAG, "onMessageReceived: ${message.notification!!.body}")

            val titulo = message.notification!!.title
            val mensagem = message.notification!!.body
            senNotification(titulo, mensagem)
        }

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun senNotification(titulo: String?, mensagem: String?) {

        val intent = Intent(this, EncomendasActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val canal = getString(R.string.default_web_client_id)
        val som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, canal)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setSound(som)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(canal, "canal", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(0, notification.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: $token")
    }
}