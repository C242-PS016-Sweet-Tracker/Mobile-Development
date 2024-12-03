package com.capstone.sweettrack.view.ui.calculatorcalori.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.capstone.sweettrack.view.ui.calculatorcalori.CalculatorViewModel
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentCalculatorResultBinding
import com.coding.sweettrack.databinding.FragmentCalculatorTwoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalculatorFragmentResult : Fragment() {

    private var _binding: FragmentCalculatorResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CalculatorViewModel

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

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(requireActivity())[CalculatorViewModel::class.java]

        // Hitung kalori setelah data terinput
        viewModel.calculateCalories()

        // Observasi hasil
        viewModel.calorieResult.observe(viewLifecycleOwner) { result ->
            binding.textHasil.text = "$result kkal / hari"
        }
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
