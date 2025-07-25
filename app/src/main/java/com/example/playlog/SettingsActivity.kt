import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.playlog.AppDatabase
import com.example.playlog.BaseActivity
import com.example.playlog.R
import com.example.playlog.SeriesDao
import com.example.playlog.databinding.ActivityDetailsBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsActivity : BaseActivity() {

    private lateinit var binding: DialogConfirmDeleteBinding
    private lateinit var seriesDao: SeriesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla o layout usando View Binding
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        seriesDao = AppDatabase.getInstance(applicationContext).seriesDao()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Lógica para o botão de backup
        binding.buttonDelete.setOnClickListener { // ID do seu XML: button_delete
            handleBackup()
        }

        // Lógica para o botão de apagar tudo
        binding.buttonUpdate.setOnClickListener { // ID do seu XML: button_update
            showDeleteConfirmationDialog()
        }
    }

    /**
     * Inicia o processo de backup dos dados.
     */
    private fun handleBackup() {
        lifecycleScope.launch {
            try {
                // Busca os dados no banco em uma thread de I/O
                val seriesList = withContext(Dispatchers.IO) {
                    seriesDao.getAllSeriesSync()
                }

                if (seriesList.isEmpty()) {
                    Toast.makeText(this@ConfiguracoesActivity, "Nenhum dado para fazer backup.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Converte a lista para JSON e salva em um arquivo
                val file = saveJsonToFile(seriesList)
                Toast.makeText(this@ConfiguracoesActivity, "Backup salvo em: ${file.absolutePath}", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                Toast.makeText(this@ConfiguracoesActivity, "Falha ao criar o backup: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    /**
     * Converte uma lista de séries para JSON e salva em um arquivo no armazenamento externo da aplicação.
     */
    private suspend fun saveJsonToFile(seriesList: List<Any>): File {
        return withContext(Dispatchers.IO) {
            val gson = Gson()
            val jsonString = gson.toJson(seriesList)

            // Define o nome do arquivo com data e hora
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "playlog_backup_$timestamp.json"

            // Salva no diretório de documentos acessível pela aplicação
            val directory = getExternalFilesDir(null)
            val file = File(directory, fileName)

            FileWriter(file).use { it.write(jsonString) }
            file
        }
    }

    /**
     * Exibe o dialog de confirmação para apagar os dados.
     */
    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.create()

        // Referencia os botões do XML do dialog
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

    /**
     * Executa a exclusão de todos os dados do banco.
     */
    private fun deleteAllData() {
        lifecycleScope.launch(Dispatchers.IO) {
            seriesDao.deleteAllSeries()
            // Mostra o Toast na thread principal
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ConfiguracoesActivity, "Todos os dados foram apagados.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}