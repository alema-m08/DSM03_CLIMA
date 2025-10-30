package com.example.desafio3dsm.modelo

import com.google.gson.annotations.SerializedName

data class ModeloPais(
    @SerializedName("name")
    val nombre: NombrePais?,

    @SerializedName("capital")
    val capital: List<String>?,

    @SerializedName("region")
    val region: String?,

    @SerializedName("subregion")
    val subregion: String?,

    @SerializedName("population")
    val poblacion: Long?,

    @SerializedName("flags")
    val banderas: Banderas?,

    @SerializedName("cca2")
    val codigoISO2: String?,

    @SerializedName("cca3")
    val codigoISO3: String?,

    @SerializedName("currencies")
    val monedas: Map<String, Moneda>?,

    @SerializedName("languages")
    val idiomas: Map<String, String>?,

    @SerializedName("latlng")
    val coordenadas: List<Double>?
)

data class NombrePais(
    @SerializedName("common")
    val nombreComun: String?,

    @SerializedName("official")
    val nombreOficial: String?
)

data class Banderas(
    @SerializedName("png")
    val png: String?,

    @SerializedName("svg")
    val svg: String?
)

data class Moneda(
    @SerializedName("name")
    val nombre: String?,

    @SerializedName("symbol")
    val simbolo: String?
)