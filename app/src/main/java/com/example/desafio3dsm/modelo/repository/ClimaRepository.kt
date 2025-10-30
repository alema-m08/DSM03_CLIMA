package com.example.desafio3dsm.modelo.repository

import com.example.desafio3dsm.modelo.RespuestaClima
import com.example.desafio3dsm.servicios.ClienteRetrofit
import com.example.desafio3dsm.utilidades.EstadoRecurso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ClimaRepository {

    private val servicioClima = ClienteRetrofit.servicioClima

    suspend fun obtenerClimaCapital(nombreCapital: String): EstadoRecurso<RespuestaClima> {
        return withContext(Dispatchers.IO) {
            try {
                val respuesta = servicioClima.obtenerClimaActual(
                    ClienteRetrofit.CLAVE_API_CLIMA,
                    nombreCapital
                )

                if (respuesta.isSuccessful) {
                    val clima = respuesta.body()
                    if (clima != null) {
                        EstadoRecurso.Exito(clima)
                    } else {
                        EstadoRecurso.Error("No se recibieron datos del clima")
                    }
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
}