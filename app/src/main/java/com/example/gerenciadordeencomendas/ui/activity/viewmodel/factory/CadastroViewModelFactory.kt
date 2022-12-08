package com.example.gerenciadordeencomendas.ui.activity.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.CadastroViewModel

class CadastroViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CadastroViewModel(repository) as T
    }

}