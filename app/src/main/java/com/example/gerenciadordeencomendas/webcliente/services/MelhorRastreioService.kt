package com.example.gerenciadordeencomendas.webcliente.services

import com.example.gerenciadordeencomendas.webcliente.model.ApiMelhorRastreio
import retrofit2.http.GET
import retrofit2.http.Path

interface MelhorRastreioService {

    @GET("{codigo}")
    suspend fun buscaRastreio(
        @Path("codigo") codigo: String
    ): ApiMelhorRastreio
}