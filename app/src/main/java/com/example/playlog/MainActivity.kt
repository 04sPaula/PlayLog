package com.example.playlog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var seriesAdapter: SeriesCarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewCarrossel = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_carousel)
        val btnAllWatched = findViewById<ImageView>(R.id.ic_all_watched)
        val btnToWatch = findViewById<ImageView>(R.id.ic_to_watch)
        val lastWatchedCard = findViewById<ImageView>(R.id.media_image)


        seriesAdapter = SeriesCarouselAdapter(emptyList())
        recyclerViewCarrossel.adapter = seriesAdapter
        recyclerViewCarrossel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mainViewModel.allSeries.observe(this, Observer { series ->
            series?.let {
                seriesAdapter.updateData(it)
            }
        })

        mainViewModel.insertTestData()


        btnAllWatched.setOnClickListener {
            val intent = Intent(this, AllWatchedActivity::class.java)
            startActivity(intent)
        }
        btnToWatch.setOnClickListener {
            val intent = Intent(this, ToWatchActivity::class.java)
            startActivity(intent)
        }
        lastWatchedCard.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }
    }
}