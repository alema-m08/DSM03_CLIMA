package com.example.desafio3dsm.utilidades

sealed class EstadoRecurso<T> {
    class Cargando<T> : EstadoRecurso<T>()
    data class Exito<T>(val datos: T) : EstadoRecurso<T>()
    data class Error<T>(val mensaje: String) : EstadoRecurso<T>()
}