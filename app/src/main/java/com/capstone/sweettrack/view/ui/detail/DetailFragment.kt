package com.capstone.sweettrack.view.ui.detail

import DetailViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.data.local.entity.FavoriteFood
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        viewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            if (isFavorite) {
                binding.favoriteIcon.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.favoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        })

        binding.favoriteIcon.setOnClickListener {
            viewModel.toggleFavorite()
        }

        val favoriteFood = arguments?.getParcelable<FavoriteFood>("foodDetail")
        favoriteFood?.let { food ->
            binding.tvName.text = food.name
            binding.tvKalori.text = "Kalori: ${food.kalori}"
            binding.tvGula.text = "Gula: ${food.gula}"
            binding.tvLemak.text = "Lemak: ${food.lemak}"
            binding.tvProtein.text = "Protein: ${food.protein}"
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
