package com.capstone.sweettrack.view.ui.resetpassword

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
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.util.CustomEmailEditText
import com.capstone.sweettrack.util.CustomPasswordEditText
import com.capstone.sweettrack.view.ViewModelFactory
import com.capstone.sweettrack.view.ui.signup.SignUpFragmentDirections
import com.capstone.sweettrack.view.ui.signup.SignUpViewModel
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentResetPasswordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResetPasswordViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

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

    private fun sendToEmail() {

        showLoading(true)

        val emailText = email.text.toString()

        viewModel.requestOTP(emailText)

        viewModel.requestResult.observe(viewLifecycleOwner) { result ->

            if (result != null){
                if (result.error != true) {
                    showLoading(false)
                    val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                        setTitle("Informasi!")
                        setMessage("Cek email!! dan masukkan kode OTP")
                        setCancelable(false)
                        create()
                    }.show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                        val action = ResetPasswordFragmentDirections.actionFragmentResetPasswordToNewPasswordFragment()
                        findNavController().navigate(action)
                    }, 4000)
                }
            }

        }
    }

    private fun checkFormValidity(
        email: CustomEmailEditText,
    ) {
        val isEmailValid = email.error == null && email.text?.isNotEmpty() == true
        binding.sendButton.isEnabled = isEmailValid
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
            sendToEmail()
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