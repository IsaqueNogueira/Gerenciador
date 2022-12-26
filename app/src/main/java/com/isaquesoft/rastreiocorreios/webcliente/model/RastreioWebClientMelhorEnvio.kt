package com.isaquesoft.rastreiocorreios.webcliente.model

import com.isaquesoft.rastreiocorreios.webcliente.RetrofitMelhorRastreio
import com.isaquesoft.rastreiocorreios.webcliente.services.MelhorRastreioService

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