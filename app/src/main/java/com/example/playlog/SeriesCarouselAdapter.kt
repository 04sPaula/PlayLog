package com.example.playlog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Intent

class SeriesCarouselAdapter(private var series: List<SeriesEntity>) :
    RecyclerView.Adapter<SeriesCarouselAdapter.SerieViewHolder>() {

    class SerieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.media_title)
        val descricao: TextView = itemView.findViewById(R.id.media_description)
        val poster: ImageView = itemView.findViewById(R.id.media_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_serie, parent, false)
        return SerieViewHolder(view)
    }

    override fun onBindViewHolder(holder: SerieViewHolder, position: Int) {
        val serie = series[position]
        holder.titulo.text = serie.nome
        holder.descricao.text = serie.descricao
        Glide.with(holder.itemView.context).load(serie.caminhoPoster).into(holder.poster)

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