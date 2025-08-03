package com.example.playlog

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class DetailsActivity : BaseActivity() {

    companion object {
        const val EXTRA_SERIES_ID = "extra_series_id"
    }

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mediaEpisodeInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val buttonUpdate = findViewById<MaterialButton>(R.id.button_update)
        val buttonDelete = findViewById<MaterialButton>(R.id.button_delete)
        mediaEpisodeInfo = findViewById(R.id.media_episode_info)

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
            if (serie != null) {
                mediaTitle.text = serie.nome
                mediaDescription.text = serie.descricao

                Glide.with(this)
                    .load(serie.caminhoPoster)
                    .placeholder(R.drawable.placeholder_image)
                    .into(mediaImage)

                if (serie.temporadaAtual != null && serie.episodioAtual != null) {
                    mediaEpisodeInfo.text = "T${serie.temporadaAtual} EP${serie.episodioAtual}"
                    mediaEpisodeInfo.visibility = android.view.View.VISIBLE
                } else {
                    mediaEpisodeInfo.visibility = android.view.View.GONE
                    buttonDelete.visibility = android.view.View.GONE
                }

            } else {
                Toast.makeText(this, "Série não encontrada ou removida.", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        buttonUpdate.setOnClickListener {
            showUpdateEpisodeDialog()
        }

        buttonDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showUpdateEpisodeDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_episode, null)
        val seasonInput = dialogView.findViewById<EditText>(R.id.edit_text_season)
        val episodeInput = dialogView.findViewById<EditText>(R.id.edit_text_episode)
        val cancelButton = dialogView.findViewById<android.widget.Button>(R.id.button_cancel)
        val confirmButton = dialogView.findViewById<android.widget.Button>(R.id.button_confirm)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        confirmButton.setOnClickListener {
            val seasonStr = seasonInput.text.toString()
            val episodeStr = episodeInput.text.toString()

            if (seasonStr.isNotEmpty() && episodeStr.isNotEmpty()) {
                val season = seasonStr.toInt()
                val episode = episodeStr.toInt()
                detailsViewModel.updateEpisodeProgress(season, episode)
                Toast.makeText(this, "Progresso atualizado!", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Por favor, preencha ambos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }


    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Descartar Progresso")
            .setMessage("Você tem certeza que quer remover esta série da sua lista?")
            .setPositiveButton("Sim") { _, _ ->
                detailsViewModel.deleteSeries()
            }
            .setNegativeButton("Não", null)
            .show()
    }
}