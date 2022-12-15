package com.example.gerenciadordeencomendas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.webcliente.model.RastreioWebClientMelhorEnvio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val IDENTIFICADOR_DO_CANAL = "principal"

class Notificacaoservice : LifecycleService() {

    val repository by lazy {
        Repository()
    }

    val rastreioWebClientMelhorRastreio by lazy {
        RastreioWebClientMelhorEnvio()
    }

    override fun onBind(arg0: Intent?): IBinder? {
        super.onBind(arg0)
        // TODO Auto-generated method stub
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e("TAG", "onStartCommand")

        val gerenciadorDeNotificacoes =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nome = getString(R.string.default_web_client_id)
            val descricao = getString(R.string.default_web_client_id)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(IDENTIFICADOR_DO_CANAL, nome, importancia)
            canal.description = descricao

            gerenciadorDeNotificacoes.createNotificationChannel(canal)
        }

        fun foo() {
            verificaAtualizacaoRastreio().observe(this, Observer {
                if (it != "Não atualizou") {
                    val notificacao = NotificationCompat.Builder(this, IDENTIFICADOR_DO_CANAL)
                        .setContentTitle(it)
                        .setContentText("Está em trânsito")
                        .setSmallIcon(R.drawable.logo)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build()
                    gerenciadorDeNotificacoes.notify(1, notificacao)
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
            for (i in it) {
                lifecycleScope.launch {
                        rastreioWebClientMelhorRastreio.buscaRastreio(i.codigoRastreio)
                            ?.let { rastreioEncontrado ->
                                if (rastreioEncontrado.success == true) {
                                    val tamanhoEvent = rastreioEncontrado.data.events.size - 1
                                    val ultimoStatus =
                                        rastreioEncontrado.data.events.get(tamanhoEvent)
                                    if (i.dataHoraApi != ultimoStatus.date) {
                                        Log.i("TAG", "onCreate: Mudou")
                                        liveData.value = i.nomePacote
                                    } else {
                                        liveData.value = "Não atualizou"
                                    }
                                }
                            }
                }
            }
        })
        return liveData
    }

}