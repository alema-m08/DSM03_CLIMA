package com.example.desafio3dsm.modelo.repository

import com.example.desafio3dsm.modelo.ModeloPais
import com.example.desafio3dsm.modelo.cache.CachePaises
import com.example.desafio3dsm.servicios.ClienteRetrofit
import com.example.desafio3dsm.utilidades.EstadoRecurso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class PaisesRepository {

    private val servicioPaises = ClienteRetrofit.servicioPaises

    // Obtener países por región con caché
    suspend fun obtenerPaisesPorRegion(region: String): EstadoRecurso<List<ModeloPais>> {
        return withContext(Dispatchers.IO) {
            try {
                // Primero intentar obtener del caché
                val paisesCache = CachePaises.obtener(region)
                if (paisesCache != null) {
                    return@withContext EstadoRecurso.Exito(paisesCache)
                }

                // Si no hay caché, hacer petición
                val respuesta = servicioPaises.obtenerPaisesPorRegion(region)

                if (respuesta.isSuccessful) {
                    val paises = respuesta.body() ?: emptyList()

                    // Guardar en caché
                    CachePaises.guardar(region, paises)

                    EstadoRecurso.Exito(paises)
                } else {
                    EstadoRecurso.Error("Error ${respuesta.code()}: ${respuesta.message()}")
                }
            } catch (e: HttpException) {
                EstadoRecurso.Error("Error de red: ${e.message()}")
            } catch (e: IOException) {
                EstadoRecurso.Error("Sin conexión a internet")
            } catch (e: Exception) {
                EstadoRecurso.Error("Error inesperado: ${e.message}")
            }
        }
    }

    // Buscar países por nombre
    suspend fun buscarPaises(query: String): EstadoRecurso<List<ModeloPais>> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = servicioPaises.buscarPaisesPorNombre(query)

                if (respuesta.isSuccessful) {
                    EstadoRecurso.Exito(respuesta.body() ?: emptyList())
                } else {
                    EstadoRecurso.Error("No se encontraron países")
                }
            } catch (e: Exception) {
                EstadoRecurso.Error("Error: ${e.message}")
            }
        }
    }
}