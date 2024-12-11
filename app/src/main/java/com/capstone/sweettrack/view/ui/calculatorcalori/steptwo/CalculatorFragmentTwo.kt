package com.capstone.sweettrack.view.ui.calculatorcalori.steptwo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.sweettrack.adapter.ActivityLevelAdapter
import com.capstone.sweettrack.view.ViewModelFactory
import com.capstone.sweettrack.view.ui.calculatorcalori.CalculatorViewModel
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentCalculatorTwoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class CalculatorFragmentTwo : Fragment() {

    private var _binding: FragmentCalculatorTwoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CalculatorViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }


    private val activityLevels = listOf(
        "Tidak aktif" to "Sedikit atau tidak ada aktivitas fisik.",
        "Ringan" to "Aktivitas fisik ringan sehari-hari.",
        "Sedang" to "Olahraga ringan-moderat 3-5 hari per minggu.",
        "Aktif" to "Olahraga berat 6-7 hari per minggu.",
        "Sangat aktif" to "Aktivitas fisik atau pekerjaan fisik sangat berat."
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        binding.activityLevelRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ActivityLevelAdapter(activityLevels) { selectedLevel ->

                viewModel.setSelectedActivityLevel(selectedLevel)
                Toast.makeText(context, "Dipilih: $selectedLevel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressBar.visibility = View.GONE

                findNavController().navigate(R.id.action_calculatorFragmentTwo_to_calculatorFragmentResult)
            }, 1500) // Simulasi delay 1,5 detik
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

