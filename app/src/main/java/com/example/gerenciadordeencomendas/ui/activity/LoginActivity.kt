package com.example.gerenciadordeencomendas.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gerenciadordeencomendas.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.lang.Exception

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
        recuperarSenha()
    }

    private fun recuperarSenha() {
        binding.activityLoginBotaoEsqueceuSenha.setOnClickListener {
            val email = binding.activityLoginEmail.text.toString()
            if (!TextUtils.isEmpty(email)) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            mostraMensagemDeErro("Enviamos uma mensagem para o seu email com o link de recuperação de senha!")
                        }
                    }.addOnFailureListener {
                        verificaErro(it)
                    }
            } else {
                mostraMensagemDeErro("Digite o email de sua conta para recuperar a senha!")
            }
        }
    }

    private fun verificaErro(it: Exception) {
        val mensagemErro = when (it) {
            is FirebaseAuthInvalidCredentialsException -> "*Email inválido!"
            is FirebaseAuthInvalidUserException -> "Email não cadastrado!"

            else -> ""
        }
        mostraMensagemDeErro(mensagemErro)
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

                        Log.i("TAG", "clicouBotaoLogin: $exception")
                        val mensagemErro = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "*Email ou senha incorretos!"
                            is FirebaseNetworkException -> "*Sem conexão com a internet!"
                            is FirebaseAuthInvalidUserException -> "Não há registro de usuário correspondente a este email."
                            is FirebaseTooManyRequestsException -> "O acesso a esta conta foi temporariamente desativado devido a muitas tentativas de login malsucedidas. Você pode restaurá-lo imediatamente redefinindo sua senha ou pode tentar novamente mais tarde"
                            else -> "*Ocorreu um erro ao fazer login, verifique a senha!"
                        }
                        mostraMensagemDeErro(mensagemErro)

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

    private fun mostraMensagemDeErro(mensagemErro: String) {
        binding.activityLoginMensagemErro.text = mensagemErro
        Handler().postDelayed({
            binding.activityLoginMensagemErro.text = ""
        }, 8000)
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
            }
        }
    }

    private fun mostraProgressBar() {
        binding.activityLoginProgessbar.visibility = View.VISIBLE
        binding.activityLoginBotaoLogin.visibility = View.GONE
    }

    private fun ocultaProgressBar() {
        binding.activityLoginProgessbar.visibility = View.GONE
        binding.activityLoginBotaoLogin.visibility = View.VISIBLE
    }
}