package com.example.gerenciadordeencomendas.ui.activity.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.ListaEncomendasViewModel


class ListaEncomendasViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListaEncomendasViewModel(repository) as T
    }
}