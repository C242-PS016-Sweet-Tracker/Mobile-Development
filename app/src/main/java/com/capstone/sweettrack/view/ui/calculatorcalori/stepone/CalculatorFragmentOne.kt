package com.capstone.sweettrack.view.ui.calculatorcalori.stepone

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.view.ui.calculatorcalori.CalculatorViewModel
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentCalculatorOneBinding
import com.coding.sweettrack.databinding.FragmentWelcomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalculatorFragmentOne : Fragment() {

    private var _binding: FragmentCalculatorOneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_types,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.genderSpinner.adapter = genderAdapter

        binding.sendButton.setOnClickListener {

            val age = binding.ageEditText.text.toString()
            val height = binding.heightEditText.text.toString()
            val weight = binding.weightEditText.text.toString()
            val gender = binding.genderSpinner.selectedItem.toString()


            if (age.isEmpty() || height.isEmpty() || weight.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.age.value = age.toInt()
            viewModel.height.value = height.toDouble()
            viewModel.weight.value = weight.toDouble()
            viewModel.gender.value = gender

            findNavController().navigate(R.id.action_calculatorFragmentOne_to_calculatorFragmentTwo)
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
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
