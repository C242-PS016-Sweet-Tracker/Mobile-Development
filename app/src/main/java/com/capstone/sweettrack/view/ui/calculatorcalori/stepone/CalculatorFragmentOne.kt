package com.capstone.sweettrack.view.ui.calculatorcalori.stepone

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.sweettrack.view.ui.calculatorcalori.CalculatorViewModel
import com.coding.sweettrack.R

class CalculatorFragmentOne : Fragment() {

    companion object {
        fun newInstance() = CalculatorFragmentOne()
    }

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_calculator_one, container, false)
    }
}