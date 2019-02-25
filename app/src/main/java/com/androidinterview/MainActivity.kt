package com.androidinterview

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MainAdapter()
    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    private val prefs: SharedPreferences by lazy {
        this.getSharedPreferences("com.androidinterview.prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_main.setHasFixedSize(true)
        val gridSpans = 2
        val layoutManager = StaggeredGridLayoutManager(gridSpans, StaggeredGridLayoutManager.VERTICAL)
        recycler_main.layoutManager = layoutManager
        recycler_main.adapter = adapter

        adapter.listener = object : RecyclerViewClickListener {
            override fun onClick(view: View, position: Int) {
                viewModel.toggleFav(adapter.key(position), prefs)
                adapter.toggle(position)
            }
        }

        viewModel.itemsLiveData.observe(this, Observer {
            @Suppress("SENSELESS_COMPARISON")
            val list = if (it!!.GalleryImages==null) emptyList() else it.GalleryImages
            adapter.addAll(list)
        })
        viewModel.getItems(prefs)
    }
}
