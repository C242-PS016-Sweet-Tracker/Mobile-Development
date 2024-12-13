package com.capstone.sweettrack.view.ui.calculatorcalori.result

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.view.ViewModelFactory
import com.capstone.sweettrack.view.ui.calculatorcalori.CalculatorViewModel
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentCalculatorResultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class CalculatorFragmentResult : Fragment() {

    private var _binding: FragmentCalculatorResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CalculatorViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }


    private var kalori by Delegates.notNull<Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.calculateCalories()

        setupAction()

        viewModel.calorieResult.observe(viewLifecycleOwner) { result ->
            binding.textHasil.text = getString(R.string.hasil_kalori, result.toString())
            kalori = result.toDouble()
        }
    }

    private fun saveUserCalorie() {

        viewModel.saveUserCalorie(kalori)
        observeSaveUserCalorieResult()

    }

    private fun observeSaveUserCalorieResult() {

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            println(result)
            if (!result.error) {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle(result.message)
                    setMessage(result.describe)
                    setCancelable(false)
                    create()
                }.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog.dismiss()
                    val action = CalculatorFragmentResultDirections.actionCalculatorFragmentResultToNavigationHome()
                    findNavController().navigate(action)
                }, 1000)
            } else {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle(result.message)
                    setMessage(result.describe)
                    setCancelable(false)
                    create()
                }.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog.dismiss()
                }, 2000)
            }
        }
    }

    private fun setupAction() {
        binding.sendButton.setOnClickListener {
            saveUserCalorie()
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
