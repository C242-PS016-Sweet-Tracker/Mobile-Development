package com.capstone.sweettrack.view.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.sweettrack.adapter.RecommendationAdapter
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var tipe: String
    private lateinit var adapter: RecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecommendationAdapter { item ->
            val bundle = Bundle().apply { putParcelable("foodDetail", item) }
            findNavController().navigate(R.id.action_navigation_home_to_detailFragment, bundle)
        }

        setupView()
        setupObservers()
        setupAction()
        observerData()

        observeViewModel()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin && findNavController().currentDestination?.id != R.id.welcomeFragment) {
                val action = HomeFragmentDirections.actionNavigationHomeToWelcomeFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.errorMessage.observe(requireActivity()) { message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerData() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.dataResult.observe(viewLifecycleOwner) { result ->
            if (result.error != true) {
                val dataUser = result.data
                if (dataUser != null) {
                    binding.textHome.text = getString(R.string.hello, dataUser.username)
                    binding.tvCalorie.text =
                        getString(R.string.hasil_kalori, dataUser.kalori.toString())
                    binding.tvCalorieNow.text =
                        getString(R.string.kalori_now, dataUser.kalori_harian.toString())

                    tipe = dataUser.tipe_diabetes
                    viewModel.fetchRecommendations(dataUser.tipe_diabetes)


                    val totalKalori = dataUser.kalori  // Total kalori ideal/hari
                    val kaloriHarian = dataUser.kalori_harian  // Kalori yang sudah tercapai

                    val progress = if (totalKalori > 0) {
                        ((kaloriHarian.toFloat() / totalKalori) * 100).toInt()
                    } else {
                        0
                    }
                    binding.progressBarDeterminate.progress = progress


                } else {
                    Toast.makeText(
                        requireContext(),
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    result.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupView() {
        val window = requireActivity().window
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_scanFoodFragment)
        }
        binding.btnCalculate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_calculatorFragmentOne)
        }
        binding.btnFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_favoriteFragment)
        }

        binding.tvSeeAll.setOnClickListener {
            val bundle = Bundle().apply {
                putString("type", tipe)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_recomendationFragment,
                bundle
            )
        }

        binding.btnGemini.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_chatFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendRecyclerView.layoutManager = layoutManager
        binding.recommendRecyclerView.adapter = adapter
    }


    private fun observeViewModel() {
        viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            recommendations?.let {
                val limitedRecommendations = it.take(3)
                adapter.setRecommendations(limitedRecommendations)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}