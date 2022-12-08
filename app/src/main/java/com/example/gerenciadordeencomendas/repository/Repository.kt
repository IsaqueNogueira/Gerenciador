package com.example.gerenciadordeencomendas.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gerenciadordeencomendas.databinding.ItemEncomendaBinding
import com.example.gerenciadordeencomendas.model.Encomenda
import com.example.gerenciadordeencomendas.model.Usuario
import com.example.gerenciadordeencomendas.utils.Utils
import com.example.gerenciadordeencomendas.webcliente.model.ApiCorreios
import com.example.gerenciadordeencomendas.webcliente.model.RastreioWebCliente
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Repository {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    private val webCliente by lazy {
        RastreioWebCliente()
    }

    fun cadastraUsuario(usuario: Usuario): Task<AuthResult> {
        val resultado = auth.createUserWithEmailAndPassword(usuario.email, usuario.senha)
            .addOnCompleteListener {
            }
        return resultado
    }

    fun salvarNomeDoUsuario(nome: String): Task<Void> {
        val user: MutableMap<String, Any> = HashMap()
        user["nome"] = nome
        val usuarioId = auth.currentUser?.uid
        val documentReference = db.collection("Usuarios").document(usuarioId.toString())
        return documentReference.set(user).addOnSuccessListener {
            Log.d(
                "db",
                "Sucesso ao salvar os dados"
            )
        }.addOnFailureListener { e -> Log.d("db_error", "Erros so salvar os dados$e") }

    }


    fun salvarEncomenda(encomenda: Encomenda): Task<Void> {
        val documentReference = db.collection("Encomendas").document()
        return documentReference.set(encomenda).addOnSuccessListener {
            Log.i("Salvar encomenda", "salvarEncomenda: Sucesso ao salvar encomenda")
        }

    }

    fun buscaTodasEncomendas() {
        val usuarioId = auth.currentUser?.uid
        db.collection("Encomendas")
            .orderBy("dataCriado", Query.Direction.DESCENDING)
            .whereEqualTo("usuarioId", usuarioId)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val encomenda: List<Encomenda> = emptyList()
                    val encomendas = encomenda.toMutableList()
                    for (document in it.getResult()) {
                        val usuarioId = document.getString("usuarioId").toString()
                        val codigoRastreio = document.getString("codigoRastrio").toString()
                        val nomePacote = document.getString("nomePacote").toString()
                        val status = document.getString("status").toString()
                        val dataCriado = document.getLong("dataCriado")!!
                        val dataAtualizado = document.getString("dataAtualizado").toString()
                        val firebaseId = document.id
                        encomendas.add(
                            Encomenda(
                                firebaseId,
                                usuarioId,
                                codigoRastreio,
                                nomePacote,
                                status,
                                dataCriado,
                                dataAtualizado
                            )
                        )
                        liveDataEncomenda.value = encomendas
                    }
                }
            }
    }

    val liveDataEncomenda = MutableLiveData<List<Encomenda>>()

    fun buscaEncomendaPorId(encomendaId: String) {
        db.collection("Encomendas")
            .document(encomendaId)
            .addSnapshotListener { documento, error ->
                if (documento != null) {
                    documento.getLong("dataCriado")?.let { dataCriado ->
                        val usuarioId = documento.getString("usuarioId").toString()
                        val codigoRastreio = documento.getString("codigoRastreio").toString()
                        val nomePacote = documento.getString("nomePacote").toString()
                        val status = documento.getString("status").toString()
                        val dataAtualizado = documento.getString("dataAtualizado").toString()
                        val firebaseId = documento.id
                        val encomendas = Encomenda(
                            firebaseId,
                            usuarioId,
                            codigoRastreio,
                            nomePacote,
                            status,
                            dataCriado,
                            dataAtualizado
                        )
                        liveDataEncomendaId.value = encomendas
                    }


                }
            }
    }

    val liveDataEncomendaId = MutableLiveData<Encomenda>()

    fun atualizaStatus(encomendaId: String, status: String) {
        val data = Utils().dataHora()
        db.collection("Encomendas").document(encomendaId)
            .update("dataAtualizado", "Atualizado em: $data", "status", status)
            .addOnCompleteListener {

            }
    }

    fun excluirEncomenda(firebaseId: String): Task<Void> {
        return db.collection("Encomendas").document(firebaseId)
            .delete().addOnCompleteListener {
            }
    }


    suspend fun buscaWebCliente(codigo: String): ApiCorreios {
        val user = "isaquecross15@gmail.com"
        val token = "0a40c26417782427548f2aeb57f74c4038faf1f26ac662379425e35c848cce2b"
        return webCliente.buscaRastreio(user, token, codigo)
    }

}