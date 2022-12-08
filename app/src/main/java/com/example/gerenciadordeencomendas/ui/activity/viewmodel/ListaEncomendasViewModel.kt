package com.example.gerenciadordeencomendas.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gerenciadordeencomendas.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class ListaEncomendasViewModel(
    private val repository: Repository
) : ViewModel() {


    fun buscaTodasEncomendas() {
         repository.buscaTodasEncomendas()
    }

    fun excluirEncomenda(firebaseId: String): Task<Void> {
      return  repository.excluirEncomenda(firebaseId)
    }

    val liveDataEncomenda = repository.liveDataEncomenda
    val auth = FirebaseAuth.getInstance()
}