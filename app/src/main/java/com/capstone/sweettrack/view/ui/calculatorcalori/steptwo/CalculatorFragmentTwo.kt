package com.capstone.sweettrack.view.ui.calculatorcalori.steptwo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentCalculatorOneBinding
import com.coding.sweettrack.databinding.FragmentCalculatorTwoBinding


class CalculatorFragmentTwo : Fragment() {

    private var _binding: FragmentCalculatorTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}