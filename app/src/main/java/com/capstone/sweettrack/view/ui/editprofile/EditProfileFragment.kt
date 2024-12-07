package com.capstone.sweettrack.view.ui.editprofile

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.capstone.sweettrack.data.remote.response.EditProfileRequest
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentEditProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupGenderSpinner()
        observeData()
        startAction()
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.dataResult.observe(viewLifecycleOwner) { result ->
            if (!result.error) {
                val dataUser = result.data
                if (dataUser != null) {
                    binding.etFullName.setText(dataUser.nama_lengkap_user)
                    binding.etUsername.setText(dataUser.username)

                    val genderAdapter = binding.genderSpinner.adapter
                    val position = (0 until genderAdapter.count).firstOrNull {
                        genderAdapter.getItem(it).toString() == dataUser.jenis_kelamin
                    } ?: 0
                    binding.genderSpinner.setSelection(position)

                    binding.etAge.setText(dataUser.user_umur.toString())
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

    private fun observeEditProfileResult() {
        viewModel.updateResult.observe(viewLifecycleOwner) { result ->
            println(result)
            if (!result.error) {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Informasi")
                    setMessage(result.message)
                    setCancelable(false)
                    create()
                }.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog.dismiss()
                    findNavController().popBackStack()
                }, 1000)
            } else {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle(result.message)
                    setMessage(result.describe)
                    setCancelable(false)
                    create()
                }.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog.dismiss()
                }, 2000)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }


    private fun saveChanges() {
        val fullName = binding.etFullName.text.toString()
        val username = binding.etUsername.text.toString()
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val gender = binding.genderSpinner.selectedItem.toString()

        if (fullName.isEmpty() || username.isEmpty() || age == 0) {
            Toast.makeText(
                requireContext(),
                "Lengkapi semua data terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val request = EditProfileRequest(
            namaLengkap = fullName,
            username = username,
            jenisKelamin = gender,
            umur = age
        )

        viewModel.editProfileUser(request)
        observeEditProfileResult()
    }


    private fun startAction() {
        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }

        binding.profileImage.setOnClickListener {
            startGallery()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            showImage(it)
        }
    }

    private fun showImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.ic_place_holder)
            .into(binding.profileImage)
    }


    private fun setupView() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            show()
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

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

    private fun setupGenderSpinner() {
        val genderAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_types,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = genderAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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