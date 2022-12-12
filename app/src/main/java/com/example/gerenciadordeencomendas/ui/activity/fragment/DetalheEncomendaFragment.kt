package com.example.gerenciadordeencomendas.ui.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadordeencomendas.adapters.DetalheEncomendaAdapter
import com.example.gerenciadordeencomendas.databinding.DetalheEncomendaBinding
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.CHAVE_ENCOMENDA_ID
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.DetalheEncomendaViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.factory.DetalheEncomendaViewModelFactory
import com.example.gerenciadordeencomendas.utils.Utils
import com.example.gerenciadordeencomendas.utils.verificaConexao
import com.example.gerenciadordeencomendas.webcliente.model.ApiMelhorRastreio
import com.example.gerenciadordeencomendas.webcliente.model.Event
import kotlinx.coroutines.launch


class DetalheEncomendaFragment : Fragment() {
    var verificaConexao: () -> Unit = {}
    var botaoVoltar: () -> Unit = {}
    private val encomendaId: String by lazy {
        arguments?.getString(CHAVE_ENCOMENDA_ID) ?: throw IllegalArgumentException("Id inválido")
    }

    private val adpter by lazy {
        context?.let {
            DetalheEncomendaAdapter(it)
        } ?: throw IllegalArgumentException("Contexto inválido")
    }

    private lateinit var binding: DetalheEncomendaBinding

    private val viewModel by lazy {
        val repository = Repository()
        val factory = DetalheEncomendaViewModelFactory(repository)
        val provedor = ViewModelProvider(this, factory)
        provedor.get(DetalheEncomendaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetalheEncomendaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostraEncomenda()
    }

    private fun mostraEncomenda() {
        mostraProgressbar()
        viewModel.buscaEncomendaPorId(encomendaId).observe(this, Observer {
            lifecycleScope.launch {

                val nomePacote = binding.detalheEncomendaNomePacote
                nomePacote.text = it.nomePacote
                val codigoRastreio = binding.detalheEncomendaCodigoRastreio
                codigoRastreio.text = it.codigoRastreio

                viewModel.buscaWebClienteMelhorRastreio(it.codigoRastreio)?.let {
                    if (it.success == true) {


                        val rastreioMelhorRastreio = it

                        val tamanhoEvent = rastreioMelhorRastreio.data.events.size - 1
                        val ultimoStatus = rastreioMelhorRastreio.data.events.get(tamanhoEvent)
                        val primeiroStatus = rastreioMelhorRastreio.data.events.get(0)


                        if (ultimoStatus.events == "Objeto entregue ao destinatário") {
                            activity?.title = "Entregue"
                        } else {
                            activity?.title = "Em trânsito"
                        }

//                diasDePostagem(ultimoStatus, primeiroStatus)

                        adpter.atualiza(
                            rastreioMelhorRastreio.data.events,
                            encomendaId,
                            ultimoStatus,
                            primeiroStatus
                        )
                        val recyclerView = binding.detalheEncomendaRecyclerview
                        val layoutManager = LinearLayoutManager(context)
                        layoutManager.reverseLayout = true
                        layoutManager.stackFromEnd = true
                        recyclerView.layoutManager = layoutManager
                        recyclerView.adapter = adpter
                        ocultaProgressbar()
                    } else {
                        mostraErro()
                    }
                } ?: mostraErro()
            }

        })

    }

    private fun mostraErro() {
        verificaConexao()
        val mensagemErro = binding.detalheEncomendaMensagemErro
        val iconeErro = binding.detalheEncomendaIconErro
        mensagemErro.visibility = View.VISIBLE
        iconeErro.visibility = View.VISIBLE
        ocultaProgressbar()

    }

//    private fun diasDePostagem(ultimoStatus: Event, primeiroStatus: Event) {
//        val dataPostagem = primeiroStatus.date
//        var dataHoje = Utils().data()
//        if (ultimoStatus.events == "Objeto entregue ao destinatário") {
//            dataHoje = ultimoStatus.date
//        }
//        val diasEnviado = Utils().dias(dataPostagem, dataHoje)
//        var dia: String
//        var textoEnviado: String
//        if (diasEnviado < 2) {
//            dia = "dia"
//        } else {
//            dia = "dias"
//        }
//
//        if (ultimoStatus.events == "Objeto entregue ao destinatário") {
//            textoEnviado = "Entregue em:"
//        } else {
//            textoEnviado = "Enviado há:"
//        }
//        binding.detalheEncomendaDiasenviado.text = "$textoEnviado $diasEnviado $dia"
//    }

    private fun ocultaProgressbar() {
        binding.detalheEncomendaProgressbar.visibility = View.GONE
    }

    private fun mostraProgressbar() {
        binding.detalheEncomendaProgressbar.visibility = View.VISIBLE
    }

}