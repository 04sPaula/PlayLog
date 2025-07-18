package com.example.playlog

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SeriesSearchAdapter(private var series: List<SeriesEntity>) :
    RecyclerView.Adapter<SeriesSearchAdapter.SerieViewHolder>() {

    class SerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.media_title)
        val descricao: TextView = itemView.findViewById(R.id.media_description)
        val poster: ImageView = itemView.findViewById(R.id.media_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_serie_search, parent, false)
        return SerieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SerieViewHolder, position: Int) {
        val serie = series[position]
        Log.d("SearchDebug", "onBindViewHolder na posição $position para a série: ${serie.nome}")
        holder.titulo.text = serie.nome
        holder.descricao.text = serie.descricao

        Glide.with(holder.itemView.context)
            .load(serie.caminhoPoster)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra(DetailsActivity.EXTRA_SERIES_ID, serie.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return series.size
    }

    fun updateData(newSeries: List<SeriesEntity>) {
        this.series = newSeries
        notifyDataSetChanged()
    }
}