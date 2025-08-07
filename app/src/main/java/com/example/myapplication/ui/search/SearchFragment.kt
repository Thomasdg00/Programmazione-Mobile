package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.AziendaFragment
import com.example.myapplication.R
import com.example.myapplication.data.adapter.SearchAdapter
import com.example.myapplication.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        adapter = SearchAdapter(emptyList()) { selectedItem ->
            // Azione al click: apri il nuovo fragment
            val fragment = AziendaFragment.newInstance(selectedItem)

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment) // Usa l'id corretto del tuo container
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewResults.adapter = adapter

        // Osserva i risultati filtrati
        viewModel.filteredItems.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        // Gestione input dalla SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterItems(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterItems(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}