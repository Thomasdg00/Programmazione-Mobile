package com.example.myapplication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.LoginDataSource
import com.example.myapplication.data.LoginRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailEditText = view.findViewById<TextInputEditText>(R.id.editTextEmail)
        val passwordEditText = view.findViewById<TextInputEditText>(R.id.editTextPassword)
        val loginButton = view.findViewById<MaterialButton>(R.id.buttonLogin)

        // Simple ViewModel instantiation for demo; use DI in production
        val repository = LoginRepository(LoginDataSource())
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repository))[LoginViewModel::class.java]

        loginButton.setOnClickListener {
            val email = emailEditText.text?.toString() ?: ""
            val password = passwordEditText.text?.toString() ?: ""
            viewModel.login(email, password)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.error?.let {
                Toast.makeText(requireContext(), getString(it), Toast.LENGTH_SHORT).show()
            }
            result.success?.let {
                Toast.makeText(requireContext(), getString(R.string.welcome) + ", " + it.displayName, Toast.LENGTH_SHORT).show()
                val intent = android.content.Intent(requireContext(), com.example.myapplication.MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}
