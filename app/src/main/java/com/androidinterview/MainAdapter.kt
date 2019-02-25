package com.androidinterview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.server.pojo.GalleryImage
import kotlinx.android.synthetic.main.item.view.*

class MainAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<GalleryImage>()
    var listener: RecyclerViewClickListener? = null

    override fun getItemCount(): Int {
        return items.size
    }

    fun addAll(list: List<GalleryImage>) {
        val initialSize = items.size
        items.addAll(list)
        val updatedSize = items.size
        notifyItemRangeInserted(initialSize,updatedSize)
    }

    fun toggle(row: Int) {
        items.get(row).Checked = if (items.get(row).Checked) false else true
        notifyItemChanged(row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false), listener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val w = items.get(position).OriginalWidth
        val h = items.get(position).OriginalHeight
        holder.text.text = "$w X $h"
        val url = url(position)
        Glide.with(holder.context).load(url).into(holder.image)
        if (items.get(position).Checked) holder.checkmark.visibility = View.VISIBLE
        else holder.checkmark.visibility = View.GONE
    }

    fun url(pos: Int) = if (items.get(pos).ImageUrls.SizeThumb != null)
        items.get(pos).ImageUrls.SizeThumb else items.get(pos).ImageUrls.SizeLarge
    fun key(pos: Int) = items.get(pos).ImageUrls.SizeLarge
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val text = view.text_item
    val image = view.image_item
    val checkmark = view.checkbox
    val context: Context = view.context

    private var listener: RecyclerViewClickListener? = null

    constructor(v: View, l: RecyclerViewClickListener?): this(v) {
        listener = l
        v.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            listener?.onClick(view, adapterPosition)
        }
    }
}

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int)
}
