package com.example.playlog

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

class SearchActivity : BaseActivity() {
    val viewModel: SearchViewModel by viewModels()
    lateinit var seriesAdapter: SeriesSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView = findViewById<SearchView>(R.id.search_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_search)
        val emptyStateView = findViewById<View>(R.id.empty_state_view)

        seriesAdapter = SeriesSearchAdapter(emptyList())
        recyclerView.adapter = seriesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchSeries(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchSeries(it) }
                return true
            }
        })

        viewModel.searchResults.observe(this, Observer { results ->
            Log.d("SearchDebug", "Observer recebeu uma lista com tamanho: ${results?.size ?: "nulo"}")

            if (results.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                emptyStateView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateView.visibility = View.GONE
                seriesAdapter.updateData(results)
            }
        })
    }
}