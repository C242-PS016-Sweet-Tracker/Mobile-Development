package com.capstone.sweettrack.view.ui.resetpassword

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.util.CustomEmailEditText
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentResetPasswordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResetPasswordViewModel by viewModels()

    private lateinit var email: CustomEmailEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        playAnimation()
        setupAction()

        email = binding.emailEditText

        email.addTextChangedListener { checkFormValidity(email) }


    }

    private fun sendToEmail(email: String) {

    }

    private fun checkFormValidity(
        email: CustomEmailEditText,
    ) {
        val isEmailValid = email.error == null && email.text?.isNotEmpty() == true
        binding.sendButton.isEnabled = isEmailValid
    }

    private fun setupView() {
        val window = requireActivity().window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun playAnimation() {

        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val prevBtn = ObjectAnimator.ofFloat(binding.prevButton, View.ALPHA, 1f).setDuration(100)
        val sendBtn = ObjectAnimator.ofFloat(binding.sendButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                image, message, email, emailEdit, title, prevBtn, sendBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {

        binding.sendButton.isEnabled = false

        binding.prevButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentResetPassword_to_loginFragment)
        }

        binding.sendButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            sendToEmail(email)
            findNavController().navigate(R.id.action_fragmentResetPassword_to_newPasswordFragment)

        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}