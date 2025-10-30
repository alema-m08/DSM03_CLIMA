package com.example.desafio3dsm.servicios

import com.example.desafio3dsm.modelo.RespuestaClima
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicioClima {

    @GET("current.json")
    suspend fun obtenerClimaActual(
        @Query("key") claveApi: String,
        @Query("q") ubicacion: String
    ): Response<RespuestaClima>
}