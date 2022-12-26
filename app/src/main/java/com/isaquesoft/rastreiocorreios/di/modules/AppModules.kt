package com.isaquesoft.rastreiocorreios.di.modules

import com.isaquesoft.rastreiocorreios.repository.Repository
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.CadastroViewModel
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.DetalheEncomendaViewModel
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.ListaEncomendasViewModel
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
        CadastroViewModel(get())
    }


}