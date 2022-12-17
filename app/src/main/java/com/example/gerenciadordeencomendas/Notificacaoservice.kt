package com.example.gerenciadordeencomendas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.EncomendasActivity
import com.example.gerenciadordeencomendas.webcliente.model.RastreioWebClientMelhorEnvio
import kotlinx.coroutines.launch
import java.util.*

private const val IDENTIFICADOR_DO_CANAL = "principal"

class Notificacaoservice : LifecycleService() {

    val repository by lazy {
        Repository()
    }

    val rastreioWebClientMelhorRastreio by lazy {
        RastreioWebClientMelhorEnvio()
    }
    companion object {
        var id = 1
    }

    override fun onBind(arg0: Intent?): IBinder? {
        super.onBind(arg0)
        // TODO Auto-generated method stub
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val gerenciadorDeNotificacoes =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nome = getString(R.string.channel_name)
            val descricao = getString(R.string.channel_description)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(IDENTIFICADOR_DO_CANAL, nome, importancia)
            canal.description = descricao

            gerenciadorDeNotificacoes.createNotificationChannel(canal)
        }

        fun foo() {
            verificaAtualizacaoRastreio().observe(this, Observer {
                if (it != null) {

                    val i = Intent(this, EncomendasActivity::class.java)
                    var pendingIntent: PendingIntent? = null
                    pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(
                            this,
                            0,
                            i,
                            PendingIntent.FLAG_MUTABLE
                        )
                    } else {
                        PendingIntent.getActivity(
                            this,
                            0,
                            i,
                            PendingIntent.FLAG_ONE_SHOT
                        )
                    }

                    val notificacao = NotificationCompat.Builder(this, IDENTIFICADOR_DO_CANAL)
                        .setContentTitle(it)
                        .setContentText("Está em trânsito")
                        .setSmallIcon(R.drawable.logo)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()

                    gerenciadorDeNotificacoes.notify(id, notificacao)

                    }
            })
        }
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
             lifecycleScope.launch {
                 foo()
             }
            }
        }, 0, 600000)

        // START_STICKY serve para executar seu serviço até que você pare ele, é reiniciado automaticamente sempre que termina
        return START_STICKY

    }


    private fun verificaAtualizacaoRastreio(): LiveData<String> {
        val liveData = MutableLiveData<String>()
        repository.buscaTodasEncomendas().observe(this, Observer {
            it.forEach() {
                lifecycleScope.launch {
                        rastreioWebClientMelhorRastreio.buscaRastreio(it.codigoRastreio)
                            ?.let { rastreioEncontrado ->
                                if (rastreioEncontrado.success == true) {
                                    val tamanhoEvent = rastreioEncontrado.data.events.size - 1
                                    val ultimoStatus =
                                        rastreioEncontrado.data.events.get(tamanhoEvent)
                                    if (it.dataHoraApi != ultimoStatus.date) {
                                        liveData.value = it.nomePacote
                                    } else {
                                        liveData.value = null
                                    }
                                }
                            }

                }
            }
        })
        return liveData
    }

}