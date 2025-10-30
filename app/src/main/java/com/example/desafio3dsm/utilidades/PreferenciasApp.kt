package com.example.desafio3dsm.utilidades

import android.content.Context
import android.content.SharedPreferences

class PreferenciasApp(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constantes.PREFS_NAME,
        Context.MODE_PRIVATE
    )

    // Guardar favoritos
    fun guardarFavorito(codigoPais: String) {
        val favoritos = obtenerFavoritos().toMutableSet()
        favoritos.add(codigoPais)
        prefs.edit().putStringSet(Constantes.KEY_FAVORITOS, favoritos).apply()
    }

    // Eliminar favorito
    fun eliminarFavorito(codigoPais: String) {
        val favoritos = obtenerFavoritos().toMutableSet()
        favoritos.remove(codigoPais)
        prefs.edit().putStringSet(Constantes.KEY_FAVORITOS, favoritos).apply()
    }

    // Obtener favoritos
    fun obtenerFavoritos(): Set<String> {
        return prefs.getStringSet(Constantes.KEY_FAVORITOS, emptySet()) ?: emptySet()
    }

    // Verificar si es favorito
    fun esFavorito(codigoPais: String): Boolean {
        return obtenerFavoritos().contains(codigoPais)
    }

    // Guardar última región visitada
    fun guardarUltimaRegion(region: String) {
        prefs.edit().putString(Constantes.KEY_ULTIMA_REGION, region).apply()
    }

    // Obtener última región visitada
    fun obtenerUltimaRegion(): String? {
        return prefs.getString(Constantes.KEY_ULTIMA_REGION, null)
    }
}