package com.example.gerenciadordeencomendas.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.databinding.ItemRastreioBinding
import com.example.gerenciadordeencomendas.repository.Repository
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
            val circuloLinhaDoTempo = binding.itemRastreioLinhaDoTempoCirculo

            val data = binding.itemRastreioData
            data.text = evento.date

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
            if(evento.comment != null)  {
                subStatus.text =
                    evento.local + " - " + evento.city + "/" + evento.uf + "\n" + evento.comment
            }
            if( evento.events == ultimoStatus.events && evento.local == ultimoStatus.local &&
                evento.date == ultimoStatus.date ){
                status.setTextColor(Color.parseColor("#000000"))
                subStatus.setTextColor(Color.parseColor("#000000"))
                circuloLinhaDoTempo.setBackgroundResource(R.drawable.view_circular_preto)
            }

            var statusAtualizar: String
            if (ultimoStatus.events == "Objeto entregue ao destinatário"){
                 statusAtualizar = "Entregue"
            }else{
                statusAtualizar = ultimoStatus.events
            }
            repository.atualizaStatus(encomendaId, statusAtualizar)

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

    fun atualiza(evento: List<Event>, encomendaId: String, ultimoStatus: Event) {
        this.encomendaId = encomendaId
        this.ultimoStatus = ultimoStatus
        this.evento.clear()
        this.evento.addAll(evento)
        notifyDataSetChanged()
    }
}