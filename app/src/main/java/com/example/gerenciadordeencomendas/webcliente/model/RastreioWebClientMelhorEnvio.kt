package com.example.gerenciadordeencomendas.webcliente.model

import com.example.gerenciadordeencomendas.webcliente.RetrofitMelhorRastreio
import com.example.gerenciadordeencomendas.webcliente.services.MelhorRastreioService

class RastreioWebClientMelhorEnvio {

    private val apiMelhorRastreio: MelhorRastreioService =
        RetrofitMelhorRastreio().melhorEnvioService

    suspend fun buscaRastreio(codigo: String): ApiMelhorRastreio? {

        return try {
            apiMelhorRastreio.buscaRastreio(codigo)
        } catch (e: Exception) {
            null
        }

    }

}