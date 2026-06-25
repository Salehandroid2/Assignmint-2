package com.example.youtubesearch

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubesearch.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = VideoAdapter(mutableListOf())

    // API key provided for the assignment
    private val apiKey = "AIzaSyAl2XVvUdWzbCz6MimdtmLYV27dZSNXuIk"
    private val maxResults = 25

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.searchButton.setOnClickListener { startSearch() }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startSearch()
                true
            } else {
                false
            }
        }
    }

    private fun startSearch() {
        val q = binding.searchInput.text.toString().trim()
        if (q.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            return
        }
        SearchTask().execute(q)
    }

    // Network call runs off the main thread using AsyncTask
    @Suppress("DEPRECATION")
    inner class SearchTask : AsyncTask<String, Void, SearchResponse?>() {

        private var errorMessage: String? = null

        override fun onPreExecute() {
            binding.progress.visibility = View.VISIBLE
            binding.emptyText.visibility = View.GONE
            adapter.update(emptyList())
        }

        override fun doInBackground(vararg params: String?): SearchResponse? {
            val query = params[0] ?: return null
            var connection: HttpURLConnection? = null
            try {
                val encoded = URLEncoder.encode(query, "UTF-8")
                val urlStr =
                    "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video" +
                        "&q=" + encoded + "&maxResults=" + maxResults + "&key=" + apiKey
                val url = URL(urlStr)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                val code = connection.responseCode
                if (code != HttpURLConnection.HTTP_OK) {
                    errorMessage = "Server error (code " + code + "). Check API key or quota."
                    return null
                }
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                return Gson().fromJson(body, SearchResponse::class.java)
            } catch (e: Exception) {
                errorMessage = "Network error: " + e.localizedMessage
                return null
            } finally {
                connection?.disconnect()
            }
        }

        override fun onPostExecute(result: SearchResponse?) {
            binding.progress.visibility = View.GONE
            if (result == null) {
                binding.emptyText.text = errorMessage ?: "Something went wrong"
                binding.emptyText.visibility = View.VISIBLE
                return
            }
            val items = result.items.filter { it.snippet != null }
            adapter.update(items)
            if (items.isEmpty()) {
                binding.emptyText.text = "No results found"
                binding.emptyText.visibility = View.VISIBLE
            }
        }
    }
}
