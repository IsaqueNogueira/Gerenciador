package com.example.gerenciadordeencomendas.ui.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class ListaEncomendasViewModel(
    private val repository: Repository
) : ViewModel() {

    fun buscaTodasEncomendas(): LiveData<List<Encomenda>> {
        val liveDataEncomenda = repository.buscaTodasEncomendas()
        return liveDataEncomenda
    }

    fun excluirEncomenda(firebaseId: String): Task<Void> {
      return  repository.excluirEncomenda(firebaseId)
    }

    fun salvarEncomenda(encomenda: Encomenda): Task<Void> {
        return repository.salvarEncomenda(encomenda)
    }

    val auth = FirebaseAuth.getInstance()

}