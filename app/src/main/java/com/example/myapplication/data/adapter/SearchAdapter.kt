package com.example.myapplication.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class SearchAdapter(
    private var items: List<String>,
    private val onItemClick: (String) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textItem: TextView = itemView.findViewById(R.id.textItem)

    fun bind(item: String) {
        textItem.text = item
        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.textItem.text = items[position]
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}
