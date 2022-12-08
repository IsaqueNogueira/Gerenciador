package com.example.gerenciadordeencomendas.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.gerenciadordeencomendas.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        vaiParaCadastro()
        verificaUsuarioLogado()
        clicouBotaoLogin()
    }

    private fun clicouBotaoLogin() {
        binding.activityLoginBotaoLogin.setOnClickListener {
            val email = binding.activityLoginEmail.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {
                mostraProgressBar()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            vaiParaListaEncomendas()
                            finish()
                        } else {
                            ocultaProgressBar()
                        }
                    }.addOnFailureListener { exception ->
                        ocultaProgressBar()
                        val mensagemErro = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "*Email ou senha incorretos!"
                            is FirebaseNetworkException -> "*Sem conexão com a internet!"
                            else -> "*Ocorreu um erro ao fazer login, verifique a senha!"
                        }
                        binding.activityLoginMensagemErro.text = mensagemErro
                        Handler().postDelayed({
                            binding.activityLoginMensagemErro.text = ""
                        }, 4000)

                    }
            }
            when {
                TextUtils.isEmpty(email) -> {
                    binding.activityLoginEmail.setError("Preencha este campo")
                    binding.activityLoginEmail.requestFocus()
                }
                TextUtils.isEmpty(senha) -> {
                    binding.activityLoginSenha.setError("Preencha este campo")
                    binding.activityLoginSenha.requestFocus()
                }
            }

        }
    }

    private fun verificaUsuarioLogado() {
        val usuarioLogado = FirebaseAuth.getInstance().currentUser
        if (usuarioLogado != null) {
            vaiParaListaEncomendas()
        }
    }

    private fun vaiParaListaEncomendas() {
        Intent(this, EncomendasActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun vaiParaCadastro() {
        binding.activityLoginBotaoVaiparacadastro.setOnClickListener {
            Intent(this, CadastroActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    private fun mostraProgressBar(){
        binding.activityLoginProgessbar.visibility = View.VISIBLE
        binding.activityLoginBotaoLogin.visibility = View.GONE
    }
    private fun ocultaProgressBar(){
        binding.activityLoginProgessbar.visibility = View.GONE
        binding.activityLoginBotaoLogin.visibility = View.VISIBLE
    }
}