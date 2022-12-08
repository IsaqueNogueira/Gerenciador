package com.example.gerenciadordeencomendas.webcliente.model

data class ApiCorreios (
    val codigo: String,
    val host: String,
    val eventos: List<Evento>,
    val time: Double,
    val quantidade: Long,
    val servico: String,
    val ultimo: String
)

data class Evento (
    val data: String,
    val hora: String,
    val local: String,
    val status: String,
    val subStatus: List<String>
)
