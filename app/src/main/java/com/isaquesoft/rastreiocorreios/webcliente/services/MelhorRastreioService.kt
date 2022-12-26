package com.isaquesoft.rastreiocorreios.webcliente.services

import com.isaquesoft.rastreiocorreios.webcliente.model.ApiMelhorRastreio
import retrofit2.http.GET
import retrofit2.http.Path

interface MelhorRastreioService {

    @GET("{codigo}")
    suspend fun buscaRastreio(
        @Path("codigo") codigo: String
    ): ApiMelhorRastreio
}