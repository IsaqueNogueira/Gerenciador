package com.example.gerenciadordeencomendas.ui.activity.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.databinding.ItemRastreioBinding
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.utils.Utils
import com.example.gerenciadordeencomendas.webcliente.model.Event
import com.example.gerenciadordeencomendas.webcliente.model.Evento
import java.text.SimpleDateFormat
import java.util.*

class DetalheEncomendaAdapter(
    private val context: Context,
    evento: List<Event> = emptyList()
) : RecyclerView.Adapter<DetalheEncomendaAdapter.ViewHolder>() {


    private val evento = evento.toMutableList()
    private lateinit var encomendaId: String
    private lateinit var ultimoStatus: Event
    private lateinit var primeiroStatus: Event

    private val repository by lazy {
        Repository()
    }

    inner class ViewHolder(private val binding: ItemRastreioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var evento: Event

        fun vincula(evento: Event) {
            this.evento = evento

            val status = binding.itemRastreioStatus
            status.text = evento.events

            val subStatus = binding.itemRastreioSubstatus

            val data = binding.itemRastreioData
            val date = Utils().formataDataConvertida(evento.date)
            data.text = date

            when {
                evento.local == "País" -> {
                    subStatus.text = evento.local
                }
                evento.destination_city != null -> {
                    subStatus.text =
                        "De: ${evento.local} ${evento.city} - ${evento.uf} \nPara: ${evento.destination_local} ${evento.destination_city} - ${evento.uf}"
                }
                evento.destination_city == null -> {
                    subStatus.text = evento.local + " - " + evento.city + "/" + evento.uf
                }



            }

            if(evento.events == "Objeto entregue ao destinatário"){
                binding.itemRastreioLinhaDoTempoCirculoEntregue.visibility = View.VISIBLE
                binding.itemRastreioCheck.visibility = View.VISIBLE
            }
            if(evento.events == "Objeto saiu para entrega ao destinatário"){
                binding.itemRastreioLinhaDoTempoCirculoSaiuParaEntrega.visibility = View.VISIBLE
                binding.itemRastreioCarrinho.visibility = View.VISIBLE
            }

            if(evento.events == "Objeto postado" || evento.events == "Objeto postado após o horário limite da unidade"){
                binding.itemRastreioLinhaDoTempoCirculoPostado.visibility = View.VISIBLE
                binding.itemRastreioPostado.visibility = View.VISIBLE
            }

            if (evento.events == "Objeto recebido pelos Correios do Brasil"){
                binding.itemRastreioLinhaDoTempoCirculoBrasil.visibility = View.VISIBLE
                binding.itemRastreioBrasil.visibility = View.VISIBLE
            }

            if (evento.events == "Aguardando pagamento"){
                binding.itemRastreioLinhaDoTempoCirculoTaxa.visibility = View.VISIBLE
                binding.itemRastreioTaxa.visibility = View.VISIBLE
            }
            if (evento.events == "Pagamento confirmado"){
                binding.itemRastreioLinhaDoTempoCirculoPagamentoConfirmado.visibility = View.VISIBLE
                binding.itemRastreioPagamento.visibility = View.VISIBLE
            }
            if (evento.events == "Fiscalização aduaneira finalizada"){
                binding.itemRastreioLinhaDoTempoCirculoFiscalizacao.visibility = View.VISIBLE
                binding.itemRastreioFiscalizacao.visibility = View.VISIBLE
            }

            if(evento.events != primeiroStatus.events){
                binding.itemRastreioLinhaDoTempo2.visibility = View.VISIBLE
            }

            if(evento.comment != null)  {
                subStatus.text =
                    evento.local + " - " + evento.city + "/" + evento.uf + "\n" + evento.comment
            }
            if( evento.events == ultimoStatus.events && evento.local == ultimoStatus.local &&
                evento.date == ultimoStatus.date ){
                status.setTextColor(Color.parseColor("#000000"))
                subStatus.setTextColor(Color.parseColor("#000000"))
                binding.itemRastreioLinhaDoTempo.visibility = View.GONE
            }

            repository.atualizaStatus(encomendaId, ultimoStatus.events)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemRastreioBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = evento[position]
        holder.vincula(evento)
    }

    override fun getItemCount(): Int = evento.size

    fun atualiza(evento: List<Event>, encomendaId: String, ultimoStatus: Event, primeiroStatus: Event) {
        this.encomendaId = encomendaId
        this.ultimoStatus = ultimoStatus
        this.primeiroStatus = primeiroStatus
        this.evento.clear()
        this.evento.addAll(evento)
        notifyDataSetChanged()
    }
}