package com.capstone.sweettrack.view.ui.welcome

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
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentWelcomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        playAnimation()
        setupAction()
        observeViewModel()
    }

    private fun setupView() {
        val window = requireActivity().window
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.GONE
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, together)
            start()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            viewModel.onLoginClicked()
        }

        binding.signupButton.setOnClickListener {
            viewModel.onSignupClicked()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun observeViewModel() {
        viewModel.navigateTo.observe(viewLifecycleOwner) { navigateTo ->
            val navController = findNavController()
            when (navigateTo) {
                WelcomeViewModel.NavigateTo.LOGIN -> {
                    navController.navigate(R.id.action_welcomeFragment_to_loginFragment)
                    viewModel.navigationDone()
                }

                WelcomeViewModel.NavigateTo.SIGNUP -> {
                    navController.navigate(
                        R.id.action_welcomeFragment_to_fragmentSignUp
                    )
                    viewModel.navigationDone()
                }

                null -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}