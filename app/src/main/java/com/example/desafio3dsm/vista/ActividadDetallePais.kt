package com.example.desafio3dsm.vista

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.desafio3dsm.R
import com.example.desafio3dsm.controlador.ControladorPaises
import com.example.desafio3dsm.modelo.RespuestaClima
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.NumberFormat
import java.util.Locale

class ActividadDetallePais : AppCompatActivity() {

    // Vistas de información del país
    private lateinit var imagenBandera: ImageView
    private lateinit var textoNombrePais: TextView
    private lateinit var textoNombreOficial: TextView
    private lateinit var textoCapital: TextView
    private lateinit var textoRegion: TextView
    private lateinit var textoSubregion: TextView
    private lateinit var textoPoblacion: TextView
    private lateinit var textoCodigosISO: TextView
    private lateinit var textoMonedas: TextView
    private lateinit var textoIdiomas: TextView
    private lateinit var textoCoordenadas: TextView

    // Vistas del mapa
    private lateinit var mapaCapital: MapView
    private lateinit var barraProgresoMapa: View
    private lateinit var textoCapitalMapa: TextView
    private lateinit var textoCoordenadasMapa: TextView

    // Vistas de información del clima
    private lateinit var tarjetaClima: CardView
    private lateinit var barraProgresoClima: View
    private lateinit var textoMensajeClima: CardView
    private lateinit var imagenIconoClima: ImageView
    private lateinit var textoTemperatura: TextView
    private lateinit var textoCondicion: TextView
    private lateinit var textoSensacionTermica: TextView
    private lateinit var textoViento: TextView
    private lateinit var textoHumedad: TextView
    private lateinit var textoUbicacion: TextView

    private lateinit var controlador: ControladorPaises

    // Coordenadas de la capital
    private var latitudCapital: Double = 0.0
    private var longitudCapital: Double = 0.0
    private var nombreCapital: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar OSMDroid
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.actividad_detalle_pais)

        configurarVista()
        cargarDatosPais()
        cargarDatosClima()
        inicializarMapa()
    }

    private fun configurarVista() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar vistas del país
        imagenBandera = findViewById(R.id.imagenBandera)
        textoNombrePais = findViewById(R.id.textoNombrePais)
        textoNombreOficial = findViewById(R.id.textoNombreOficial)
        textoCapital = findViewById(R.id.textoCapital)
        textoRegion = findViewById(R.id.textoRegion)
        textoSubregion = findViewById(R.id.textoSubregion)
        textoPoblacion = findViewById(R.id.textoPoblacion)
        textoCodigosISO = findViewById(R.id.textoCodigosISO)
        textoMonedas = findViewById(R.id.textoMonedas)
        textoIdiomas = findViewById(R.id.textoIdiomas)
        textoCoordenadas = findViewById(R.id.textoCoordenadas)

        // Inicializar vistas del mapa
        mapaCapital = findViewById(R.id.mapaCapital)
        barraProgresoMapa = findViewById(R.id.barraProgresoMapa)
        textoCapitalMapa = findViewById(R.id.textoCapitalMapa)
        textoCoordenadasMapa = findViewById(R.id.textoCoordenadasMapa)

        // Inicializar vistas del clima
        tarjetaClima = findViewById(R.id.tarjetaClima)
        barraProgresoClima = findViewById(R.id.barraProgresoClima)
        textoMensajeClima = findViewById(R.id.textoMensajeClima)
        imagenIconoClima = findViewById(R.id.imagenIconoClima)
        textoTemperatura = findViewById(R.id.textoTemperatura)
        textoCondicion = findViewById(R.id.textoCondicion)
        textoSensacionTermica = findViewById(R.id.textoSensacionTermica)
        textoViento = findViewById(R.id.textoViento)
        textoHumedad = findViewById(R.id.textoHumedad)
        textoUbicacion = findViewById(R.id.textoUbicacion)

        controlador = ControladorPaises()
    }

    private fun inicializarMapa() {
        // Configurar el mapa
        mapaCapital.setTileSource(TileSourceFactory.MAPNIK)
        mapaCapital.setMultiTouchControls(true)
        mapaCapital.controller.setZoom(2.0)

        // Posición inicial: centro del mundo
        val puntoInicial = GeoPoint(20.0, 0.0)
        mapaCapital.controller.setCenter(puntoInicial)
    }

    private fun animarZoomACapital() {
        if (latitudCapital == 0.0 && longitudCapital == 0.0) {
            barraProgresoMapa.visibility = View.GONE
            textoCapitalMapa.text = "📍 Coordenadas no disponibles"
            textoCoordenadasMapa.text = "No se puede mostrar en el mapa"
            return
        }

        // Actualizar textos
        textoCapitalMapa.text = "📍 Ubicando $nombreCapital..."
        textoCoordenadasMapa.text = "Lat: ${String.format("%.4f", latitudCapital)}, Lon: ${String.format("%.4f", longitudCapital)}"

        val puntoCapital = GeoPoint(latitudCapital, longitudCapital)

        // Animación de zoom progresivo
        Handler(Looper.getMainLooper()).postDelayed({
            mapaCapital.controller.animateTo(puntoCapital, 5.0, 800L)

            Handler(Looper.getMainLooper()).postDelayed({
                mapaCapital.controller.animateTo(puntoCapital, 10.0, 800L)

                Handler(Looper.getMainLooper()).postDelayed({
                    mapaCapital.controller.animateTo(puntoCapital, 15.0, 800L)
                    agregarMarcador(puntoCapital)
                    barraProgresoMapa.visibility = View.GONE
                    textoCapitalMapa.text = "📍 $nombreCapital"
                }, 800)
            }, 800)
        }, 500)
    }

    private fun agregarMarcador(punto: GeoPoint) {
        val marcador = Marker(mapaCapital)
        marcador.position = punto
        marcador.title = nombreCapital
        marcador.snippet = "Capital del país"
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        mapaCapital.overlays.add(marcador)
        mapaCapital.invalidate()

        // Mostrar el popup del marcador
        Handler(Looper.getMainLooper()).postDelayed({
            marcador.showInfoWindow()
        }, 300)
    }

    private fun cargarDatosPais() {
        val nombrePais = intent.getStringExtra("NOMBRE_PAIS") ?: "País"
        val nombreOficial = intent.getStringExtra("NOMBRE_OFICIAL") ?: "No disponible"
        val capital = intent.getStringExtra("CAPITAL") ?: "No disponible"
        val region = intent.getStringExtra("REGION") ?: "No disponible"
        val subregion = intent.getStringExtra("SUBREGION") ?: "No disponible"
        val poblacion = intent.getLongExtra("POBLACION", 0)
        val urlBandera = intent.getStringExtra("URL_BANDERA")
        val codigoISO2 = intent.getStringExtra("CODIGO_ISO2") ?: ""
        val codigoISO3 = intent.getStringExtra("CODIGO_ISO3") ?: ""
        val monedas = intent.getStringExtra("MONEDAS") ?: "No disponible"
        val idiomas = intent.getStringExtra("IDIOMAS") ?: "No disponible"
        latitudCapital = intent.getDoubleExtra("LATITUD", 0.0)
        longitudCapital = intent.getDoubleExtra("LONGITUD", 0.0)
        nombreCapital = capital

        // Configurar título
        supportActionBar?.title = nombrePais

        // Mostrar datos
        textoNombrePais.text = nombrePais
        textoNombreOficial.text = "Nombre oficial: $nombreOficial"
        textoCapital.text = "Capital: $capital"
        textoRegion.text = "Región: $region"
        textoSubregion.text = "Subregión: $subregion"
        textoPoblacion.text = "Población: ${formatearNumero(poblacion)}"
        textoCodigosISO.text = "Códigos ISO: $codigoISO2 / $codigoISO3"
        textoMonedas.text = "Monedas: $monedas"
        textoIdiomas.text = "Idiomas: $idiomas"

        if (latitudCapital != 0.0 && longitudCapital != 0.0) {
            textoCoordenadas.text = "Coordenadas: ${String.format("%.2f", latitudCapital)}, ${String.format("%.2f", longitudCapital)}"
            animarZoomACapital()
        } else {
            textoCoordenadas.text = "Coordenadas: No disponibles"
            barraProgresoMapa.visibility = View.GONE
        }

        // Cargar bandera
        if (!urlBandera.isNullOrEmpty()) {
            Glide.with(this)
                .load(urlBandera)
                .placeholder(R.color.gris_pizarra)
                .error(R.color.gris_pizarra)
                .into(imagenBandera)
        }
    }

    private fun cargarDatosClima() {
        val capital = intent.getStringExtra("CAPITAL")

        if (capital.isNullOrEmpty()) {
            mostrarErrorClima("No se pudo obtener el nombre de la capital")
            return
        }

        mostrarCargandoClima(true)

        controlador.obtenerClimaCapital(
            capital,
            alExito = { datosClima ->
                mostrarCargandoClima(false)
                mostrarDatosClima(datosClima)
            },
            alError = { mensajeError ->
                mostrarCargandoClima(false)
                mostrarErrorClima(mensajeError)
            }
        )
    }

    private fun mostrarDatosClima(clima: RespuestaClima) {
        tarjetaClima.visibility = View.VISIBLE
        textoMensajeClima.visibility = View.GONE

        val climaActual = clima.climaActual
        val ubicacion = clima.ubicacion

        textoUbicacion.text = "${ubicacion?.nombre}, ${ubicacion?.pais}"
        textoTemperatura.text = "${climaActual?.temperaturaCelsius}°C / ${climaActual?.temperaturaFahrenheit}°F"
        textoCondicion.text = climaActual?.condicion?.texto ?: "N/A"
        textoSensacionTermica.text = "Sensación térmica: ${climaActual?.sensacionTermicaC}°C"
        textoViento.text = "Viento: ${climaActual?.velocidadVientoKph} kph / ${climaActual?.velocidadVientoMph} mph"
        textoHumedad.text = "Humedad: ${climaActual?.humedad}%"

        // Cargar icono del clima
        val urlIcono = climaActual?.condicion?.icono
        if (!urlIcono.isNullOrEmpty()) {
            val urlCompleta = "https:$urlIcono"
            Glide.with(this)
                .load(urlCompleta)
                .into(imagenIconoClima)
        }
    }

    private fun mostrarErrorClima(mensaje: String) {
        tarjetaClima.visibility = View.GONE
        textoMensajeClima.visibility = View.VISIBLE
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun mostrarCargandoClima(mostrar: Boolean) {
        barraProgresoClima.visibility = if (mostrar) View.VISIBLE else View.GONE
        if (mostrar) {
            tarjetaClima.visibility = View.GONE
            textoMensajeClima.visibility = View.GONE
        }
    }

    private fun formatearNumero(numero: Long): String {
        return NumberFormat.getNumberInstance(Locale.US).format(numero)
    }

    override fun onResume() {
        super.onResume()
        mapaCapital.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapaCapital.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}