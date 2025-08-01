package com.example.playlog

import android.content.Intent
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupNavigation()
    }

    private fun setupNavigation() {
        val btnHome = findViewById<ImageButton>(R.id.nav_home)
        val btnSearch = findViewById<ImageButton>(R.id.nav_search)
        val btnSettings = findViewById<ImageButton>(R.id.nav_settings)
        val btnBackWard = findViewById<ImageView>(R.id.ic_backward)

        btnBackWard?.setOnClickListener {
            finish()
        }

        btnSettings?.setOnClickListener {
            if (this !is SettingsActivity) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        btnHome?.setOnClickListener {
            if (this !is MainActivity) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        btnSearch?.setOnClickListener {
            if (this !is SearchActivity) {
                startActivity(Intent(this, SearchActivity::class.java))
            }
        }

    }
}