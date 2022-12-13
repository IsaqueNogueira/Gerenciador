package com.example.gerenciadordeencomendas.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentOnAttachListener
import com.example.gerenciadordeencomendas.R
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.ui.activity.extensions.transacaoFragment
import com.example.gerenciadordeencomendas.ui.activity.fragment.DetalheEncomendaFragment
import com.example.gerenciadordeencomendas.ui.activity.fragment.ListaEncomendasFragment
import com.example.gerenciadordeencomendas.utils.verificaConexao
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG_DETALHE_ENCOMENDA = "detalhe_encomenda"
class EncomendasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encomendas)
         if (savedInstanceState == null){
             abreListaEncomendas()
         }
        if (supportFragmentManager.findFragmentByTag(TAG_DETALHE_ENCOMENDA) != null){
            supportActionBar?.hide()
        }

    }

    init {
        val fm = supportFragmentManager

        val listener = FragmentOnAttachListener { fragmentManager, fragment ->
            when(fragment){
                is ListaEncomendasFragment ->{
                    configuraListaEncomendaFragment(fragment)

                }
                is DetalheEncomendaFragment ->{
                    configuraDetalheEncomendaFragment(fragment)
                    verificaConexao()
                }
            }

        }
        fm.addFragmentOnAttachListener(listener)
    }

    private fun configuraDetalheEncomendaFragment(fragment: DetalheEncomendaFragment) {

        fragment.copia = this::copiar

        fragment.botaoVoltar = {
            supportFragmentManager.findFragmentByTag(TAG_DETALHE_ENCOMENDA)?.let {
                removeFragmentDetalheEncomenda(fragment)
            }
        }
    }

    private fun copiar(encomenda: Encomenda){
        val clipBoard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", encomenda.codigoRastreio)
        clipBoard.setPrimaryClip(clip)
    }

    private fun removeFragmentDetalheEncomenda(fragment: DetalheEncomendaFragment) {
        transacaoFragment {
            addToBackStack(null)
            remove(fragment)
        }
        supportFragmentManager.popBackStack()
    }

    private fun configuraListaEncomendaFragment(fragment: ListaEncomendasFragment) {
        fragment.quandoClicarSair = this::vaiParaLogin
        fragment.quandoClicarAdicionarEncomenda = this::vaiParaFormEncomenda
        fragment.quandoClicarNoItem = this::vaiParaDetalheEncomenda
    }

    private fun abreListaEncomendas() {
        transacaoFragment {
            replace(R.id.activity_encomendas_container, ListaEncomendasFragment())
        }
    }

    private fun vaiParaFormEncomenda() {
        Intent(this, FormEncomendaActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun vaiParaLogin() {
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun vaiParaDetalheEncomenda(encomenda: Encomenda) {
        val fragment = DetalheEncomendaFragment()
        val dados = Bundle()
        dados.putString(CHAVE_ENCOMENDA_ID, encomenda.firebaseId)
        fragment.arguments = dados
        transacaoFragment {
            addToBackStack(null)
            replace(R.id.activity_encomendas_container, fragment, TAG_DETALHE_ENCOMENDA)
        }
    }

}