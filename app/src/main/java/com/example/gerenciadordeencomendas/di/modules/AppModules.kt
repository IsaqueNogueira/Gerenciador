package com.example.gerenciadordeencomendas.di.modules

import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.CadastroViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.DetalheEncomendaViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.FormEncomendaViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.ListaEncomendasViewModel
import com.example.gerenciadordeencomendas.webcliente.model.RastreioWebClientMelhorEnvio
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
      single<Repository> {
          Repository()
      }
    viewModel {
        ListaEncomendasViewModel(get())
    }

    viewModel {
        DetalheEncomendaViewModel(get())
    }

    viewModel {
        FormEncomendaViewModel(get())
    }

    viewModel {
        CadastroViewModel(get())
    }


}