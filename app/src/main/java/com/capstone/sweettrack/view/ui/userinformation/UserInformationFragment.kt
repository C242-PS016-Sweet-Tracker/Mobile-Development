package com.capstone.sweettrack.view.ui.userinformation

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentUserInformationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserInformationFragment : Fragment() {

    private var _binding: FragmentUserInformationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserInformationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGenderSpinner()
        setupActivityLevelSpinner()
        setupDiabetesTypeSpinner()
        setupSendButton()
        observeViewModel()
        setupView()
    }

    private fun setupGenderSpinner() {
        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_types,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = genderAdapter
    }

    private fun setupActivityLevelSpinner() {
        val activityLevelAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.activity_levels,
            android.R.layout.simple_spinner_item
        )
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.activityLevelSpinner.adapter = activityLevelAdapter
    }

    private fun setupDiabetesTypeSpinner() {
        val diabetesTypeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.diabetes_types,
            android.R.layout.simple_spinner_item
        )
        diabetesTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.diabetesTypeSpinner.adapter = diabetesTypeAdapter
    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val age = binding.ageEditText.text.toString().toIntOrNull()
            val gender = binding.genderSpinner.selectedItem.toString()
            val height = binding.heightEditText.text.toString().toDoubleOrNull()
            val weight = binding.weightEditText.text.toString().toDoubleOrNull()
            val activityLevel = binding.activityLevelSpinner.selectedItem.toString()
            val diabetesType = binding.diabetesTypeSpinner.selectedItem.toString()
            val lastBloodSugar = binding.lastBloodSugarEditText.text.toString().toDoubleOrNull()

            if (validateInputs(name, age, height, weight, lastBloodSugar)) {
                viewModel.submitUserData(
                    name,
                    age!!,
                    gender,
                    height!!,
                    weight!!,
                    activityLevel,
                    diabetesType,
                    lastBloodSugar!!
                )
            } else {
                Toast.makeText(requireContext(), "Harap isi semua data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_userInformationFragment_to_navigation_home)

            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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

    private fun validateInputs(
        name: String,
        age: Int?,
        height: Double?,
        weight: Double?,
        lastBloodSugar: Double?
    ): Boolean {
        return name.isNotBlank() && age != null && height != null && weight != null && lastBloodSugar != null
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
