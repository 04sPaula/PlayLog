package com.example.playlog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeriesSearchAdapter(val series: List<SeriesEntity>) :
    RecyclerView.Adapter<SeriesSearchAdapter.SerieViewHolder>() {

        class SerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titulo: TextView = itemView.findViewById(R.id.media_title)
            val descricao: TextView = itemView.findViewById(R.id.media_description)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.component_serie_search, parent, false)
            return SerieViewHolder(view)
        }

        override fun onBindViewHolder(holder: SerieViewHolder, position: Int) {
            val serie = series[position]
            holder.titulo.text = serie.nome
            holder.descricao.text = serie.descricao
        }

        override fun getItemCount(): Int {
            return series.size
        }
}