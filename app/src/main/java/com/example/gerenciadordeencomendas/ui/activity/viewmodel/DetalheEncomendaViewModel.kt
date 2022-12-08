package com.example.gerenciadordeencomendas.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.webcliente.model.ApiCorreios

class DetalheEncomendaViewModel(
    private val repository: Repository
): ViewModel() {

    val liveDataEncomendaId = repository.liveDataEncomendaId

    fun buscaEncomendaPorId(encomendaId: String) {
        repository.buscaEncomendaPorId(encomendaId)
    }

    suspend fun buscaWebCliente(codigo: String): ApiCorreios {
      return  repository.buscaWebCliente(codigo)
    }


}