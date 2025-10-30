package com.example.desafio3dsm.modelo.cache

import com.example.desafio3dsm.modelo.ModeloPais
import com.example.desafio3dsm.utilidades.Constantes

object CachePaises {

    private val cachePorRegion = mutableMapOf<String, DatosCache>()

    data class DatosCache(
        val paises: List<ModeloPais>,
        val timestamp: Long
    )

    // Guardar en caché
    fun guardar(region: String, paises: List<ModeloPais>) {
        cachePorRegion[region] = DatosCache(
            paises = paises,
            timestamp = System.currentTimeMillis()
        )
    }

    // Obtener de caché
    fun obtener(region: String): List<ModeloPais>? {
        val cache = cachePorRegion[region] ?: return null

        // Verificar si el caché está vigente
        val tiempoTranscurrido = System.currentTimeMillis() - cache.timestamp
        return if (tiempoTranscurrido < Constantes.TIEMPO_CACHE_PAISES) {
            cache.paises
        } else {
            null
        }
    }

    // Limpiar caché
    fun limpiar() {
        cachePorRegion.clear()
    }

    // Verificar si existe caché válido
    fun existeCacheValido(region: String): Boolean {
        return obtener(region) != null
    }
}