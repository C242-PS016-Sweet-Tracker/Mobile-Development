package com.capstone.sweettrack.view.ui.history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.sweettrack.adapter.HistoryAdapter
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyAdapter = HistoryAdapter()
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        viewModel.getHistoryUser()

        viewModel.historyList.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isEmpty()) {
                binding.tvEmptyHistory.visibility = View.VISIBLE // Tampilkan teks "No History"
            } else {
                binding.tvEmptyHistory.visibility = View.GONE
                historyAdapter.submitList(historyList)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
