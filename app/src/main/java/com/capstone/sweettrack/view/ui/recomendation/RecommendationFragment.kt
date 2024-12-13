package com.capstone.sweettrack.view.ui.recomendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.sweettrack.adapter.RecommendationAdapter
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentRecomendationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecommendationFragment : Fragment() {
    private var _binding: FragmentRecomendationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecommendationViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: RecommendationAdapter
    private var tipe: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecomendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipe = arguments?.getString("type")
        tipe?.let {
            viewModel.fetchRecommendations(tipe ?: "")
        }

        adapter = RecommendationAdapter { item ->
            val bundle = Bundle().apply { putParcelable("foodDetail", item) }
            findNavController().navigate(
                R.id.action_recomendationFragment_to_detailFragment,
                bundle
            )
        }

        setupView()
        setupRecyclerView()
        observeViewModel()

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
            binding.progressBarRecommendation.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            show()
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        findNavController().navigateUp()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
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