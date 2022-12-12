package com.example.gerenciadordeencomendas.ui.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.webcliente.model.ApiCorreios
import com.example.gerenciadordeencomendas.webcliente.model.ApiMelhorRastreio

class DetalheEncomendaViewModel(
    private val repository: Repository
) : ViewModel() {


    fun buscaEncomendaPorId(encomendaId: String): LiveData<Encomenda> {
        val liveDataEncomendaId = repository.buscaEncomendaPorId(encomendaId)

        return liveDataEncomendaId
    }

    suspend fun buscaWebCliente(codigo: String): ApiCorreios {
        return repository.buscaWebCliente(codigo)
    }

    suspend fun buscaWebClienteMelhorRastreio(codigo: String): ApiMelhorRastreio? {
        return repository.buscaWebClientMelhorEnvio(codigo)
    }


}