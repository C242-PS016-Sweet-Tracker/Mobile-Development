package com.capstone.sweettrack.view.ui.result.resultocr

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coding.sweettrack.R

class ResultOcrFragment : Fragment() {

    companion object {
        fun newInstance() = ResultOcrFragment()
    }

    private val viewModel: ResultOcrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_result_ocr, container, false)
    }
}