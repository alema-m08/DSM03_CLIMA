package com.example.desafio3dsm.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.desafio3dsm.R
import com.example.desafio3dsm.modelo.ModeloPais
import com.example.desafio3dsm.utilidades.PreferenciasApp

class AdaptadorPaises(
    private val listaPaises: List<ModeloPais>,
    private val preferencias: PreferenciasApp? = null,
    private val onPaisClick: (ModeloPais) -> Unit,
    private val onFavoritoClick: ((ModeloPais, Boolean) -> Unit)? = null
) : RecyclerView.Adapter<AdaptadorPaises.VistaHolder>() {

    class VistaHolder(vista: View) : RecyclerView.ViewHolder(vista) {
        val imagenBanderaPais: ImageView = vista.findViewById(R.id.imagenBanderaPais)
        val imagenBanderaFondo: ImageView = vista.findViewById(R.id.imagenBanderaFondo)
        val textoNombrePais: TextView = vista.findViewById(R.id.textoNombrePais)
        val textoCapital: TextView = vista.findViewById(R.id.textoCapitalPais)
        val iconoFavorito: ImageView? = try {
            vista.findViewById(R.id.iconoFavorito)
        } catch (e: Exception) {
            null
        }
    }

    override fun onCreateViewHolder(padre: ViewGroup, tipoVista: Int): VistaHolder {
        val vista = LayoutInflater.from(padre.context)
            .inflate(R.layout.item_pais, padre, false)
        return VistaHolder(vista)
    }

    override fun onBindViewHolder(holder: VistaHolder, posicion: Int) {
        val pais = listaPaises[posicion]

        holder.textoNombrePais.text = pais.nombre?.nombreComun ?: "País desconocido"
        holder.textoCapital.text = pais.capital?.firstOrNull() ?: "No disponible"

        // Configurar favorito
        holder.iconoFavorito?.let { iconoFav ->
            if (preferencias != null) {
                val esFavorito = preferencias.esFavorito(pais.codigoISO2 ?: "")
                iconoFav.setImageResource(
                    if (esFavorito) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                )
                iconoFav.visibility = View.VISIBLE

                iconoFav.setOnClickListener {
                    val nuevoEstado = !esFavorito
                    iconoFav.setImageResource(
                        if (nuevoEstado) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                    )
                    onFavoritoClick?.invoke(pais, nuevoEstado)
                }
            } else {
                iconoFav.visibility = View.GONE
            }
        }

        // Cargar bandera (solo si no está ya cargada - Glide cachea automáticamente)
        val urlBandera = pais.banderas?.png
        if (!urlBandera.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(urlBandera)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.color.gris_pizarra)
                .error(R.color.gris_pizarra)
                .into(holder.imagenBanderaPais)

            Glide.with(holder.itemView.context)
                .load(urlBandera)
                .into(holder.imagenBanderaFondo)
        }

        // Click en el item
        holder.itemView.setOnClickListener {
            onPaisClick.invoke(pais)
        }
    }

    override fun getItemCount(): Int = listaPaises.size

    // Importante: esto ayuda a RecyclerView a optimizar el reciclaje
    override fun getItemId(position: Int): Long {
        return listaPaises[position].codigoISO2?.hashCode()?.toLong() ?: position.toLong()
    }
}