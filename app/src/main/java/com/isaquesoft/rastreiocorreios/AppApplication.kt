package com.isaquesoft.rastreiocorreios

import android.app.Application
import com.isaquesoft.rastreiocorreios.di.modules.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AppApplication)
            modules(appModules)
           // startService(Intent(baseContext, Notificacaoservice::class.java))
        }
    }
}