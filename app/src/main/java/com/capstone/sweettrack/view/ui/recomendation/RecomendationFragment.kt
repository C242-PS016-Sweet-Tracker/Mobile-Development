package com.capstone.sweettrack.view.ui.recomendation

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.sweettrack.data.response.ListEventsItem
import com.capstone.sweettrack.databinding.FragmentRecommendationBinding
import com.capstone.sweettrack.ui.adapter.EventAdapter
import com.capstone.sweettrack.ui.detail.DetailActivity
import com.capstone.sweettrack.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.coding.sweettrack.databinding.FragmentRecomendationBinding

class RecomendationFragment : Fragment() {

    private var _binding: FragmentRecomendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendationViewModel: RecomendationViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recommendationViewModel = ViewModelProvider(this).get(RecomendationViewModel::class.java)

        _binding = FragmentRecomendationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rvRecommendation
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        adapter = EventAdapter { event -> onEventClick(event) }
        recyclerView.adapter = adapter

        // observe loading
        recommendationViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarRecommendation.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // observe listEventsItem
        recommendationViewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("RecommendationFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }

        return root
    }

    private fun onEventClick(event: ListEventsItem) {
        Log.d("RecommendationFragmentClickTest", "Event clicked: ${event.id}")
        event.id?.let { id ->
            Log.d("RecommendationFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
            val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id.toString())
            }
            startActivity(intentToDetail)
        } ?: run {
            Log.e("RecommendationFragmentClickTest", "Event ID is null")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}