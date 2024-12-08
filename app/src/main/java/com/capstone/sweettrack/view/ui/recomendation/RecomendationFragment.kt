package com.capstone.sweettrack.view.ui.recomendation

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentLoginBinding
import com.coding.sweettrack.databinding.FragmentRecomendationBinding

class RecomendationFragment : Fragment() {
    private var _binding: FragmentRecomendationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecomendationViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: RecomendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecomendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecomendationAdapter { item ->
            Toast.makeText(context, "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
            // Navigate to the detail page if needed
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchRecommendations()
    }

    private fun setupRecyclerView() {
        binding.rvRecommendation.layoutManager = LinearLayoutManager(context)
        binding.rvRecommendation.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            adapter.setRecommendations(recommendations)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarRecommendation.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}