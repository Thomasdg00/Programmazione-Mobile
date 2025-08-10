package com.example.myapplication.ui.company

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.CompanyRepository
import com.example.myapplication.data.model.Company
import kotlinx.coroutines.launch

class CompanyViewModel(
    private val repository: CompanyRepository = CompanyRepository()
) : ViewModel() {
    private val _companies = MutableLiveData<List<Company>>()
    val companies: LiveData<List<Company>> = _companies

    private val _company = MutableLiveData<Company?>()
    val company: LiveData<Company?> = _company

    fun loadCompanies() {
        viewModelScope.launch {
            _companies.value = repository.getAllCompanies()
        }
    }

    fun loadCompany(companyId: String) {
        viewModelScope.launch {
            _company.value = repository.getCompany(companyId)
        }
    }

    fun addCompany(company: Company, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val id = repository.addCompany(company)
            loadCompanies()
            onComplete(id)
        }
    }

    fun updateCompany(company: Company) {
        viewModelScope.launch {
            repository.updateCompany(company)
            loadCompanies()
        }
    }

    fun deleteCompany(companyId: String) {
        viewModelScope.launch {
            repository.deleteCompany(companyId)
            loadCompanies()
        }
    }
}
