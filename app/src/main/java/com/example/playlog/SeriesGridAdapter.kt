package com.example.playlog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SeriesGridAdapter(
    private var seriesList: List<SeriesEntity>,
    private val onItemClicked: (SeriesEntity) -> Unit
) : RecyclerView.Adapter<SeriesGridAdapter.SeriesViewHolder>() {

    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mediaImage: ImageView = itemView.findViewById(R.id.media_image)
        val mediaTitle: TextView = itemView.findViewById(R.id.media_title)
        val mediaDescription: TextView = itemView.findViewById(R.id.media_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_serie, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val series = seriesList[position]

        holder.mediaTitle.text = series.nome
        holder.mediaDescription.text = series.descricao

        Glide.with(holder.itemView.context)
            .load(series.caminhoPoster)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.mediaImage)

        holder.itemView.setOnClickListener {
            onItemClicked(series)
        }
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    fun updateData(newSeriesList: List<SeriesEntity>) {
        this.seriesList = newSeriesList
        notifyDataSetChanged()
    }
}