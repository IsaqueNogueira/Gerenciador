package com.example.gerenciadordeencomendas.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciadordeencomendas.databinding.ActivityCadastroBinding
import com.example.gerenciadordeencomendas.model.Usuario
import com.example.gerenciadordeencomendas.repository.Repository
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.CadastroViewModel
import com.example.gerenciadordeencomendas.ui.activity.viewmodel.factory.CadastroViewModelFactory
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        val repository = Repository()
        val factory = CadastroViewModelFactory(repository)
        val provedor = ViewModelProvider(this, factory)
        provedor.get(CadastroViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        vaiParaLogin()

        binding.activityCadastroBotaoCadastrar.setOnClickListener {
            val nome = binding.activityCadastroNome.text.toString()
            val email = binding.activityCadastroEmail.text.toString()
            val senha = binding.activityCadastroSenha.text.toString()
            if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {
                mostraProgressBar()
                val usuario = Usuario(nome, email, senha)
                viewModel.cadastraUsuario(usuario).addOnSuccessListener {
                    Intent(this, EncomendasActivity::class.java).apply {
                        viewModel.salvarNomeDoUsuario(usuario.nome).addOnCompleteListener {
                            if (it.isSuccessful) {
                                startActivity(this)
                                finish()
                            }
                        }
                    }
                }.addOnFailureListener { exception ->
                    ocultaProgressBar()
                    val mensagemErro = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "*Digite uma senha com no mínimo 6 caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "*Digite um email válido!"
                        is FirebaseAuthUserCollisionException -> "*Esta conta já foi cadastrada!"
                        is FirebaseNetworkException -> "*Sem conexão com a internet!"
                        else -> "*Erro ao cadastrar usuário!"
                    }
                    binding.activityCadastroMensagemErro.text = mensagemErro
                    Handler().postDelayed({
                        binding.activityCadastroMensagemErro.text = ""
                    }, 4000)

                }
            }
            when {
                TextUtils.isEmpty(nome) -> {
                    binding.activityCadastroNome.setError("Preencha este campo")
                    binding.activityCadastroNome.requestFocus()
                }
                TextUtils.isEmpty(email) -> {
                    binding.activityCadastroEmail.setError("Preencha este campo")
                    binding.activityCadastroEmail.requestFocus()
                }
                TextUtils.isEmpty(senha) -> {
                    binding.activityCadastroSenha.setError("Preencha este campo")
                    binding.activityCadastroSenha.requestFocus()
                }
            }


        }

    }

    private fun vaiParaLogin() {
        binding.activityCadastroBotaoVaiparalogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
    }

    private fun mostraProgressBar() {
        binding.activityCadastroProgessbar.visibility = View.VISIBLE
        binding.activityCadastroBotaoCadastrar.visibility = View.GONE
    }

    private fun ocultaProgressBar() {
        binding.activityCadastroProgessbar.visibility = View.GONE
        binding.activityCadastroBotaoCadastrar.visibility = View.VISIBLE
    }
}