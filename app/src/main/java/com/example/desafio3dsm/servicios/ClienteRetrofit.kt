package com.example.desafio3dsm.servicios

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteRetrofit {

    private const val URL_BASE_PAISES = "https://restcountries.com/"
    private const val URL_BASE_CLIMA = "http://api.weatherapi.com/v1/"

    const val CLAVE_API_CLIMA = "0ce857fa36b84968ada21613252910"

    private val retrofitPaises: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_PAISES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitClima: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_CLIMA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val servicioPaises: ServicioPaises by lazy {
        retrofitPaises.create(ServicioPaises::class.java)
    }

    val servicioClima: ServicioClima by lazy {
        retrofitClima.create(ServicioClima::class.java)
    }
}