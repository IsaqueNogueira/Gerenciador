package com.example.gerenciadordeencomendas.webcliente.model

import com.example.gerenciadordeencomendas.webcliente.RetrofitMelhorRastreio
import com.example.gerenciadordeencomendas.webcliente.services.MelhorRastreioService

class RastreioWebClientMelhorEnvio {

    private val apiMelhorRastreio: MelhorRastreioService =
        RetrofitMelhorRastreio().melhorEnvioService

    suspend fun buscaRastreio(codigo: String): ApiMelhorRastreio {

        val rastreioResposta = apiMelhorRastreio
            .buscaRastreio(codigo)

        return rastreioResposta


    }
}