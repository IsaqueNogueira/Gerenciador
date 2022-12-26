package com.isaquesoft.rastreiocorreios.ui.activity.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isaquesoft.rastreiocorreios.databinding.ItemRastreioBinding
import com.isaquesoft.rastreiocorreios.repository.Repository
import com.isaquesoft.rastreiocorreios.utils.Utils
import com.isaquesoft.rastreiocorreios.webcliente.model.Event

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
                        "De: ${evento.local} ${evento.city}/${evento.uf} \nPara: ${evento.destination_local} ${evento.destination_city}/${evento.uf}"
                }
                evento.destination_city == null -> {
                    subStatus.text = evento.local + " - " + evento.city + "/" + evento.uf
                }
            }

            if(evento.events == "Objeto entregue ao destinatário"){
                binding.itemRastreioLinhaDoTempoCirculoEntregue.visibility = View.VISIBLE
                binding.itemRastreioCheck.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#54B435"))
            }else{
                binding.itemRastreioLinhaDoTempoCirculoEntregue.visibility = View.GONE
                binding.itemRastreioCheck.visibility = View.GONE
            }
            if(evento.events == "Objeto saiu para entrega ao destinatário"){
                binding.itemRastreioLinhaDoTempoCirculoSaiuParaEntrega.visibility = View.VISIBLE
                binding.itemRastreioCarrinho.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#4caf50"))
            }else{
                binding.itemRastreioLinhaDoTempoCirculoSaiuParaEntrega.visibility = View.GONE
                binding.itemRastreioCarrinho.visibility = View.GONE
            }

            if(evento.events == "Objeto postado" || evento.events == "Objeto postado após o horário limite da unidade"){
                binding.itemRastreioLinhaDoTempoCirculoPostado.visibility = View.VISIBLE
                binding.itemRastreioPostado.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#697DFF"))
            }else{
                binding.itemRastreioLinhaDoTempoCirculoPostado.visibility = View.GONE
                binding.itemRastreioPostado.visibility = View.GONE
            }

            if (evento.events == "Objeto recebido pelos Correios do Brasil"){
                binding.itemRastreioLinhaDoTempoCirculoBrasil.visibility = View.VISIBLE
                binding.itemRastreioBrasil.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#008E76"))

            }else{
                binding.itemRastreioLinhaDoTempoCirculoBrasil.visibility = View.GONE
                binding.itemRastreioBrasil.visibility = View.GONE
            }

            if (evento.events == "Aguardando pagamento"){
                binding.itemRastreioLinhaDoTempoCirculoTaxa.visibility = View.VISIBLE
                binding.itemRastreioTaxa.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#f57c00"))

            }else{
                binding.itemRastreioLinhaDoTempoCirculoTaxa.visibility = View.GONE
                binding.itemRastreioTaxa.visibility = View.GONE
            }
            if (evento.events == "Pagamento confirmado"){
                binding.itemRastreioLinhaDoTempoCirculoPagamentoConfirmado.visibility = View.VISIBLE
                binding.itemRastreioPagamento.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#2e7d32"))

            }else{
                binding.itemRastreioLinhaDoTempoCirculoPagamentoConfirmado.visibility = View.GONE
                binding.itemRastreioPagamento.visibility = View.GONE
            }
            if (evento.events == "Fiscalização aduaneira finalizada"){
                binding.itemRastreioLinhaDoTempoCirculoFiscalizacao.visibility = View.VISIBLE
                binding.itemRastreioFiscalizacao.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#ffc107"))
            }else{
                binding.itemRastreioLinhaDoTempoCirculoFiscalizacao.visibility = View.GONE
                binding.itemRastreioFiscalizacao.visibility = View.GONE
            }
            if(evento.events == "Objeto em trânsito - por favor aguarde" || evento.events == "Encaminhado para fiscalização aduaneira"){
                status.setTextColor(Color.parseColor("#008EFF"))
            }

            if(evento.events == "Direcionado para entrega interna a pedido do cliente"){
                binding.itemRastreioLinhaDoTempoCirculoDirecionado.visibility = View.VISIBLE
                binding.itemRastreioDirecionado.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#ffc107"))
            }else{
                binding.itemRastreioLinhaDoTempoCirculoDirecionado.visibility = View.GONE
                binding.itemRastreioDirecionado.visibility = View.GONE
            }
            if(evento.events == "Objeto aguardando retirada no endereço indicado"){
                binding.itemRastreioLinhaDoTempoCirculoRetirada.visibility = View.VISIBLE
                binding.itemRastreioRetirada.visibility = View.VISIBLE
                status.setTextColor(Color.parseColor("#ffc107"))
            }else{
                binding.itemRastreioLinhaDoTempoCirculoRetirada.visibility = View.GONE
                binding.itemRastreioRetirada.visibility = View.GONE
            }

            if(evento.events != primeiroStatus.events){
                binding.itemRastreioLinhaDoTempo2.visibility = View.VISIBLE
            }else{
                binding.itemRastreioLinhaDoTempo2.visibility = View.GONE
            }

            if(evento.comment != null)  {
                subStatus.text =
                    evento.comment +"\n"+ evento.local + " - " + evento.city + "/" + evento.uf
            }
            if( evento.events == ultimoStatus.events && evento.local == ultimoStatus.local &&
                evento.date == ultimoStatus.date ){
                binding.itemRastreioLinhaDoTempo.visibility = View.GONE
            }else{
                binding.itemRastreioLinhaDoTempo.visibility = View.VISIBLE
            }

            repository.atualizaStatus(encomendaId, ultimoStatus.events, ultimoStatus.date)

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