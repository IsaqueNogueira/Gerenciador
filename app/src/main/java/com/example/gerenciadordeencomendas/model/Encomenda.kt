package com.example.gerenciadordeencomendas.model

import com.google.firebase.firestore.Exclude

data class Encomenda(
    @get:Exclude val firebaseId : String,
    val usuarioId: String,
    val codigoRastreio: String,
    val nomePacote: String,
    val status: String,
    val dataCriado: Long,
    val dataAtualizado: String
)