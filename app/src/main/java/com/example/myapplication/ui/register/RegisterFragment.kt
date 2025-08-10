package com.example.myapplication.ui.register


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.RegisterDataSource
import com.example.myapplication.data.RegisterRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    val nameEditText = view.findViewById<TextInputEditText>(R.id.editTextName)
    val emailEditText = view.findViewById<TextInputEditText>(R.id.editTextEmail)
    val passwordEditText = view.findViewById<TextInputEditText>(R.id.editTextPassword)
    val confirmPasswordEditText = view.findViewById<TextInputEditText>(R.id.editTextConfirmPassword)
    val bioEditText = view.findViewById<TextInputEditText>(R.id.editTextBio)
    val profileImageEditText = view.findViewById<TextInputEditText>(R.id.editTextProfileImage)
    val registerButton = view.findViewById<MaterialButton>(R.id.buttonRegister)

        val repository = RegisterRepository(RegisterDataSource())
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(repository))[RegisterViewModel::class.java]

        val userButton = view.findViewById<MaterialButton>(R.id.buttonUser)
        val companyButton = view.findViewById<MaterialButton>(R.id.buttonCompany)

        registerButton.setOnClickListener {
            val email = emailEditText.text?.toString() ?: ""
            val password = passwordEditText.text?.toString() ?: ""
            val confirmPassword = confirmPasswordEditText.text?.toString() ?: ""
            val name = nameEditText.text?.toString() ?: ""
            val bio = bioEditText.text?.toString() ?: ""
            val profileImageUrl = profileImageEditText.text?.toString() ?: ""
            val type = when {
                userButton.isChecked -> "standard"
                companyButton.isChecked -> "aziendale"
                else -> "standard"
            }

            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || name.isBlank()) {
                Toast.makeText(requireContext(), getString(R.string.invalid_registration_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(requireContext(), getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(email, password, name, type, bio, profileImageUrl)
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.error?.let {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
            }
            result.success?.let {
                Toast.makeText(requireContext(), getString(R.string.registration_success) + ", " + it.displayName, Toast.LENGTH_SHORT).show()
                // TODO: Navigate to main screen or login
            }
        }
    }
}
