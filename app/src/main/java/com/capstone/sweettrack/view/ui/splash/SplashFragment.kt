package com.capstone.sweettrack.view.ui.splash

import android.os.Build
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentSplashBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
//    private val binding get() = _binding!!

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Handler(Looper.getMainLooper()).postDelayed({
            setupObservers()
//        findNavController().navigate(R.id.action_splashFragment_to_navigation_home)

        }, 2000)
    }

    private fun setupObservers() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin && findNavController().currentDestination?.id != R.id.welcomeFragment) {
                val action = SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
                findNavController().navigate(action)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_navigation_home)
                }, 2000)

//                val action = SplashFragmentDirections.actionSplashFragmentToNavigationHome()
//                findNavController().navigate(action)
            }
        }

        viewModel.errorMessage.observe(requireActivity()) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
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

//    override fun onResume() {
//        super.onResume()
//        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
//        bottomNavigationView?.visibility = View.INVISIBLE
//    }
//
//    override fun onPause() {
//        super.onPause()
//        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
//        bottomNavigationView?.visibility = View.GONE
//    }

    override fun onStart() {
        super.onStart()
        toggleBottomNavVisibility(View.GONE)
    }

    override fun onStop() {
        super.onStop()
        toggleBottomNavVisibility(View.VISIBLE)
    }

    private fun toggleBottomNavVisibility(visibility: Int) {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
