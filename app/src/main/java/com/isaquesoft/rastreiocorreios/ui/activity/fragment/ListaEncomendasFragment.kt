package com.isaquesoft.rastreiocorreios.ui.activity.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.isaquesoft.rastreiocorreios.R
import com.isaquesoft.rastreiocorreios.databinding.ListaEncomendasBinding
import com.isaquesoft.rastreiocorreios.model.Encomenda
import com.isaquesoft.rastreiocorreios.ui.activity.LoginActivity
import com.isaquesoft.rastreiocorreios.ui.activity.adapters.ListaEncomendasAdapter
import com.isaquesoft.rastreiocorreios.ui.activity.viewmodel.ListaEncomendasViewModel
import com.isaquesoft.rastreiocorreios.utils.Utils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    lateinit var mAdView : AdView

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
        activity?.title = "Rastreio Correios"
        clicouBotaoAdicionarPacote()
        context?.let {
            MobileAds.initialize(it) {}
        }
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
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
    private fun configuraAlertDialogSair(context: Context) {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val nomeNoBanco = FirebaseFirestore.getInstance().collection("Usuarios").document(uid).get()
        nomeNoBanco.addOnSuccessListener {
            val usuarioLogado = it.get("nome")
            AlertDialog.Builder(context)
                .setTitle("Olá, $usuarioLogado!")
                .setMessage("Deseja realmente sair?")
                .setPositiveButton("Sim") { _, _ ->
                    viewModel.auth.signOut()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .setNegativeButton("Não") { _, _ -> }
                .show()
        }
    }

    private fun configuraRecyclerview() {
        viewModel.buscaTodasEncomendas().observe(this, Observer { encomendas ->
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

    @SuppressLint("MissingInflatedId")
    private fun criaAlertDialodFormEncomenda() {

        val inflat =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflat.inflate(R.layout.alert_dialog_form_encomenda, null);
        context?.let { context ->
            val alertDialog = AlertDialog.Builder(context)
                .setView(layout)
                .create()
            alertDialog.setOnShowListener {
                val buttonPositive: Button =
                    layout.findViewById(R.id.form_encomenda_positive_button)
                buttonPositive.setOnClickListener {
                    val codigoRastreioEditText: EditText =
                        layout.findViewById(R.id.form_encomenda_edittext_codigo) as EditText
                    val codigoRastreio = codigoRastreioEditText.text.toString()
                    val nomePacoteEditText: EditText =
                        layout.findViewById(R.id.form_encomenda_edittext_descricao) as EditText
                    val nomePacote = nomePacoteEditText.text.toString()
                    clicouBotaoAdicionarEncomenda(codigoRastreio, nomePacote, layout, alertDialog)
                }
                val buttonNegative: Button =
                    layout.findViewById(R.id.form_encomenda_negative_button)
                buttonNegative.setOnClickListener { alertDialog.dismiss() }
            }
            alertDialog.show()
        }
    }

    private fun clicouBotaoAdicionarPacote() {
        binding.floatingActionButton.setOnClickListener {
            criaAlertDialodFormEncomenda()
        }
    }

    private fun clicouBotaoAdicionarEncomenda(
        codigoRastreio: String,
        nomePacote: String,
        layout: View,
        alertDialog: AlertDialog
    ) {
        val usuarioId = viewModel.auth.currentUser?.uid
        val dataAtualizado = Utils().dataHora()
        val dataCriado = Utils().dataHoraMillisegundos()
        val status = "Toque para atualizar"
        val firebaseId = ""
        val dataHoraApi = ""
        if (!TextUtils.isEmpty(codigoRastreio) && !TextUtils.isEmpty(nomePacote)) {
            val encomenda = Encomenda(
                firebaseId,
                usuarioId.toString(),
                codigoRastreio,
                nomePacote,
                status,
                dataCriado,
                dataAtualizado,
                dataHoraApi
            )
            if (validaRastreio(codigoRastreio)) {
                viewModel.salvarEncomenda(encomenda).addOnCompleteListener {
                    if (it.isSuccessful) {
                        viewModel.buscaTodasEncomendas().observe(viewLifecycleOwner, Observer {
                            adapter.atualiza(it)
                            alertDialog.dismiss()
                        })
                    } else {
                        val mensagemErro: TextView =
                            layout.findViewById(R.id.form_encomenda_mensagemErro) as TextView
                        mensagemErro.visibility = View.VISIBLE
                    }
                }
            } else {
                val codigoRastreioEditText: EditText =
                    layout.findViewById(R.id.form_encomenda_edittext_codigo) as EditText
                codigoRastreioEditText.setError("Código inválido")
                codigoRastreioEditText.requestFocus()
            }
        }
        when {
            codigoRastreio.isEmpty() -> {
                val codigoRastreioEditText: EditText =
                    layout.findViewById(R.id.form_encomenda_edittext_codigo) as EditText
                codigoRastreioEditText.setError("Campo obrigatório")
                codigoRastreioEditText.requestFocus()
            }
            nomePacote.isEmpty() -> {
                val nomePacoteEditText: EditText =
                    layout.findViewById(R.id.form_encomenda_edittext_descricao) as EditText
                nomePacoteEditText.setError("Campo obrigatório!")
                nomePacoteEditText.requestFocus()
            }
        }

    }

    private fun validaRastreio(input: String): Boolean {
        val regex =
            Regex(pattern = "^[A-Z]{2}[0-9]{9}[A-Z]{2}\$", options = setOf(RegexOption.IGNORE_CASE))
        return regex.matches(input)
    }

}