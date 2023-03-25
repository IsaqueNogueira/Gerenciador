package com.isaquesoft.rastreiocorreios.ui.activity.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.isaquesoft.rastreiocorreios.R
import com.isaquesoft.rastreiocorreios.databinding.DetalheEncomendaBinding
import com.isaquesoft.rastreiocorreios.ui.activity.adapters.DetalheEncomendaAdapter
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.ComponentesVisuais
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.DetalheEncomendaViewModel
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.EstadoAppViewModel
import com.isaquesoft.rastreiocorreios.utils.Utils
import com.isaquesoft.rastreiocorreios.utils.verificaConexao
import com.isaquesoft.rastreiocorreios.webcliente.model.Event
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalheEncomendaFragment : Fragment(R.layout.detalhe_encomenda) {
    private val argumento by navArgs<DetalheEncomendaFragmentArgs>()
    private val encomendaId: String by lazy {
        argumento.encomendaId
    }
    private val adpter by lazy {
        context?.let {
            DetalheEncomendaAdapter(it)
        } ?: throw IllegalArgumentException("Contexto inválido")
    }

    private val controlador by lazy { findNavController() }
    private lateinit var binding: DetalheEncomendaBinding

    private val viewModel: DetalheEncomendaViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DetalheEncomendaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        estadoAppViewModel.temComponentes = ComponentesVisuais(true, true)
        requireActivity().title = "Encomenda"
        mostraEncomenda()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity() != null) {
                val direction = DetalheEncomendaFragmentDirections.actionDetalheEncomendaToListaEncomendas()
                controlador.navigate(direction)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun mostraEncomenda() {
        mostraProgressbar()
        viewModel.buscaEncomendaPorId(encomendaId)
            .observe(
                viewLifecycleOwner,
                Observer { encomenda ->
                    lifecycleScope.launch {
                        val nomePacote = binding.detalheEncomendaNomePacote
                        nomePacote.text = encomenda.nomePacote
                        val codigoRastreio = binding.detalheEncomendaCodigoRastreio
                        codigoRastreio.text = encomenda.codigoRastreio

                        val botaoCopiar = binding.detalheEncomendaBotaoCopiar

                        botaoCopiar.setOnClickListener {
                            val clipBoard =
                                activity?.applicationContext?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip: ClipData =
                                ClipData.newPlainText("simple text", encomenda.codigoRastreio)
                            clipBoard.setPrimaryClip(clip)
                            Toast.makeText(context, "Código de rastreio copiado", Toast.LENGTH_SHORT)
                                .show()
                        }

                        try {
                            viewModel.buscaWebClienteMelhorRastreio(encomenda.codigoRastreio)?.let {
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

                                    diasDePostagem(ultimoStatus, primeiroStatus)

                                    adpter.atualiza(
                                        rastreioMelhorRastreio.data.events,
                                        encomendaId,
                                        ultimoStatus,
                                        primeiroStatus,
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
                        } catch (e: Exception) {
                            Log.i("TAG", "mostraEncomenda: $e")
                            mostraErro()
                        }
                    }
                },
            )
    }

    private fun mostraErro() {
        verificaConexao()
        val mensagemErro = binding.detalheEncomendaMensagemErro
        val iconeErro = binding.detalheEncomendaIconErro
        mensagemErro.visibility = View.VISIBLE
        iconeErro.visibility = View.VISIBLE
        ocultaProgressbar()
    }

    private fun diasDePostagem(ultimoStatus: Event, primeiroStatus: Event) {
        val dataPostagem = primeiroStatus.date

        val data = Utils().formataDataConvertida(dataPostagem)

        var dataHoje = Utils().data()
        if (ultimoStatus.events == "Objeto entregue ao destinatário") {
            val data = Utils().formataDataConvertida(ultimoStatus.date)
            dataHoje = data
        }
        val diasEnviado = Utils().dias(data, dataHoje)

        var dia: String
        var textoEnviado: String
        if (diasEnviado < 2) {
            dia = "dia"
        } else {
            dia = "dias"
        }

        if (ultimoStatus.events == "Objeto entregue ao destinatário") {
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
}
