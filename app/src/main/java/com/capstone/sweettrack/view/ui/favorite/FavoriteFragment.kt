package com.capstone.sweettrack.view.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.sweettrack.adapter.FavoriteAdapter
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observerData() {
        viewModel.getFavorite()

        favoriteAdapter = FavoriteAdapter { favorite ->
            val bundle = Bundle().apply { putParcelable("foodDetail", favorite) }
            findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment2, bundle)
        }

        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        // Observe favoriteList
        viewModel.favoriteList.observe(viewLifecycleOwner) { favoriteList ->
            favoriteAdapter.setFavorites(favoriteList)
        }

        // Observe isLoading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        bottomNavigationView?.visibility = View.VISIBLE
    }

}
