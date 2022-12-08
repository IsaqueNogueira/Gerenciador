package com.example.gerenciadordeencomendas.webcliente.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMelhorRastreio (
    val success: Boolean,
    val data: Data
)

@Serializable
data class Data (
    val company: Company,

    @SerialName("tracking_code")
    val trackingCode: String,

    val status: String,
    val events: List<Event>
)

@Serializable
data class Company (
    val id: Long,

    @SerialName("sk_company")
    val skCompany: String,

    val name: String,

    @SerialName("is_active")
    val isActive: Long,

    @SerialName("jobs_frequency")
    val jobsFrequency: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class Event (
    val events: String,
    val tag: String,
    val local: String,
    val date: String,
    val city: String? = null,
    val uf: String? = null,

    @SerialName("destination_local")
    val destination_local: String? = null,

    @SerialName("destination_city")
    val destination_city: String? = null,

    @SerialName("destination_uf")
    val destination_uf: String? = null,

    val comment: String? = null
)
