package com.example.playlog

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllWatchedActivity : BaseActivity() {

    private val viewModel: AllWatchedViewModel by viewModels()
    private lateinit var seriesAdapter: SeriesGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_watched)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_grid)
        val backButton = findViewById<ImageView>(R.id.ic_backward)

        seriesAdapter = SeriesGridAdapter(emptyList()) { series ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EXTRA_SERIES_ID, series.id)
            startActivity(intent)
        }

        recyclerView.adapter = seriesAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        viewModel.allWatchedSeries.observe(this, Observer { seriesList ->
            seriesAdapter.updateData(seriesList)
        })

        backButton.setOnClickListener {
            finish()
        }
    }
}