package com.example.behaviordemo.mimusic.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.behaviordemo.databinding.ItemSongLaypoutBinding

class SongAdater(
    private val values: List<Int>
) : RecyclerView.Adapter<SongAdater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSongLaypoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = (item + 1).toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemSongLaypoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.tvItemMuiscNo
    }
}
