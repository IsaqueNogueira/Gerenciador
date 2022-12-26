package com.isaquesoft.rastreiocorreios.webcliente

import com.isaquesoft.rastreiocorreios.webcliente.services.RastreioService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInicializador {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.linketrack.com/track/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val rastreioService = retrofit.create(RastreioService::class.java)
}