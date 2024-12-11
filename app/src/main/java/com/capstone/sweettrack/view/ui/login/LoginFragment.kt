package com.capstone.sweettrack.view.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.capstone.sweettrack.util.CustomUsernameEditText
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var email: CustomUsernameEditText
    private lateinit var password: CustomPasswordEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupAction()
        playAnimation()

        email = binding.emailEditText
        password = binding.passwordEditText

        binding.loginButton.setOnClickListener {
            val emailText = email.text.toString()
            val passText = password.text.toString()

            loginAction(emailText, passText)
        }

        email.addTextChangedListener { checkFormValidity(email, password) }
        password.addTextChangedListener { checkFormValidity(email, password) }
    }

    private fun checkFormValidity(
        email: CustomUsernameEditText,
        password: CustomPasswordEditText,
    ) {
        val isEmailValid = email.error == null && email.text?.isNotEmpty() == true
        val isPasswordValid = password.error == null && password.text?.isNotEmpty() == true
        binding.loginButton.isEnabled = isEmailValid && isPasswordValid
    }

    private fun loginAction(email: String, password: String) {

        loginViewModel.login(email, password)

        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (result.error != true) {
                    val user = result.loginResult
                    if (user != null) {
                        loginViewModel.saveSession(
                            UserModel(user.userId ?: "", user.name ?: "", user.token ?: "", true)
                        )
                        val alertDialog = AlertDialog.Builder(requireContext()).apply {
                            setTitle("Informasi")
                            setMessage(getString(R.string.login_successful))
                            setCancelable(false)
                            create()
                        }.show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            alertDialog.dismiss()
                            if (findNavController().currentDestination?.id == R.id.loginFragment) {
                                val action = LoginFragmentDirections.actionLoginFragmentToNavigationHome()
                                findNavController().navigate(action)
                            }
                        }, 2000)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login gagal ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

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
    }



    private fun playAnimation() {

        val message =
            ObjectAnimator.ofFloat(binding.loginTV, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailTV, View.ALPHA, 1f).setDuration(100)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val registerText =
            ObjectAnimator.ofFloat(binding.registerPromptTextView, View.ALPHA, 1f).setDuration(100)
        val resetText =
            ObjectAnimator.ofFloat(binding.resetPassTextView, View.ALPHA, 1f).setDuration(100)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val signUpBtn = ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                registerText,
                message,
                email,
                emailEdit,
                password,
                passwordEdit,
                resetText,
                loginBtn,
                signUpBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.loginButton.isEnabled = false

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_fragmentSignUp)
        }

        binding.resetPassTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_fragmentResetPassword)
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
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
