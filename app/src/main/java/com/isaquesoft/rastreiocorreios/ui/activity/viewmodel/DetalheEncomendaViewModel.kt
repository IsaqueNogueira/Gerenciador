package com.isaquesoft.rastreiocorreios.ui.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.isaquesoft.rastreiocorreios.model.Encomenda
import com.isaquesoft.rastreiocorreios.repository.Repository
import com.isaquesoft.rastreiocorreios.webcliente.model.ApiMelhorRastreio

class DetalheEncomendaViewModel(
    private val repository: Repository
) : ViewModel() {


    fun buscaEncomendaPorId(encomendaId: String): LiveData<Encomenda> {
        val liveDataEncomendaId = repository.buscaEncomendaPorId(encomendaId)

        return liveDataEncomendaId
    }

//    suspend fun buscaWebCliente(codigo: String): ApiCorreios {
//        return repository.buscaWebCliente(codigo)
//    }

    suspend fun buscaWebClienteMelhorRastreio(codigo: String): ApiMelhorRastreio? {
        return repository.buscaWebClientMelhorEnvio(codigo)
    }


}