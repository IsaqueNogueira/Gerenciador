package com.example.gerenciadordeencomendas.ui.activity.fragment

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gerenciadordeencomendas.databinding.FormEncomendaBinding
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.FormEncomendaViewModel
import com.example.gerenciadordeencomendas.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormEncomendaFragment: Fragment() {

    private val controlador by lazy {
        findNavController()
    }
    private val viewModel: FormEncomendaViewModel by viewModel()
    private lateinit var binding: FormEncomendaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Adicionar"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FormEncomendaBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clicouBotaoAdicionarEncomenda()
    }

    private fun clicouBotaoAdicionarEncomenda() {
        binding.activityFormEncomendaBotao.setOnClickListener {
            val usuarioId = viewModel.auth.currentUser?.uid
            val codigoRastreio = binding.activityFormEncomendaCodigo.text.toString()
            val nomePacote = binding.activityFormEncomendaNomedopacote.text.toString()
            val dataAtualizado = Utils().dataHora()
            val dataCriado = Utils().dataHoraMillisegundos()
            val status = "Toque para atualizar"
            val firebaseId = ""
            val dataHoraApi = ""
            if (!TextUtils.isEmpty(codigoRastreio) && !TextUtils.isEmpty(nomePacote)) {
                mostraProgressBar()
                val encomenda = Encomenda(firebaseId, usuarioId.toString(), codigoRastreio, nomePacote, status, dataCriado, dataAtualizado, dataHoraApi)
                if (validaRastreio(codigoRastreio)) {
                    viewModel.salvarEncomenda(encomenda).addOnCompleteListener {
                        if (it.isSuccessful) {
                            ocularProgessBar()
                            vaiParaListaEncomendas()
                        } else {
                            ocularProgessBar()
                            binding.activityFormEncomendaMensagemErro.text =
                                "*Erro ao adicionar encomenda!"
                            Handler().postDelayed({
                                binding.activityFormEncomendaMensagemErro.text = ""
                            }, 4000)
                        }
                    }
                } else {
                    ocularProgessBar()
                    binding.activityFormEncomendaCodigo.setError("Código inválido")
                    binding.activityFormEncomendaCodigo.requestFocus()
                }
            }
            when {
                codigoRastreio.isEmpty() -> {
                    binding.activityFormEncomendaCodigo.setError("Campo obrigatório")
                    binding.activityFormEncomendaCodigo.requestFocus()
                }
                nomePacote.isEmpty() -> {
                    binding.activityFormEncomendaNomedopacote.setError("Campo obrigatório!")
                    binding.activityFormEncomendaNomedopacote.requestFocus()
                }
            }

        }
    }

    private fun vaiParaListaEncomendas() {
       val direction = FormEncomendaFragmentDirections.actionFormEncomendaToListaEncomendas()
        controlador.navigate(direction)
    }

    private fun validaRastreio(input: String): Boolean {
        val regex = Regex(pattern = "^[A-Z]{2}[0-9]{9}[A-Z]{2}\$", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }
    private fun mostraProgressBar(){
        binding.activityFormEncomendaProgressbar.visibility = View.VISIBLE
        binding.activityFormEncomendaBotao.visibility = View.GONE
    }

    private fun ocularProgessBar(){
        binding.activityFormEncomendaProgressbar.visibility = View.GONE
        binding.activityFormEncomendaBotao.visibility = View.VISIBLE
    }
}