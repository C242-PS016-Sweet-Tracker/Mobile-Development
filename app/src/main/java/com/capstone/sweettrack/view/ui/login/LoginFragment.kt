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
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.util.CustomEmailEditText
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var email: CustomEmailEditText
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

        email.addTextChangedListener { checkFormValidity(email, password) }
        password.addTextChangedListener { checkFormValidity(email, password) }
    }

    private fun checkFormValidity(
        email: CustomEmailEditText,
        password: CustomPasswordEditText,
    ) {
        val isEmailValid = email.error == null && email.text?.isNotEmpty() == true
        val isPasswordValid = password.error == null && password.text?.isNotEmpty() == true
        binding.loginButton.isEnabled = isEmailValid && isPasswordValid
    }

    private fun loginAction(email: String, password: String) {

        loginViewModel.isLoading.observe(requireActivity()) { isLoading ->
            showLoading(isLoading)
        }

        loginViewModel.login(email, password)

        loginViewModel.loginResult.observe(requireActivity()) { result ->
            if (result != null) {
                if (result.error != true) {
                    val user = result.loginResult
                    if (user != null) {
                        loginViewModel.saveSession(
                            UserModel(user.userId ?: "", user.name ?: "", user.token ?: "", true)
                        )
                        val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                            setTitle("Yess!")
                            setMessage(getString(R.string.login_successful))
                            setCancelable(false)
                            create()
                        }.show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            alertDialog.dismiss()
                            findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
                        }, 2000)
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Login gagal ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

        val message =
            ObjectAnimator.ofFloat(binding.loginTV, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val registerText =
            ObjectAnimator.ofFloat(binding.registerPromptTextView, View.ALPHA, 1f).setDuration(100)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                registerText,
                message,
                email,
                emailEdit,
                password,
                passwordEdit,
                loginBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.loginButton.isEnabled = false

        binding.registerPromptTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_fragmentSignUp)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            loginAction(email, password)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
