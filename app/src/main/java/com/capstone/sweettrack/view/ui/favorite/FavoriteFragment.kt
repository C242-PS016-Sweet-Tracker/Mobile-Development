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
import com.capstone.sweettrack.adapter.HistoryAdapter
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentDetailBinding
import com.coding.sweettrack.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

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

        favoriteAdapter = FavoriteAdapter()
        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

//        viewModel.favoriteList.observe(viewLifecycleOwner) { favoriteList ->
//            favoriteAdapter.submitList(favoriteList)
//        }

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
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

    override fun onDestroy() {
        super.onDestroy()
    }
}