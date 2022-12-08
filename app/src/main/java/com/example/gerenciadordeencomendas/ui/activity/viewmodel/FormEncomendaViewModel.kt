package com.example.gerenciadordeencomendas.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.repository.Repository
import com.google.android.gms.tasks.Task

class FormEncomendaViewModel(
    private val repository: Repository
): ViewModel() {

    fun salvarEncomenda(encomenda: Encomenda): Task<Void> {
       return repository.salvarEncomenda(encomenda)
    }

    val auth = repository.auth

}