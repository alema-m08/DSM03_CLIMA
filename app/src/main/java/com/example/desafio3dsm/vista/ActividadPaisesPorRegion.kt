package com.example.desafio3dsm.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio3dsm.R
import com.example.desafio3dsm.adaptadores.AdaptadorPaises
import com.example.desafio3dsm.controlador.ControladorPaises
import com.example.desafio3dsm.modelo.ModeloPais

class ActividadPaisesPorRegion : AppCompatActivity() {

    private lateinit var recyclerViewPaises: RecyclerView
    private lateinit var barraProgreso: View
    private lateinit var textoMensaje: TextView
    private lateinit var controlador: ControladorPaises
    private var nombreRegion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_paises_region)

        configurarVista()
        cargarPaisesPorRegion()
    }

    private fun configurarVista() {
        nombreRegion = intent.getStringExtra("NOMBRE_REGION") ?: "Región"
        supportActionBar?.title = "Países de $nombreRegion"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewPaises = findViewById(R.id.recyclerViewPaises)
        barraProgreso = findViewById(R.id.barraProgreso)
        textoMensaje = findViewById(R.id.textoMensaje)

        recyclerViewPaises.layoutManager = LinearLayoutManager(this)

        // Importante: usar setHasFixedSize para mejor rendimiento
        recyclerViewPaises.setHasFixedSize(true)

        // Deshabilitar el nested scrolling para mejor rendimiento
        recyclerViewPaises.isNestedScrollingEnabled = false

        controlador = ControladorPaises()
    }

    private fun cargarPaisesPorRegion(forzarRecarga: Boolean = false) {
        // Solo mostrar loading si no hay caché
        if (!controlador.tieneCacheRegion(nombreRegion)) {
            mostrarCargando(true)
        } else if (forzarRecarga) {
            Toast.makeText(this, "Actualizando...", Toast.LENGTH_SHORT).show()
        }

        controlador.obtenerPaisesPorRegion(
            nombreRegion,
            alExito = { listaPaises ->
                mostrarCargando(false)
                if (listaPaises.isNotEmpty()) {
                    mostrarPaises(listaPaises)

                    // Mensaje informativo solo si es primera carga
                    if (!forzarRecarga) {
                        Toast.makeText(
                            this,
                            "${listaPaises.size} países cargados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    mostrarMensaje("No se encontraron países en esta región")
                }
            },
            alError = { mensajeError ->
                mostrarCargando(false)
                mostrarMensaje("Error: $mensajeError")
            },
            forzarRecarga = forzarRecarga
        )
    }

    private fun mostrarPaises(listaPaises: List<ModeloPais>) {
        recyclerViewPaises.visibility = View.VISIBLE
        textoMensaje.visibility = View.GONE

        val adaptador = AdaptadorPaises(
            listaPaises = listaPaises,
            onPaisClick = { paisSeleccionado ->
                abrirDetallePais(paisSeleccionado)
            }
        )

        recyclerViewPaises.adapter = adaptador
    }

    private fun mostrarCargando(mostrar: Boolean) {
        barraProgreso.visibility = if (mostrar) View.VISIBLE else View.GONE
        recyclerViewPaises.visibility = if (mostrar) View.GONE else View.VISIBLE
        textoMensaje.visibility = View.GONE
    }

    private fun mostrarMensaje(mensaje: String) {
        textoMensaje.visibility = View.VISIBLE
        textoMensaje.text = mensaje
        recyclerViewPaises.visibility = View.GONE
        barraProgreso.visibility = View.GONE
    }

    private fun abrirDetallePais(pais: ModeloPais) {
        val intencion = Intent(this, ActividadDetallePais::class.java)

        // Información básica
        intencion.putExtra("NOMBRE_PAIS", pais.nombre?.nombreComun ?: "")
        intencion.putExtra("NOMBRE_OFICIAL", pais.nombre?.nombreOficial ?: "")
        intencion.putExtra("CAPITAL", pais.capital?.firstOrNull() ?: "")
        intencion.putExtra("REGION", pais.region ?: "")
        intencion.putExtra("SUBREGION", pais.subregion ?: "")
        intencion.putExtra("POBLACION", pais.poblacion ?: 0L)
        intencion.putExtra("URL_BANDERA", pais.banderas?.png ?: "")
        intencion.putExtra("CODIGO_ISO2", pais.codigoISO2 ?: "")
        intencion.putExtra("CODIGO_ISO3", pais.codigoISO3 ?: "")

        // Monedas
        val monedas = pais.monedas?.values?.joinToString(", ") {
            "${it.nombre} (${it.simbolo})"
        } ?: "No disponible"
        intencion.putExtra("MONEDAS", monedas)

        // Idiomas
        val idiomas = pais.idiomas?.values?.joinToString(", ") ?: "No disponible"
        intencion.putExtra("IDIOMAS", idiomas)

        // Coordenadas
        val coordenadas = pais.coordenadas
        if (coordenadas != null && coordenadas.size >= 2) {
            intencion.putExtra("LATITUD", coordenadas[0])
            intencion.putExtra("LONGITUD", coordenadas[1])
        } else {
            intencion.putExtra("LATITUD", 0.0)
            intencion.putExtra("LONGITUD", 0.0)
        }

        startActivity(intencion)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}