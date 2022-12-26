package com.isaquesoft.rastreiocorreios.webcliente.model

import com.isaquesoft.rastreiocorreios.webcliente.RetrofitInicializador
import com.isaquesoft.rastreiocorreios.webcliente.services.RastreioService

class RastreioWebCliente {

    private val apiCorreios: RastreioService =
        RetrofitInicializador().rastreioService

    suspend fun buscaRastreio(user: String, token: String, codigo: String): ApiCorreios{

            val rastreioResposta = apiCorreios
                .buscaRastreio(user, token,codigo)

            return rastreioResposta


    }
}