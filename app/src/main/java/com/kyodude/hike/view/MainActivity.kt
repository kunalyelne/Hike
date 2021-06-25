package com.kyodude.hike.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyodude.hike.databinding.ActivityMainBinding
import com.kyodude.hike.utils.extensions.addOnScrolledToEnd
import com.kyodude.hike.view.adapters.PhotosAdapter
import com.kyodude.hike.viewModel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: PhotosAdapter
    private var query: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        adapter = PhotosAdapter()
        binding.rvData.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        binding.rvData.adapter = adapter

        binding.searchBtn.setOnClickListener {
            query = binding.etSearch.text.toString()
            if(query==null || query.equals("") || query?.replaceAfter(" ","").equals("")) {
                Toast.makeText(this,"Please enter any text",Toast.LENGTH_LONG).show()
            } else {
                viewModel.setSearch(query!!)
                try {
                    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.rvData.addOnScrolledToEnd {
            viewModel.incrementPage(query!!)
        }

        viewModel.getIsLoading().observe(this, {
            if(it) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        })

        viewModel.getPhotoListLiveData().observe(this, {
            adapter.setPhotos(it)
        })
    }
}