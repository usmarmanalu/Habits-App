package com.dicoding.habitapp.ui.news

import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.dicoding.habitapp.databinding.*
import com.dicoding.habitapp.ui.*

class NewsActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Portal News"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        setupRecyclerView()

        observeViewModel()

        viewModel.getNews("id")
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter()
        binding.recyclerViewNews.apply {
            this.adapter = this@NewsActivity.adapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
    }

    private fun observeViewModel() {
        // Observe the newsData and errorState LiveData
        isLoading(true)
        viewModel.newsData.observe(this) { newsResponse ->
            // Update UI with news data
            isLoading(false)
            adapter.submitList(newsResponse.articles)
        }
    }

    private fun isLoading(isLoading: Boolean) {
        binding.apply {
            progresBarNews.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}


