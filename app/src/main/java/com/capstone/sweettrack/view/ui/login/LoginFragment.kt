package com.capstone.sweettrack.view.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerPromptTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_fragmentSignUp)
        }


        // Set aksi pada tombol login
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Validasi input kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jalankan proses login
            lifecycleScope.launch {
                val isLoginSuccessful = loginViewModel.login(email, password)
                if (isLoginSuccessful) {
                    Toast.makeText(requireContext(), R.string.login_successful, Toast.LENGTH_SHORT).show()
                    // Arahkan ke HomeFragment
                    findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
                } else {
                    Toast.makeText(requireContext(), R.string.login_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
