package com.example.gerenciadordeencomendas.ui.activity.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.adapters.ListaEncomendasAdapter
import com.example.gerenciadordeencomendas.databinding.ListaEncomendasBinding
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.ListaEncomendasViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.factory.ListaEncomendasViewModelFactory

class ListaEncomendasFragment : Fragment() {
    var quandoClicarSair: () -> Unit = {}
    var quandoClicarAdicionarEncomenda: () -> Unit = {}
    var quandoClicarNoItem: (encomenda: Encomenda) -> Unit = {}

    private val adapter by lazy {
        context?.let {
            ListaEncomendasAdapter(it)
        } ?: throw IllegalArgumentException("Contexto inválido")
    }

   private lateinit var binding : ListaEncomendasBinding

    private val viewModel by lazy {
        val repository = Repository()
        val factory = ListaEncomendasViewModelFactory(repository)
        val provedor = ViewModelProvider(this, factory)
        provedor.get(ListaEncomendasViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        excluirEncomenda()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListaEncomendasBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Minhas encomendas"
        clicouBotaoAdicionarPacote()
    }

    override fun onResume() {
        super.onResume()
        configuraRecyclerview()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_lista_encomendas, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        context?.let {
            configuraAlertDialogSair(it)
            return super.onOptionsItemSelected(item)
        } ?: throw IllegalArgumentException("Contexto inválido")

    }

    private fun configuraAlertDialogSair(it: Context) {
        AlertDialog.Builder(it)
            .setTitle("Deseja realmente sair?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.auth.signOut()
                quandoClicarSair()
            }
            .setNegativeButton("Não") { _, _ -> }
            .show()
    }

    private fun configuraRecyclerview() {
        viewModel.buscaTodasEncomendas()
        viewModel.liveDataEncomenda.observe(this, Observer { encomendas ->
            adapter.atualiza(encomendas)
            val recyclerView = binding.listaEncomendaRecyclerview
            recyclerView.adapter = adapter
            adapter.quandoClicarNoItem = {
                quandoClicarNoItem(it)
            }
        })
    }

    private fun excluirEncomenda() {
        adapter.quandoSegurarNoItem = { encomenda ->
            context?.let {context->
                configuraAlertDialogExcluirEncomenda(context, encomenda)
            }
        }

    }

    private fun configuraAlertDialogExcluirEncomenda(
        context: Context,
        encomenda: Encomenda
    ) {
        AlertDialog.Builder(context)
            .setTitle("Tem certeza que deseja excluir a encomenda?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.excluirEncomenda(encomenda.firebaseId)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            adapter.remove(encomenda)
                            Toast.makeText(
                                context,
                                "Encomenda excluída",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
            .setNegativeButton("Não") { _, _ -> }
            .show()
    }

    private fun clicouBotaoAdicionarPacote() {
        binding.listaEncomendaBotao.setOnClickListener {
            quandoClicarAdicionarEncomenda()
        }
    }
}