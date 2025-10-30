package com.example.desafio3dsm.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio3dsm.R
import com.example.desafio3dsm.utilidades.Constantes
import com.example.desafio3dsm.utilidades.PreferenciasApp

class ActividadPrincipal : AppCompatActivity() {

    private lateinit var listaRegiones: ListView
    private lateinit var preferencias: PreferenciasApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_principal)

        preferencias = PreferenciasApp(this)

        configurarVista()
        configurarListaRegiones()
        aplicarAnimaciones()
    }

    private fun configurarVista() {
        supportActionBar?.hide()
        listaRegiones = findViewById(R.id.listaRegiones)
    }

    private fun configurarListaRegiones() {
        // Crear array con regiones y emojis
        val regionesConEmojis = Constantes.REGIONES.map { region ->
            "${Constantes.EMOJIS_REGIONES[region]} $region"
        }.toTypedArray()

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            regionesConEmojis
        )

        listaRegiones.adapter = adaptador

        listaRegiones.onItemClickListener = AdapterView.OnItemClickListener {
                _, vista, posicion, _ ->
            // Animar el item clickeado
            val animacion = AnimationUtils.loadAnimation(this, R.anim.scale_in)
            vista.startAnimation(animacion)

            // Obtener región sin emoji
            val regionSeleccionada = Constantes.REGIONES[posicion]

            // Guardar última región visitada
            preferencias.guardarUltimaRegion(regionSeleccionada)

            // Pequeño delay para que se vea la animación
            vista.postDelayed({
                abrirPaisesPorRegion(regionSeleccionada)
            }, 200)
        }
    }

    private fun aplicarAnimaciones() {
        // Animar header
        val headerCard = findViewById<View>(R.id.headerCard)
        val animacionFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        headerCard.startAnimation(animacionFadeIn)

        // Animar lista con delay
        headerCard.postDelayed({
            val animacionSlideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom)
            listaRegiones.parent.let { parent ->
                (parent as View).startAnimation(animacionSlideIn)
            }
        }, 300)
    }

    private fun abrirPaisesPorRegion(nombreRegion: String) {
        val intencion = Intent(this, ActividadPaisesPorRegion::class.java)
        intencion.putExtra("NOMBRE_REGION", nombreRegion)
        startActivity(intencion)
        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_in)
    }
}