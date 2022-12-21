package com.example.gerenciadordeencomendas.ui.activity.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.databinding.ItemEncomendaBinding
import com.example.gerenciadordeencomendas.model.Encomenda

class ListaEncomendasAdapter(
    private val context: Context,
    encomenda: List<Encomenda> = emptyList(),
    var quandoClicarNoItem: (encomenda: Encomenda) -> Unit = {},
    var quandoSegurarNoItem: (encomenda: Encomenda) -> Unit = {}
) : RecyclerView.Adapter<ListaEncomendasAdapter.ViewHolder>() {


    private val encomenda = encomenda.toMutableList()

    inner class ViewHolder(private val binding: ItemEncomendaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var encomenda: Encomenda

        init {
            itemView.setOnClickListener {
                if (::encomenda.isInitialized) {
                    quandoClicarNoItem(encomenda)
                }
            }

            itemView.setOnLongClickListener {
                if (::encomenda.isInitialized) {
                    quandoSegurarNoItem(encomenda)
                    binding.itemEncomendaCardview.setBackgroundColor(Color.parseColor("#b2b2b2"))
                }
                true
            }

        }

        fun vincula(encomenda: Encomenda) {
            this.encomenda = encomenda
            val nomePacote = binding.itemEncomendaNomePacote
            nomePacote.text = encomenda.nomePacote

            val status = binding.itemEncomendaStatus
            status.text = encomenda.status

            val dataAtualizado = binding.itemEncomendaData
            dataAtualizado.text = encomenda.dataAtualizado

            if (encomenda.status == "Objeto entregue ao destinatário") {
                binding.itemEncomendaCheck.visibility = View.VISIBLE
                binding.itemEncomendaIconEntregue.visibility = View.VISIBLE
                nomePacote.setTextColor(Color.parseColor("#7E7E7E"))
            } else {
                binding.itemEncomendaCheck.visibility = View.GONE
                binding.itemEncomendaIconEntregue.visibility = View.GONE
                nomePacote.setTextColor(Color.parseColor("#000000"))
            }

            if (encomenda.status == "Objeto postado" || encomenda.status == "Objeto postado após o horário limite da unidade") {
                binding.itemEncomendaLinhaDoTempoCirculoPostado.visibility = View.VISIBLE
                binding.itemEncomendaPostado.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoPostado.visibility = View.GONE
                binding.itemEncomendaPostado.visibility = View.GONE
            }

            if (encomenda.status == "Objeto saiu para entrega ao destinatário") {
                binding.itemEncomendaLinhaDoTempoCirculoSaiuParaEntrega.visibility = View.VISIBLE
                binding.itemEncomendaCarrinho.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoSaiuParaEntrega.visibility = View.GONE
                binding.itemEncomendaCarrinho.visibility = View.GONE
            }

            if (encomenda.status == "Objeto recebido pelos Correios do Brasil") {
                binding.itemEncomendaLinhaDoTempoCirculoBrasil.visibility = View.VISIBLE
                binding.itemEncomendaBrasil.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoBrasil.visibility = View.GONE
                binding.itemEncomendaBrasil.visibility = View.GONE
            }

            if (encomenda.status == "Aguardando pagamento") {
                binding.itemEncomendaLinhaDoTempoCirculoTaxa.visibility = View.VISIBLE
                binding.itemEncomendaTaxa.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoTaxa.visibility = View.GONE
                binding.itemEncomendaTaxa.visibility = View.GONE
            }

            if (encomenda.status == "Pagamento confirmado") {
                binding.itemEncomendaLinhaDoTempoCirculoPagamentoConfirmado.visibility =
                    View.VISIBLE
                binding.itemEncomendaPagamento.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoPagamentoConfirmado.visibility = View.GONE
                binding.itemEncomendaPagamento.visibility = View.GONE
            }

            if (encomenda.status == "Fiscalização aduaneira finalizada") {
                binding.itemEncomendaLinhaDoTempoCirculoFiscalizacao.visibility = View.VISIBLE
                binding.itemEncomendaFiscalizacao.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoFiscalizacao.visibility = View.GONE
                binding.itemEncomendaFiscalizacao.visibility = View.GONE
            }

            if (encomenda.status == "Fiscalização aduaneira finalizada") {
                binding.itemEncomendaLinhaDoTempoCirculoFiscalizacao.visibility = View.VISIBLE
                binding.itemEncomendaFiscalizacao.visibility = View.VISIBLE
            } else {
                binding.itemEncomendaLinhaDoTempoCirculoFiscalizacao.visibility = View.GONE
                binding.itemEncomendaFiscalizacao.visibility = View.GONE
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemEncomendaBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val encomenda = encomenda[position]
        holder.vincula(encomenda)
    }

    override fun getItemCount(): Int = encomenda.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualiza(encomenda: List<Encomenda>) {
        this.encomenda.clear()
        this.encomenda.addAll(encomenda)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(encomenda: Encomenda) {
        this.encomenda.remove(encomenda)
        notifyDataSetChanged()
    }

}


