package com.example.playlog

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class DetailsActivity : BaseActivity() {

    companion object {
        const val EXTRA_SERIES_ID = "extra_series_id"
    }

    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val seriesId = intent.getIntExtra(EXTRA_SERIES_ID, -1)

        if (seriesId == -1) {
            finish()
            return
        }

        val factory = ViewModelFactory(application, seriesId)
        detailsViewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)

        val mediaImage = findViewById<ImageView>(R.id.media_image)
        val mediaTitle = findViewById<TextView>(R.id.media_title)
        val mediaDescription = findViewById<TextView>(R.id.media_description)

        detailsViewModel.seriesDetails.observe(this, Observer { serie ->
            serie?.let {
                mediaTitle.text = it.nome
                mediaDescription.text = it.descricao

                Glide.with(this)
                    .load(it.caminhoPoster)
                    .placeholder(R.drawable.placeholder_image)
                    .into(mediaImage)
            }
        })
    }
}