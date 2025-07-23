package com.example.playlog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var seriesAdapter: SeriesCarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewCarrossel = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_carousel)
        val btnAllWatched = findViewById<ImageView>(R.id.ic_all_watched)
        val lastWatchedView = findViewById<View>(R.id.component_last_watched)
        val lastWatchedImage = lastWatchedView.findViewById<ImageView>(R.id.media_image)
        val lastWatchedTitle = lastWatchedView.findViewById<TextView>(R.id.media_title)
        val lastWatchedDescription = lastWatchedView.findViewById<TextView>(R.id.media_description)
        val lastWatchedEpisodeInfo = lastWatchedView.findViewById<TextView>(R.id.media_episode_info)

        mainViewModel.lastWatchedSeries.observe(this, Observer { series ->
            if (series != null) {
                lastWatchedView.visibility = View.VISIBLE
                lastWatchedTitle.text = series.nome
                lastWatchedDescription.text = series.descricao

                if (series.temporadaAtual != null && series.episodioAtual != null) {
                    lastWatchedEpisodeInfo.text = "T${series.temporadaAtual} EP${series.episodioAtual}"
                    lastWatchedEpisodeInfo.visibility = View.VISIBLE
                } else {
                    lastWatchedEpisodeInfo.visibility = View.GONE
                }


                Glide.with(this)
                    .load(series.caminhoPoster)
                    .placeholder(R.drawable.placeholder_image)
                    .into(lastWatchedImage)

                lastWatchedView.setOnClickListener {
                    val intent = Intent(this, DetailsActivity::class.java).apply {
                        putExtra(DetailsActivity.EXTRA_SERIES_ID, series.id)
                    }
                    startActivity(intent)
                }
            } else {
                lastWatchedView.visibility = View.GONE
            }
        })

        seriesAdapter = SeriesCarouselAdapter(emptyList())
        recyclerViewCarrossel.adapter = seriesAdapter
        recyclerViewCarrossel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mainViewModel.allSeries.observe(this, Observer { series ->
            series?.let {
                seriesAdapter.updateData(it)
            }
        })

        btnAllWatched.setOnClickListener {
            val intent = Intent(this, AllWatchedActivity::class.java)
            startActivity(intent)
        }
    }
}