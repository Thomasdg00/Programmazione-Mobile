package com.example.myapplication.ui.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Company

class CompanyAdapter(
    private val onEdit: (Company) -> Unit
) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {
    private var companies: List<Company> = emptyList()

    fun submitList(list: List<Company>) {
        companies = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
        return CompanyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companies[position]
        holder.bind(company)
        holder.itemView.setOnClickListener { onEdit(company) }
    }

    override fun getItemCount() = companies.size

    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(company: Company) {
            itemView.findViewById<TextView>(R.id.textViewCompanyName).text = company.name
            itemView.findViewById<TextView>(R.id.textViewCompanySector).text = company.sector
            itemView.findViewById<TextView>(R.id.textViewCompanyLocation).text = company.location
            val logoView = itemView.findViewById<ImageView>(R.id.imageViewCompanyLogo)
            if (company.logoUrl.isNotEmpty()) {
                Glide.with(itemView).load(company.logoUrl).into(logoView)
            } else {
                logoView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }
}
