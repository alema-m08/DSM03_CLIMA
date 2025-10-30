package com.example.desafio3dsm.modelo

import com.google.gson.annotations.SerializedName

data class RespuestaClima(
    @SerializedName("location")
    val ubicacion: Ubicacion?,

    @SerializedName("current")
    val climaActual: ClimaActual?
)

data class Ubicacion(
    @SerializedName("name")
    val nombre: String?,

    @SerializedName("country")
    val pais: String?,

    @SerializedName("localtime")
    val horaLocal: String?
)

data class ClimaActual(
    @SerializedName("temp_c")
    val temperaturaCelsius: Double?,

    @SerializedName("temp_f")
    val temperaturaFahrenheit: Double?,

    @SerializedName("condition")
    val condicion: CondicionClima?,

    @SerializedName("wind_kph")
    val velocidadVientoKph: Double?,

    @SerializedName("wind_mph")
    val velocidadVientoMph: Double?,

    @SerializedName("humidity")
    val humedad: Int?,

    @SerializedName("feelslike_c")
    val sensacionTermicaC: Double?
)

data class CondicionClima(
    @SerializedName("text")
    val texto: String?,

    @SerializedName("icon")
    val icono: String?
)