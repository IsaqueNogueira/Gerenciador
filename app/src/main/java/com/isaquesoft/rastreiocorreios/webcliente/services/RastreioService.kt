package com.isaquesoft.rastreiocorreios.webcliente.services

import com.isaquesoft.rastreiocorreios.webcliente.model.ApiCorreios
import retrofit2.http.GET
import retrofit2.http.Query

interface RastreioService {
    @GET("json")
    suspend fun buscaRastreio(
        @Query("user") user: String,
        @Query("token") token: String,
        @Query("codigo") codigo: String
    ): ApiCorreios
}