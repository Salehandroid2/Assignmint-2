package com.example.youtubesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtubesearch.databinding.ItemVideoBinding

class VideoAdapter(private val items: MutableList<SearchItem>) :
    RecyclerView.Adapter<VideoAdapter.VH>() {

    inner class VH(val b: ItemVideoBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(b)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val s = items[position].snippet
        holder.b.title.text = s?.title ?: ""
        holder.b.description.text = s?.description ?: ""
        holder.b.channel.text = s?.channelTitle ?: ""
        holder.b.publishTime.text = s?.publishTime ?: s?.publishedAt ?: ""
        val url = s?.thumbnails?.medium?.url ?: s?.thumbnails?.default?.url
        Glide.with(holder.b.thumbnail.context).load(url).into(holder.b.thumbnail)
    }

    fun update(newItems: List<SearchItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
