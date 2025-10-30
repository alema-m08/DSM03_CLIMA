package com.example.desafio3dsm.controlador

import com.example.desafio3dsm.modelo.RespuestaClima
import com.example.desafio3dsm.modelo.repository.ClimaRepository
import com.example.desafio3dsm.utilidades.EstadoRecurso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ControladorClima {

    private val repository = ClimaRepository()

    fun obtenerClimaCapital(
        nombreCapital: String,
        alExito: (RespuestaClima) -> Unit,
        alError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            when (val resultado = repository.obtenerClimaCapital(nombreCapital)) {
                is EstadoRecurso.Exito -> alExito(resultado.datos)
                is EstadoRecurso.Error -> alError(resultado.mensaje)
                is EstadoRecurso.Cargando -> {} // Manejado por la UI
            }
        }
    }
}