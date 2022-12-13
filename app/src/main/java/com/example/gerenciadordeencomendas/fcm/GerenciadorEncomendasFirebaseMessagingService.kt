package com.example.gerenciadordeencomendas.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

private const val TAG = "Gerenciador FCM"
class GerenciadorEncomendasFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "onNewToken: $token")
    }
}