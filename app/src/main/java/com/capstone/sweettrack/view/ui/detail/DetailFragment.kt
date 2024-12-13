package com.capstone.sweettrack.view.ui.detail

import DetailViewModel
import android.os.Bundle
import android.os.Parcelable
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
import com.bumptech.glide.Glide
import com.capstone.sweettrack.data.remote.response.Favorite
import com.capstone.sweettrack.data.remote.response.FavoriteAdd
import com.capstone.sweettrack.data.remote.response.Recommendation
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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

        val foodDetail = arguments?.getParcelable<Parcelable>("foodDetail")
        foodDetail?.let {
            when (it) {
                is Favorite -> {
                    viewModel.checkIfFavorite(it.nama_makanan)
                    displayFavoriteDetails(it)
                }
                is Recommendation -> {
                    displayRecommendationDetails(it)
                }
            }
        }


        viewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            if (isFavorite) {
                binding.favoriteIcon.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                binding.favoriteIcon.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        })

        binding.favoriteIcon.setOnClickListener {
//            val foodDetail = arguments?.getParcelable<Parcelable>("foodDetail")

            when (foodDetail) {
                is Favorite -> {
                    if (viewModel.isFavorite.value == true) {
                            val favoriteAdd = FavoriteAdd(
                                user_id = 0,
                                namaMakanan = foodDetail.nama_makanan,
                                kalori = foodDetail.kalori,
                                karbohidrat = foodDetail.karbohidrat,
                                lemak = foodDetail.lemak,
                                protein = foodDetail.protein,
                                serat = foodDetail.serat,
                                img = foodDetail.img
                            )
                            viewModel.removeFavorite(favoriteAdd)
                    } else {
                        val favoriteAdd = FavoriteAdd(
                            user_id = 0,
                            namaMakanan = foodDetail.nama_makanan,
                            kalori = foodDetail.kalori,
                            karbohidrat = foodDetail.karbohidrat,
                            lemak = foodDetail.lemak,
                            protein = foodDetail.protein,
                            serat = foodDetail.serat,
                            img = foodDetail.img
                        )
                        viewModel.addFavorite(favoriteAdd)
                    }
                }

                is Recommendation -> {
                    val favoriteAdd = FavoriteAdd(
                        user_id = 0,
                        namaMakanan = foodDetail.nama_makanan,
                        kalori = foodDetail.kalori,
                        karbohidrat = foodDetail.karbohidrat,
                        lemak = foodDetail.lemak,
                        protein = foodDetail.protein,
                        serat = foodDetail.serat,
                        img = foodDetail.img
                    )
                    viewModel.addFavorite(favoriteAdd)
                }
            }
        }

//        val foodDetail = arguments?.getParcelable<Parcelable>("foodDetail")
//
//        when (foodDetail) {
//            is Favorite -> {
//                displayFavoriteDetails(foodDetail)
//            }
//
//            is Recommendation -> {
//                displayRecommendationDetails(foodDetail)
//            }
//        }
    }

    private fun displayFavoriteDetails(favorite: Favorite) {
        binding.tvName.text = favorite.nama_makanan
        binding.tvKalori.text = "Kalori: ${favorite.kalori}"
        binding.tvkarbohidrat.text = "Karbohidrat: ${favorite.karbohidrat}"
        binding.tvLemak.text = "Lemak: ${favorite.lemak}"
        binding.tvProtein.text = "Protein: ${favorite.protein}"
        binding.tvSerat.text = "Serat: ${favorite.serat}"

        Glide.with(requireContext())
            .load(favorite.img)
            .placeholder(R.drawable.ic_place_holder)
            .into(binding.imgFood)
    }

    private fun displayRecommendationDetails(recommendation: Recommendation) {
        binding.tvName.text = recommendation.nama_makanan
        binding.tvKalori.text = "Kalori: ${recommendation.kalori}"
        binding.tvkarbohidrat.text = "Karbohidrat: ${recommendation.karbohidrat}"
        binding.tvLemak.text = "Lemak: ${recommendation.lemak}"
        binding.tvProtein.text = "Protein: ${recommendation.protein}"
        binding.tvSerat.text = "Serat: ${recommendation.serat}"

        Glide.with(requireContext())
            .load(recommendation.img)
            .placeholder(R.drawable.ic_place_holder)
            .into(binding.imgFood)
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
