package com.example.desafio3dsm.servicios

import com.example.desafio3dsm.modelo.ModeloPais
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ServicioPaises {

    @GET("v3.1/region/{region}")
    suspend fun obtenerPaisesPorRegion(
        @Path("region") region: String
    ): Response<List<ModeloPais>>

    @GET("v3.1/name/{nombre}")
    suspend fun buscarPaisesPorNombre(
        @Path("nombre") nombre: String
    ): Response<List<ModeloPais>>

    @GET("v3.1/all")
    suspend fun obtenerTodosPaises(): Response<List<ModeloPais>>
}