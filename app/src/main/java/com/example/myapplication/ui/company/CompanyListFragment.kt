package com.example.myapplication.ui.company

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Company
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage

class CompanyListFragment : Fragment() {
    private var selectedLogoUri: Uri? = null
    private var logoImageView: ImageView? = null
    private var onLogoUploaded: ((String) -> Unit)? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {
                selectedLogoUri = uri
                logoImageView?.let { Glide.with(this).load(uri).into(it) }
            }
        }
    }
    private val viewModel: CompanyViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CompanyAdapter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var spinnerSector: android.widget.Spinner
    private lateinit var spinnerLocation: android.widget.Spinner
    private var allCompanies: List<Company> = emptyList()
    private var selectedSector: String? = null
    private var selectedLocation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_company_list, container, false)
        searchView = view.findViewById(R.id.searchViewCompanies)
        spinnerSector = view.findViewById(R.id.spinnerSector)
        spinnerLocation = view.findViewById(R.id.spinnerLocation)
        recyclerView = view.findViewById(R.id.recyclerViewCompanies)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CompanyAdapter { company -> showEditCompanyDialog(company) }
        recyclerView.adapter = adapter
        view.findViewById<Button>(R.id.buttonAddCompany).setOnClickListener {
            showEditCompanyDialog(null)
        }

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterCompanies()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCompanies()
                return true
            }

        })

        spinnerSector.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSector = if (position == 0) null else parent.getItemAtPosition(position) as String
                filterCompanies()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
        spinnerLocation.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLocation = if (position == 0) null else parent.getItemAtPosition(position) as String
                filterCompanies()
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.companies.observe(viewLifecycleOwner) { companies ->
            allCompanies = companies
            setupSpinners(companies)
            filterCompanies()
        }
        viewModel.loadCompanies() }
    private fun filterCompanies() {
        val query = searchView.query?.toString()?.trim() ?: ""
        val filtered = allCompanies.filter { company ->
            (query.isBlank() ||
                company.name.contains(query, true) ||
                company.sector.contains(query, true) ||
                company.location.contains(query, true)) &&
            (selectedSector == null || company.sector == selectedSector) &&
            (selectedLocation == null || company.location == selectedLocation)
        }
        adapter.submitList(filtered)
    }

     private fun setupSpinners(companies: List<Company>) {
        val sectors = listOf("Tutti i settori") + companies.map { it.sector }.distinct().sorted()
        val locations = listOf("Tutte le località") + companies.map { it.location }.distinct().sorted()
        val sectorAdapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sectors)
        sectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSector.adapter = sectorAdapter
        val locationAdapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locations)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocation.adapter = locationAdapter
    }


    fun showEditCompanyDialog(company: Company?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_company, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.editTextCompanyName)
        val sectorInput = dialogView.findViewById<TextInputEditText>(R.id.editTextCompanySector)
        val locationInput = dialogView.findViewById<TextInputEditText>(R.id.editTextCompanyLocation)
        logoImageView = dialogView.findViewById(R.id.imageViewCompanyLogoPicker)
        selectedLogoUri = null

        if (company != null) {
            nameInput.setText(company.name)
            sectorInput.setText(company.sector)
            locationInput.setText(company.location)
            if (company.logoUrl.isNotEmpty()) {
                logoImageView?.let { Glide.with(this).load(company.logoUrl).into(it) }
            }
        }

        logoImageView?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (company == null) "Aggiungi Azienda" else "Modifica Azienda")
            .setView(dialogView)
            .setPositiveButton("Salva") { _, _ ->
                val saveCompany: (String) -> Unit = { logoUrl ->
                    val newCompany = Company(
                        id = company?.id ?: "",
                        name = nameInput.text.toString(),
                        sector = sectorInput.text.toString(),
                        location = locationInput.text.toString(),
                        logoUrl = logoUrl,
                        createdBy = "TODO_USER_ID"
                    )
                    if (company == null) {
                        viewModel.addCompany(newCompany) {}
                    } else {
                        viewModel.updateCompany(newCompany)
                    }
                }
                if (selectedLogoUri != null) {
                    uploadLogoToFirebase(selectedLogoUri!!, saveCompany)
                } else {
                    saveCompany(company?.logoUrl ?: "")
                }
            }
            .setNegativeButton("Annulla", null)
            .show()

    }

    private fun uploadLogoToFirebase(uri: Uri, onComplete: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("company_logos/${System.currentTimeMillis()}.jpg")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onComplete(downloadUri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Errore upload logo", Toast.LENGTH_SHORT).show()
                onComplete("")
            }
    }
    }

