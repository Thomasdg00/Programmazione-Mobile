package com.example.myapplication.ui.azienda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Review

class AziendaRecensioniFragment : Fragment() {
    private val viewModel: AziendaRecensioniViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecensioniAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_azienda_recensioni, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewReviews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RecensioniAdapter()
        recyclerView.adapter = adapter

        // Mostra info azienda
        val companyName = arguments?.getString("companyName") ?: ""
        val companyEmail = arguments?.getString("companyEmail") ?: ""
        val companyAddress = arguments?.getString("companyAddress") ?: ""
        val companyVat = arguments?.getString("companyVatNumber") ?: ""

        view.findViewById<TextView>(R.id.textViewCompanyName).text = companyName
        view.findViewById<TextView>(R.id.textViewCompanyInfo).text =
            "Email: $companyEmail\nIndirizzo: $companyAddress\nP.IVA: $companyVat"

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val companyId = arguments?.getString("companyId") ?: return
        viewModel.caricaRecensioni(companyId)
        viewModel.recensioni.observe(viewLifecycleOwner, Observer { reviews ->
            adapter.submitList(reviews)
        })
    }
}

class RecensioniAdapter : RecyclerView.Adapter<RecensioniAdapter.RecensioneViewHolder>() {
    private var recensioni: List<Review> = emptyList()

    fun submitList(list: List<Review>) {
        recensioni = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecensioneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return RecensioneViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecensioneViewHolder, position: Int) {
        val recensione = recensioni[position]
        // Qui puoi personalizzare la visualizzazione
        holder.itemView.findViewById<android.widget.TextView>(android.R.id.text1).text = "Voto: ${recensione.rating}"
        holder.itemView.findViewById<android.widget.TextView>(android.R.id.text2).text = recensione.comment
    }

    override fun getItemCount() = recensioni.size

    class RecensioneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
