package com.example.gerenciadordeencomendas

import android.app.Application
import android.content.Intent
import com.example.gerenciadordeencomendas.di.modules.appModules
import com.example.gerenciadordeencomendas.notificacao.Notificacaoservice
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AppApplication)
            modules(appModules)
            startService(Intent(baseContext, Notificacaoservice::class.java))
        }
    }
}