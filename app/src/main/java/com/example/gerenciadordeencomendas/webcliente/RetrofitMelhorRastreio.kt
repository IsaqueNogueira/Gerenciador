package com.example.gerenciadordeencomendas.webcliente

import com.example.gerenciadordeencomendas.webcliente.services.MelhorRastreioService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitMelhorRastreio {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.melhorrastreio.com.br/api/v1/trackings/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val melhorEnvioService = retrofit.create(MelhorRastreioService::class.java)
}