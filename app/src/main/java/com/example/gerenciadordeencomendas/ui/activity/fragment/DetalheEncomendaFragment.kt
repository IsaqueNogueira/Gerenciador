package com.example.gerenciadordeencomendas.ui.activity.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.adapters.DetalheEncomendaAdapter
import com.example.gerenciadordeencomendas.databinding.DetalheEncomendaBinding
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.CHAVE_ENCOMENDA_ID
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.DetalheEncomendaViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.factory.DetalheEncomendaViewModelFactory
import com.example.gerenciadordeencomendas.utils.Utils
import com.example.gerenciadordeencomendas.webcliente.model.Evento
import kotlinx.coroutines.launch


class DetalheEncomendaFragment : Fragment() {
    var botaoVoltar: () -> Unit = {}
    private val encomendaId: String by lazy{
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
    ): View? {
        binding = DetalheEncomendaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostraEncomenda()
        activity?.title = "Encomenda"
//        clicouBotaoVoltar()
    }

    private fun mostraEncomenda() {
        mostraProgressbar()
        viewModel.buscaEncomendaPorId(encomendaId)
        viewModel.liveDataEncomendaId.observe(this, Observer {
            lifecycleScope.launch {
                val nomePacote = binding.detalheEncomendaNomePacote
                nomePacote.text = it.nomePacote
                val codigoRastreio = binding.detalheEncomendaCodigoRastreio
                codigoRastreio.text = it.codigoRastreio
                val mensagemErro = binding.detalheEncomendaMensagemErro
                val iconeErro = binding.detalheEncomendaIconErro
                val rastreio = viewModel.buscaWebCliente(it.codigoRastreio)
                if (rastreio.quantidade != 0L) {
                    if (rastreio.eventos.size != 0) {
                        val tamanhoEvento = rastreio.eventos.size - 1
                        val primeiroStatus = rastreio.eventos.get(tamanhoEvento)
                        val ultimoStatus = rastreio.eventos.get(0)

                        diasDePostagem(ultimoStatus, primeiroStatus)

                        adpter.atualiza(rastreio.eventos, ultimoStatus, encomendaId, primeiroStatus)
                        val recyclerView = binding.detalheEncomendaRecyclerview
                        recyclerView.adapter = adpter
                        ocultaProgressbar()
                    }
                } else {
                    ocultaProgressbar()
                    mensagemErro.visibility = View.VISIBLE
                    iconeErro.visibility = View.VISIBLE
                    codigoRastreio.setTextColor(Color.RED)
                }
            }
        })

    }

    private fun diasDePostagem(ultimoStatus: Evento, primeiroStatus: Evento) {
        val dataPostagem = primeiroStatus.data
        var dataHoje = Utils().data()
        if (ultimoStatus.status == "Objeto entregue ao destinatário") {
            dataHoje = ultimoStatus.data
        }
        val diasEnviado = Utils().dias(dataPostagem, dataHoje)
        var dia: String
        var textoEnviado: String
        if (diasEnviado < 2) {
            dia = "dia"
        } else {
            dia = "dias"
        }

        if (ultimoStatus.status == "Objeto entregue ao destinatário") {
            textoEnviado = "Entregue em:"
        } else {
            textoEnviado = "Enviado há:"
        }
        binding.detalheEncomendaDiasenviado.text = "$textoEnviado $diasEnviado $dia"
    }

    private fun ocultaProgressbar() {
        binding.detalheEncomendaProgressbar.visibility = View.GONE
    }

    private fun mostraProgressbar() {
        binding.detalheEncomendaProgressbar.visibility = View.VISIBLE
    }


//    private fun clicouBotaoVoltar() {
//        binding.detalheEncomendaVoltar.setOnClickListener {
//            botaoVoltar()
//        }
//    }

}