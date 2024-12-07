package com.capstone.sweettrack.view.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.coding.sweettrack.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            Log.d("SplashFragment", "Navigating to next screen")
            if (findNavController().currentDestination?.id == R.id.splashFragment) {
                val isLoggedIn = checkLoginStatus()
                val action = if (isLoggedIn) {
                    R.id.action_splashFragment_to_navigation_home
                } else {
                    R.id.action_splashFragment_to_welcomeFragment
                }
                findNavController().navigate(action)
            }
        }, 2000)

    }

    private fun checkLoginStatus(): Boolean {
        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
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

}
