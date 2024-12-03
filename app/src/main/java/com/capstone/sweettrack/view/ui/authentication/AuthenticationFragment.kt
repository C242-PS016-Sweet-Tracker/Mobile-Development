package com.capstone.sweettrack.view.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.capstone.sweettrack.util.CustomEmailEditText
import com.capstone.sweettrack.util.CustomNumberEditText
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.capstone.sweettrack.view.ViewModelFactory
import com.capstone.sweettrack.view.ui.home.HomeViewModel
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentAuthenticationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AuthenticationFragment : Fragment() {

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthenticationViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var kode: CustomNumberEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: AuthenticationFragmentArgs by navArgs()
        email = args.email
        password = args.password

        kode = binding.codeEditText

        kode.addTextChangedListener { checkFormValidity(kode) }

        playAnimation()
        setupAction()
    }

    private fun checkFormValidity(
        kode: CustomNumberEditText,
    ) {
        val isCodeValid = kode.error == null && kode.text?.isNotEmpty() == true
        binding.sendButton.isEnabled = isCodeValid
    }


    private fun playAnimation() {

        val image =
            ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(100)
        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val kode = ObjectAnimator.ofFloat(binding.kodeTextView, View.ALPHA, 1f).setDuration(100)
        val kodeEdit =
            ObjectAnimator.ofFloat(binding.codeEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val desc =
            ObjectAnimator.ofFloat(binding.textKeterangan, View.ALPHA, 1f).setDuration(100)
        val desc2 =
            ObjectAnimator.ofFloat(binding.tvKeterangan2, View.ALPHA, 1f).setDuration(100)
        val sendBtn = ObjectAnimator.ofFloat(binding.sendButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                image,
                title,
                message,
                kode,
                kodeEdit,
                desc,
                desc2,
                sendBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.sendButton.isEnabled = false

        binding.tvKeterangan2.setOnClickListener {

        }

        binding.sendButton.setOnClickListener {
            verifyOTP()
        }

    }

    private fun verifyOTP() {
        val emailText = email
        val otpText = kode.text.toString()
        val passwordText = password

        viewModel.verifyOTP(emailText, otpText, passwordText)

        viewModel.verifyResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.statusCode == 201) {
                    // Jika berhasil
                    Toast.makeText(requireContext(), result.describe, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
                } else {
                    // Jika gagal
                    Toast.makeText(requireContext(), result.describe, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}