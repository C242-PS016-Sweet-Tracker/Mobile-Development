package com.capstone.sweettrack.view.ui.setting

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.reminderEat.observe(viewLifecycleOwner) {
            binding.switchReminderEat.isChecked = it
        }

        viewModel.reminderHealth.observe(viewLifecycleOwner) {
            binding.switchReminderHealth.isChecked = it
        }


        binding.switchReminderEat.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setReminderEat(isChecked)
        }

        binding.switchReminderHealth.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setReminderHealth(isChecked)
        }


        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}