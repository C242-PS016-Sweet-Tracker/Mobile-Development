package com.capstone.sweettrack.view.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.util.CustomEmailEditText
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentSignUpBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var username: TextInputEditText
    private lateinit var email: CustomEmailEditText
    private lateinit var password: CustomPasswordEditText
    private lateinit var rePassword: CustomPasswordEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        playAnimation()
        setupAction()

        username = binding.usernameEditText
        email = binding.emailEditText
        password = binding.passwordEditText
        rePassword = binding.rePasswordEditText

        username.addTextChangedListener { checkFormValidity(username, email, password, rePassword) }
        email.addTextChangedListener { checkFormValidity(username, email, password, rePassword) }
        password.addTextChangedListener { checkFormValidity(username, email, password, rePassword) }
        rePassword.addTextChangedListener { checkFormValidity(username, email, password, rePassword) }

    }

    private fun checkFormValidity(
        username: TextInputEditText,
        email: CustomEmailEditText,
        password: CustomPasswordEditText,
        rePassword: CustomPasswordEditText,
    ) {
        val isUsernameValid = username.error == null && username.text?.isNotEmpty() == true
        val isEmailValid = email.error == null && email.text?.isNotEmpty() == true
        val isPasswordValid = password.error == null && password.text?.isNotEmpty() == true
        val isRePasswordValid = rePassword.error == null && rePassword.text?.isNotEmpty() == true

        val isPasswordsMatch = password.text.toString() == rePassword.text.toString()

        if (!isPasswordsMatch) {
            rePassword.error = "Password tidak cocok"
        } else {
            rePassword.error = null
        }

        binding.signUpButton.isEnabled = isUsernameValid && isEmailValid && isPasswordValid && isRePasswordValid && isPasswordsMatch
    }

    private fun register() {
        val usernameText = username.text.toString()
        val emailText = password.text.toString()
        val passText = password.text.toString()
        val rePassword = rePassword.text.toString().trim()

//        showLoading(true)

        findNavController().navigate(R.id.action_fragmentSignUp_to_loginFragment)


//        if (viewModel.validateInputs(username, email, password, confirmPassword)) {
//            // Simulate Sign Up Success
//            Toast.makeText(requireContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show()
//
//            // Navigate to LoginFragment
//            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
//        } else {
//            Toast.makeText(requireContext(), "Please check your inputs.", Toast.LENGTH_SHORT).show()
//        }


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

        val titleTv =
            ObjectAnimator.ofFloat(binding.signUpTV, View.ALPHA, 1f).setDuration(100)
        val usernameTv =
            ObjectAnimator.ofFloat(binding.usernameTV, View.ALPHA, 1f).setDuration(100)
        val usernameEdit =
            ObjectAnimator.ofFloat(binding.usernameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTv = ObjectAnimator.ofFloat(binding.emailTV, View.ALPHA, 1f).setDuration(100)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val rePassword =
            ObjectAnimator.ofFloat(binding.ulangPassTextView, View.ALPHA, 1f).setDuration(100)
        val rePasswordEdit =
            ObjectAnimator.ofFloat(binding.rePassEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signBtn = ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                titleTv,
                usernameTv,
                usernameEdit,
                emailTv,
                emailEdit,
                password,
                passwordEdit,
                rePassword,
                rePasswordEdit,
                signBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.signUpButton.isEnabled = false

        binding.signUpButton.setOnClickListener {
            register()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
