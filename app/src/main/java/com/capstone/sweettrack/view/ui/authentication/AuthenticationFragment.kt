package com.capstone.sweettrack.view.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.util.CustomNumberEditText
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentAuthenticationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

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

        setupTime()


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
        val desc3 =
            ObjectAnimator.ofFloat(binding.tvTime, View.ALPHA, 1f).setDuration(100)
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
                desc3,
                sendBtn
            )
            startDelay = 100
        }.start()
    }

    private fun setupAction() {
        binding.sendButton.isEnabled = false

        binding.tvKeterangan2.setOnClickListener {
            resendOTP()
        }

        binding.sendButton.setOnClickListener {
            verifyOTP()
            setupTime()
        }

    }

    private fun setupTime() {
        val countdownTimeInMillis = 5 * 60 * 1000L
        val intervalInMillis = 1000L

        binding.tvKeterangan2.visibility = View.GONE

        object : CountDownTimer(countdownTimeInMillis, intervalInMillis) {
            override fun onTick(millisUntilFinished: Long) {

                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60

                val formattedTime = String.format(Locale.US, "%02d:%02d", minutes, seconds)

                binding.tvTime.text = formattedTime
            }

            override fun onFinish() {
                binding.tvTime.text = getString(R.string.otp_kadaluwarsa)
                binding.tvKeterangan2.visibility = View.VISIBLE
            }
        }.start()
    }


    private fun resendOTP() {
        val emailText = email

        viewModel.resendOTP(emailText)

        viewModel.verifyResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                if (!result.error) {
                    val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                        setTitle(result.message)
                        setMessage(result.describe)
                        setCancelable(false)
                        create()
                    }.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                    }, 3000)
                }
            }
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
                    Toast.makeText(requireContext(), result.describe, Toast.LENGTH_SHORT).show()

                    viewModel.login(email, password)

                    viewModel.loginResult.observe(requireActivity()) { loginResult ->
                        if (loginResult != null) {
                            if (loginResult.error != true) {
                                val user = loginResult.loginResult
                                if (user != null) {
                                    viewModel.saveSession(
                                        UserModel(
                                            user.userId ?: "",
                                            user.name ?: "",
                                            user.token ?: "",
                                            true
                                        )
                                    )

                                    val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                                        setTitle(R.string.sign_up_succesfull)
                                        setMessage(getString(R.string.sign_up_succesfull_message))
                                        setCancelable(false)
                                        create()
                                    }.show()

                                    Handler(Looper.getMainLooper()).postDelayed({
                                        alertDialog.dismiss()
                                        if (findNavController().currentDestination?.id == R.id.authenticationFragment) {
                                            val action =
                                                AuthenticationFragmentDirections.actionAuthenticationFragmentToUserInformationFragment()
                                            findNavController().navigate(action)
                                        }
                                    }, 2000)

                                }
                            }
                        }
                    }
                } else {
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
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}