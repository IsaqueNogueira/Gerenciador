package com.example.gerenciadordeencomendas.ui.activity.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.databinding.ListaEncomendasBinding
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.ui.activity.LoginActivity
import com.example.gerenciadordeencomendas.ui.activity.adapters.ListaEncomendasAdapter
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.ListaEncomendasViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaEncomendasFragment : Fragment() {
    private val controlador by lazy {
        findNavController()
    }
    private val adapter by lazy {
        context?.let {
            ListaEncomendasAdapter(it)
        } ?: throw IllegalArgumentException("Contexto inválido")
    }

    private lateinit var binding: ListaEncomendasBinding
    private val viewModel: ListaEncomendasViewModel by viewModel()

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
        configuraAdapter()
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

    @SuppressLint("ResourceType")
    private fun configuraAlertDialogSair(it: Context) {
        AlertDialog.Builder(it)
            .setTitle("Deseja realmente sair?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.auth.signOut()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton("Não") { _, _ -> }
            .show()
    }

    private fun configuraRecyclerview() {
        viewModel.buscaTodasEncomendas().observe(this, Observer{ encomendas ->
            adapter.atualiza(encomendas)
        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun configuraAdapter() {
        val recyclerView = binding.listaEncomendaRecyclerview
        recyclerView.adapter = adapter
        adapter.quandoClicarNoItem = {
             val direction =
                 ListaEncomendasFragmentDirections.actionListaEncomendasToDetalheEncomenda(it.firebaseId)
                controlador.navigate(direction)
        }
    }

    private fun excluirEncomenda() {
        adapter.quandoSegurarNoItem = { encomenda ->
            context?.let { context ->
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
            val direction = ListaEncomendasFragmentDirections.actionListaEncomendasToFormEncomenda()
            controlador.navigate(direction)
        }
    }
}