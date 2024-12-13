package com.capstone.sweettrack.view.ui.newpassword

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.util.CustomNumberEditText
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentNewPasswordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPasswordFragment : Fragment() {

    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewPasswordViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var otp: CustomNumberEditText
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

        otp = binding.otpEditText
        password = binding.passwordEditText
        rePassword = binding.rePasswordEditText

        password.addTextChangedListener { checkFormValidity(otp, password, rePassword) }
        rePassword.addTextChangedListener { checkFormValidity(otp, password, rePassword) }
    }

    private fun checkFormValidity(
        otp: CustomNumberEditText,
        password: CustomPasswordEditText,
        rePassword: CustomPasswordEditText
    ) {
        val isOtpValid = otp.error == null && otp.text?.isNotEmpty() == true
        val isPasswordValid = password.error == null && password.text?.isNotEmpty() == true
        val isRePasswordValid = rePassword.error == null && rePassword.text?.isNotEmpty() == true

        val isPasswordsMatch = password.text.toString() == rePassword.text.toString()

        if (!isPasswordsMatch) {
            rePassword.error = "Password tidak cocok"
        } else {
            rePassword.error = null
        }

        binding.sendButton.isEnabled =
            isOtpValid && isPasswordValid && isRePasswordValid && isPasswordsMatch
    }


    private fun changePass() {
        showLoading(true)

        val otpText = otp.text.toString()
        val passwordText = password.text.toString()

        viewModel.verifyOTP(otpText, passwordText)

        viewModel.verifyResult.observe(viewLifecycleOwner) { result ->

            if (result != null) {
                if (!result.error) {
                    showLoading(false)
                    val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                        setTitle(result.message)
                        setMessage(result.describe)
                        setCancelable(false)
                        create()
                    }.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                        if (findNavController().currentDestination?.id == R.id.newPasswordFragment) {
                            val action =
                                NewPasswordFragmentDirections.actionNewPasswordFragmentToLoginFragment()
                            findNavController().navigate(action)
                        }
                    }, 4000)
                }
            }

        }
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
        val otp =
            ObjectAnimator.ofFloat(binding.otpTextView, View.ALPHA, 1f).setDuration(100)
        val otpEdit =
            ObjectAnimator.ofFloat(binding.otpEditTextLayout, View.ALPHA, 1f).setDuration(100)
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
                otp,
                otpEdit,
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
            changePass()
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