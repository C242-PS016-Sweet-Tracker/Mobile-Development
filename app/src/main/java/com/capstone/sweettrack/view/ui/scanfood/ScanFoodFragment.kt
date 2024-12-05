package com.capstone.sweettrack.view.ui.scanfood

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import com.capstone.sweettrack.util.getImageUri
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentScanFoodBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop

class ScanFoodFragment : Fragment() {

    private var _binding: FragmentScanFoodBinding? = null
    private val binding get() = _binding!!

    private var imageUriLast: Uri? = null

    private val viewModel: ScanFoodViewModel by viewModels()

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        setupObservers()
        setupView()

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { analysisImage() }
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

    private fun setupObservers() {
        viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.previewImageView.setImageURI(uri)
                Log.d("ScanFoodFragment", "Displayed URI: $uri")
            } else {
                binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
            }
        }
    }

    private fun analysisImage() {
        val currentUri = viewModel.currentImageUri.value
        if (currentUri != null) {
            Toast.makeText(requireContext(), "Analyzing image...", Toast.LENGTH_SHORT).show()
            // Logic for analyzing image
        } else {
            Toast.makeText(requireContext(), "No image to analyze", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        imageUriLast = viewModel.currentImageUri.value
        getImageUri(requireActivity()).let { uri ->
            viewModel.setResultData(uri)
            launcherIntentCamera.launch(uri)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun startCrop(uri: Uri) {
        val cropIntent = UCrop.of(uri, viewModel.getCropDestinationUri(requireContext()))
            .withAspectRatio(1f, 1f)
            .getIntent(requireContext())
        cropImageLauncher.launch(cropIntent)
    }

    private val cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.let { data ->
                viewModel.handleCropResult(data)
                showImage()
            }
        } else if (result.resultCode == AppCompatActivity.RESULT_CANCELED) {
            viewModel.resetToLastSuccessfulImageUri()
            showToast("Crop Gambar dibatalkan")
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            Log.e("UCrop Error", cropError?.message.toString())
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri.value?.let { startCrop(it) }
        } else {
            viewModel.setResultData(imageUriLast)
            imageUriLast?.let { showImage() }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setCurrentImageUri(it)
            startCrop(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private fun showImage() {
        viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
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

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireActivity(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                showPermissionDeniedDialog()
            }
        }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Camera Permission Denied")
            .setMessage("Please enable camera permission in settings to use this feature.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}

