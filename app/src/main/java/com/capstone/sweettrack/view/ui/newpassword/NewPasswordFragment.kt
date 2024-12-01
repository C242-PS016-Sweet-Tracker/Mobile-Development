package com.capstone.sweettrack.view.ui.newpassword

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
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentNewPasswordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPasswordFragment : Fragment() {

    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPasswordViewModel by viewModels()

    private lateinit var password: CustomPasswordEditText
    private lateinit var rePassword: CustomPasswordEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        playAnimation()
        setupAction()

        password = binding.passwordEditText
        rePassword = binding.passwordEditText

        password.addTextChangedListener { checkFormValidity(password, rePassword) }
        rePassword.addTextChangedListener { checkFormValidity(password, rePassword) }
    }

    private fun checkFormValidity(
        password: CustomPasswordEditText,
        rePassword: CustomPasswordEditText,
    ) {
        val isPasswordValid = password.error == null && password.text?.isNotEmpty() == true
        val isRePasswordValid = rePassword.error == null && rePassword.text?.isNotEmpty() == true
        binding.sendButton.isEnabled = isPasswordValid && isRePasswordValid
    }


    private fun changePass(password: String, rePassword: String) {

        // proses

        findNavController().navigate(R.id.action_newPasswordFragment_to_loginFragment)
    }

    private fun setupView() {
        val window = requireActivity().window
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun playAnimation() {

        val image =
            ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(100)
        val title =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val rePassword =
            ObjectAnimator.ofFloat(binding.rePasswordTextView, View.ALPHA, 1f).setDuration(100)
        val rePasswordEdit =
            ObjectAnimator.ofFloat(binding.rePasswordEditTextLayout, View.ALPHA, 1f)
                .setDuration(100)
        val prevBtn =
            ObjectAnimator.ofFloat(binding.prevButton, View.ALPHA, 1f).setDuration(100)
        val sendBtn = ObjectAnimator.ofFloat(binding.sendButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                image,
                title,
                message,
                password,
                passwordEdit,
                rePassword,
                rePasswordEdit,
                prevBtn,
                sendBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.sendButton.isEnabled = false

        binding.prevButton.setOnClickListener {
            findNavController().navigate(R.id.action_newPasswordFragment_to_fragmentResetPassword)
        }

        binding.sendButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString().trim()
            val rePassword = binding.rePasswordEditText.text.toString().trim()

            changePass(password, rePassword)
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