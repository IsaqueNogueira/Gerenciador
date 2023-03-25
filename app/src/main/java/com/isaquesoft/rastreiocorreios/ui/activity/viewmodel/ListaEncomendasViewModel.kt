package com.isaquesoft.rastreiocorreios.ui.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.isaquesoft.rastreiocorreios.model.Encomenda
import com.isaquesoft.rastreiocorreios.repository.Repository

class ListaEncomendasViewModel(
    private val repository: Repository,
) : ViewModel() {

    fun buscaTodasEncomendas(): LiveData<List<Encomenda>> {
        val liveDataEncomenda = repository.buscaTodasEncomendas()
        return liveDataEncomenda
    }

    fun excluirEncomenda(firebaseId: String): Task<Void> {
        return repository.excluirEncomenda(firebaseId)
    }

    fun salvarEncomenda(encomenda: Encomenda): Task<Void> {
        return repository.salvarEncomenda(encomenda)
    }

    val auth = FirebaseAuth.getInstance()
}
