package com.example.gerenciadordeencomendas.webcliente.model

import com.example.gerenciadordeencomendas.webcliente.RetrofitInicializador
import com.example.gerenciadordeencomendas.webcliente.services.RastreioService

class RastreioWebCliente {

    private val apiCorreios: RastreioService =
        RetrofitInicializador().rastreioService

    suspend fun buscaRastreio(user: String, token: String, codigo: String): ApiCorreios{

            val rastreioResposta = apiCorreios
                .buscaRastreio(user, token,codigo)

            return rastreioResposta


    }
}