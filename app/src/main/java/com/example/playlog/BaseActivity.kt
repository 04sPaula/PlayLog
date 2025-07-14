package com.example.playlog

import android.content.Intent
import android.widget.ImageButton
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