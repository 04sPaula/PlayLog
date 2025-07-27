package com.example.playlog

import android.net.Uri
import com.google.android.material.button.MaterialButton
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class SettingsActivity : BaseActivity() {
    private lateinit var seriesDao: SeriesDao

    private lateinit var buttonBackup: MaterialButton
    private lateinit var buttonDeleteAll: MaterialButton
    private lateinit var buttonRestore: MaterialButton

    private lateinit var restoreLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttonBackup = findViewById(R.id.button_save)
        buttonDeleteAll = findViewById(R.id.button_reset)
        buttonRestore = findViewById(R.id.button_backup)

        seriesDao = AppDatabase.getDatabase(applicationContext).seriesDao()

        restoreLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                showRestoreConfirmationDialog(it)
            }
        }
        setupClickListeners()
    }

    private fun setupClickListeners() {
        buttonBackup.setOnClickListener { handleBackup() }
        buttonDeleteAll.setOnClickListener { showDeleteConfirmationDialog() }
        buttonRestore.setOnClickListener { handleRestoreClick() }
    }

    private fun handleRestoreClick() {
        restoreLauncher.launch(arrayOf("application/json"))
    }

    private fun showRestoreConfirmationDialog(fileUri: Uri) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_restore, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnConfirm = dialogView.findViewById<Button>(R.id.button_confirm_restore)
        val btnCancel = dialogView.findViewById<Button>(R.id.button_cancel_restore)

        btnConfirm.setOnClickListener {
            importDataFromJson(fileUri)
            alertDialog.dismiss()
        }
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun importDataFromJson(fileUri: Uri) {
        lifecycleScope.launch {
            try {
                val seriesList = readJsonFromUri(fileUri)
                if (seriesList.isNotEmpty()) {
                    seriesDao.insertAll(seriesList)
                    Toast.makeText(this@SettingsActivity, "Dados restaurados com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SettingsActivity, "Arquivo de backup vazio ou inválido.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Falha ao restaurar o backup: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private suspend fun readJsonFromUri(uri: Uri): List<SeriesEntity> {
        return withContext(Dispatchers.IO) {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val gson = Gson()
                    val seriesType = object : TypeToken<List<SeriesEntity>>() {}.type
                    gson.fromJson(reader, seriesType)
                }
            } ?: emptyList()
        }
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

                val fileUri = saveJsonToDownloads(seriesList)
                if (fileUri != null) {
                    Toast.makeText(this@SettingsActivity, "Backup salvo na pasta Downloads!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@SettingsActivity, "Não foi possível salvar o backup.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, "Falha ao criar o backup: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private suspend fun saveJsonToDownloads(seriesList: List<SeriesEntity>): Uri? {
        return withContext(Dispatchers.IO) {
            val gson = Gson()
            val jsonString = gson.toJson(seriesList)

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "playlog_backup_$timestamp.json"

            var uri: Uri? = null

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
                    }

                    val resolver = applicationContext.contentResolver
                    uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                    uri?.let {
                        resolver.openOutputStream(it)?.use { outputStream ->
                            OutputStreamWriter(outputStream).use { writer ->
                                writer.write(jsonString)
                            }
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    if (!downloadsDir.exists()) {
                        downloadsDir.mkdirs()
                    }
                    val file = File(downloadsDir, fileName)

                    FileOutputStream(file).use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.write(jsonString)
                        }
                    }
                    uri = Uri.fromFile(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uri?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        applicationContext.contentResolver.delete(it, null, null)
                    }
                }
                return@withContext null
            }
            uri
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