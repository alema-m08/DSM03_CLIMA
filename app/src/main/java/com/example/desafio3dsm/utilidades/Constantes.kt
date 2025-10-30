package com.example.desafio3dsm.utilidades

object Constantes {
    // URLs Base
    const val URL_BASE_PAISES = "https://restcountries.com/"
    const val URL_BASE_CLIMA = "https://api.weatherapi.com/v1/"

    // Regiones
    val REGIONES = arrayOf(
        "Africa",
        "Americas",
        "Asia",
        "Europe",
        "Oceania"
    )

    // Emojis por región
    val EMOJIS_REGIONES = mapOf(
        "Africa" to "🌍",
        "Americas" to "🌎",
        "Asia" to "🌏",
        "Europe" to "🇪🇺",
        "Oceania" to "🏝️"
    )

    // Tiempos de caché
    const val TIEMPO_CACHE_PAISES = 3600000L // 1 hora
    const val TIEMPO_CACHE_CLIMA = 300000L // 5 minutos

    // SharedPreferences
    const val PREFS_NAME = "DesafioPrefs"
    const val KEY_FAVORITOS = "favoritos"
    const val KEY_ULTIMA_REGION = "ultima_region"
}