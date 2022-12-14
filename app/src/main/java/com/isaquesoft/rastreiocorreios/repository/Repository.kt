package com.isaquesoft.rastreiocorreios.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaquesoft.rastreiocorreios.model.Encomenda
import com.isaquesoft.rastreiocorreios.model.Usuario
import com.isaquesoft.rastreiocorreios.utils.Utils
import com.isaquesoft.rastreiocorreios.webcliente.model.ApiMelhorRastreio
import com.isaquesoft.rastreiocorreios.webcliente.model.RastreioWebClientMelhorEnvio
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Repository {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

//    private val webClienteApiCorreios by lazy {
//        RastreioWebCliente()
//    }

    private val webClientMelhorRastreio by lazy {
        RastreioWebClientMelhorEnvio()
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

    fun buscaTodasEncomendas(): LiveData<List<Encomenda>> {
        val liveDataEncomenda = MutableLiveData<List<Encomenda>>()
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
                        val codigoRastreio = document.getString("codigoRastreio").toString()
                        val nomePacote = document.getString("nomePacote").toString()
                        val status = document.getString("status").toString()
                        val dataCriado = document.getLong("dataCriado")!!
                        val dataAtualizado = document.getString("dataAtualizado").toString()
                        val firebaseId = document.id
                        val dataHoraApi = document.getString("dataHoraApi").toString()
                        encomendas.add(
                            Encomenda(
                                firebaseId,
                                usuarioId,
                                codigoRastreio,
                                nomePacote,
                                status,
                                dataCriado,
                                dataAtualizado,
                                dataHoraApi
                            )
                        )
                        liveDataEncomenda.value = encomendas
                    }
                }

            }
        return liveDataEncomenda
    }

    fun buscaEncomendaPorId(encomendaId: String): LiveData<Encomenda> {
        val liveDataEncomendaId = MutableLiveData<Encomenda>()
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
                        val dataHoraApi = documento.getString("dataHoraApi").toString()
                        val encomendas = Encomenda(
                            firebaseId,
                            usuarioId,
                            codigoRastreio,
                            nomePacote,
                            status,
                            dataCriado,
                            dataAtualizado,
                            dataHoraApi
                        )
                        liveDataEncomendaId.value = encomendas

                    }

                }
            }
        return liveDataEncomendaId
    }


    fun atualizaStatus(encomendaId: String, status: String, dataHoraApi: String) {
        val data = Utils().dataHora()
        db.collection("Encomendas").document(encomendaId)
            .update("dataAtualizado", "Atualizado em: $data", "status", status, "dataHoraApi",dataHoraApi )
            .addOnCompleteListener {

            }
    }

    fun excluirEncomenda(firebaseId: String): Task<Void> {
        return db.collection("Encomendas").document(firebaseId)
            .delete().addOnCompleteListener {
            }
    }


//    suspend fun buscaWebCliente(codigo: String): ApiCorreios {
//        val user = "isaquecross15@gmail.com"
//        val token = "0a40c26417782427548f2aeb57f74c4038faf1f26ac662379425e35c848cce2b"
//        return webClienteApiCorreios.buscaRastreio(user, token, codigo)
//    }

    suspend fun buscaWebClientMelhorEnvio(codigo: String): ApiMelhorRastreio? {
        return webClientMelhorRastreio.buscaRastreio(codigo)
    }

}