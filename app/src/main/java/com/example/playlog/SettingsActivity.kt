package com.example.playlog

import com.google.android.material.button.MaterialButton
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.gson.Gson
import java.io.FileWriter

class SettingsActivity : BaseActivity() {
    private lateinit var seriesDao: SeriesDao

    private lateinit var buttonBackup: MaterialButton
    private lateinit var buttonDeleteAll: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttonBackup = findViewById(R.id.button_delete)
        buttonDeleteAll = findViewById(R.id.button_update)

        seriesDao = AppDatabase.getDatabase(applicationContext).seriesDao()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        buttonBackup.setOnClickListener { handleBackup() }
        buttonDeleteAll.setOnClickListener { showDeleteConfirmationDialog() }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnConfirm = dialogView.findViewById<Button>(R.id.button_confirm_delete)
        val btnCancel = dialogView.findViewById<Button>(R.id.button_cancel_delete)

        btnConfirm.setOnClickListener {
            deleteAllData()
            alertDialog.dismiss()
        }
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun handleBackup() {
        lifecycleScope.launch {
            try {
                val seriesList = withContext(Dispatchers.IO) {
                    seriesDao.getAllSeriesSync()
                }

                if (seriesList.isEmpty()) {
                    Toast.makeText(this@SettingsActivity, "Nenhum dado para fazer backup.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val file = saveJsonToFile(seriesList)
                Toast.makeText(this@SettingsActivity, "Backup salvo em: ${file.absolutePath}", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Falha ao criar o backup: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private suspend fun saveJsonToFile(seriesList: List<Any>): File {
        return withContext(Dispatchers.IO) {
            val gson = Gson()
            val jsonString = gson.toJson(seriesList)

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "playlog_backup_$timestamp.json"

            val directory = getExternalFilesDir(null)
            val file = File(directory, fileName)

            FileWriter(file).use { it.write(jsonString) }
            file
        }
    }

    private fun deleteAllData() {
        lifecycleScope.launch(Dispatchers.IO) {
            seriesDao.deleteAllSeries()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SettingsActivity, "Todos os dados foram apagados.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}