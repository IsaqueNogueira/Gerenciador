package com.example.gerenciadordeencomendas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.gerenciadordeencomendas.notificacao.Notificacaoservice

class BootCompletedIntentReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val pushIntent = Intent(context, Notificacaoservice::class.java)
            context.startService(pushIntent)
        }
    }
}