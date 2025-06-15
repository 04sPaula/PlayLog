package com.example.playlog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeriesCarouselAdapter(val series: List<Serie>) :
    RecyclerView.Adapter<SeriesCarouselAdapter.SerieViewHolder>() {

    class SerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.media_title)
        val descricao: TextView = itemView.findViewById(R.id.media_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_serie, parent, false)
        return SerieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SerieViewHolder, position: Int) {
        val serie = series[position]
        holder.titulo.text = serie.titulo
        holder.descricao.text = serie.descricao
    }

    override fun getItemCount(): Int {
        return series.size
    }
}