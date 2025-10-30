package com.example.desafio3dsm.controlador

import com.example.desafio3dsm.modelo.ModeloPais
import com.example.desafio3dsm.modelo.RespuestaClima
import com.example.desafio3dsm.servicios.ClienteRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControladorPaises {

    private val servicioPaises = ClienteRetrofit.servicioPaises
    private val servicioClima = ClienteRetrofit.servicioClima

    // Caché en memoria para países por región
    private val cachePaises = mutableMapOf<String, List<ModeloPais>>()

    // Obtener países por región (CON CACHÉ)
    fun obtenerPaisesPorRegion(
        region: String,
        alExito: (List<ModeloPais>) -> Unit,
        alError: (String) -> Unit,
        forzarRecarga: Boolean = false
    ) {
        // Si ya están en caché y no se fuerza recarga, devolver del caché
        if (!forzarRecarga && cachePaises.containsKey(region)) {
            alExito(cachePaises[region]!!)
            return
        }

        // Si no están en caché, hacer petición
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    servicioPaises.obtenerPaisesPorRegion(region)
                }

                if (respuesta.isSuccessful) {
                    respuesta.body()?.let { listaPaises ->
                        // Guardar en caché
                        cachePaises[region] = listaPaises
                        alExito(listaPaises)
                    } ?: alError("No se recibieron datos")
                } else {
                    alError("Error ${respuesta.code()}: ${respuesta.message()}")
                }
            } catch (e: Exception) {
                alError("Error de conexión: ${e.message}")
            }
        }
    }

    // Limpiar caché (útil para refrescar datos)
    fun limpiarCache() {
        cachePaises.clear()
    }

    // Limpiar caché de una región específica
    fun limpiarCacheRegion(region: String) {
        cachePaises.remove(region)
    }

    // Verificar si hay caché para una región
    fun tieneCacheRegion(region: String): Boolean {
        return cachePaises.containsKey(region)
    }

    // Obtener clima de capital
    fun obtenerClimaCapital(
        nombreCapital: String,
        alExito: (RespuestaClima) -> Unit,
        alError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = withContext(Dispatchers.IO) {
                    servicioClima.obtenerClimaActual(
                        ClienteRetrofit.CLAVE_API_CLIMA,
                        nombreCapital
                    )
                }

                if (respuesta.isSuccessful) {
                    respuesta.body()?.let { alExito(it) }
                        ?: alError("No se pudo obtener datos del clima")
                } else {
                    alError("Error: ${respuesta.message()}")
                }
            } catch (e: Exception) {
                alError("Error de conexión: ${e.message}")
            }
        }
    }
}