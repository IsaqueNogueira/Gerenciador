package com.example.gerenciadordeencomendas.ui.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gerenciadordeencomendas.model.Usuario
import com.example.gerenciadordeencomendas.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class CadastroViewModel(
    private val repository: Repository
): ViewModel() {
    fun cadastraUsuario(usuario: Usuario): Task<AuthResult> {
      return  repository.cadastraUsuario(usuario)
    }

    fun salvarNomeDoUsuario(nome: String): Task<Void> {
        return repository.salvarNomeDoUsuario(nome)
    }

}