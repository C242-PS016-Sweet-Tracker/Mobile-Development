package com.capstone.sweettrack.view.ui.detail

import DetailViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel
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

        // Observe status favorite untuk memperbarui ikon
        viewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            if (isFavorite) {
                binding.favoriteIcon.setImageResource(R.drawable.baseline_favorite_24) // Ikon terisi
            } else {
                binding.favoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24) // Ikon border
            }
        })

// Klik ikon favorite untuk toggle status
        binding.favoriteIcon.setOnClickListener {
            viewModel.toggleFavorite()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
