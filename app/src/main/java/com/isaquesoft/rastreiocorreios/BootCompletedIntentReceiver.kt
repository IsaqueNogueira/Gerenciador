package com.isaquesoft.rastreiocorreios

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.isaquesoft.rastreiocorreios.notificacao.Notificacaoservice

class BootCompletedIntentReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val pushIntent = Intent(context, Notificacaoservice::class.java)
            context.startService(pushIntent)
        }
    }
}