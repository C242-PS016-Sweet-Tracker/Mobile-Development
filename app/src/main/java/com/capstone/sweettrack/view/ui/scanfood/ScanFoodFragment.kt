package com.capstone.sweettrack.view.ui.scanfood

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.capstone.sweettrack.data.remote.response.OcrResponse
import com.capstone.sweettrack.data.remote.response.ResponseModel
import com.capstone.sweettrack.util.getImageUri
import com.capstone.sweettrack.view.ViewModelFactory
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.FragmentScanFoodBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop

class ScanFoodFragment : Fragment() {

    private var _binding: FragmentScanFoodBinding? = null
    private val binding get() = _binding!!

    private var imageUriLast: Uri? = null

    private val viewModel by viewModels<ScanFoodViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        binding.scanFoodButton.setOnClickListener { analysisImageFood() }
        binding.ocrScanBtn.setOnClickListener { analysisImageOcr() }
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

    private fun analysisImageFood() {
        val currentUri = viewModel.currentImageUri.value
        if (currentUri != null) {
            Toast.makeText(requireContext(), "Analyzing image...", Toast.LENGTH_SHORT).show()

            viewModel.scanFoodNutrition(currentUri, requireActivity())
            observeScanFoodResult()
        } else {
            Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun analysisImageOcr() {
        val currentUri = viewModel.currentImageUri.value
        if (currentUri != null) {
            Toast.makeText(requireContext(), "Analyzing image...", Toast.LENGTH_SHORT).show()

            viewModel.scanOcrNutrition(currentUri, requireActivity())
            observeOcrScanResult()
        } else {
            Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun observeOcrScanResult() {
        viewModel.ocrScanResult.observe(viewLifecycleOwner) { result ->
            if (!result.error) {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Informasi")
                    setMessage(result.message)
                    setCancelable(false)
                    create()
                }.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog.dismiss()

                    val currentUri = viewModel.currentImageUri.value
                    if (currentUri != null) {
                        moveToResultOcr(currentUri, result)
                    }
                }, 1000)
            } else {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Infomasi")
                    setMessage(result.message)
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

    private fun observeScanFoodResult() {
        viewModel.scanFoodResult.observe(viewLifecycleOwner) { result ->
            if (!result.error) {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Informasi")
                    setMessage(result.message)
                    setCancelable(false)
                    create()
                }.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    alertDialog.dismiss()

                    val currentUri = viewModel.currentImageUri.value
                    if (currentUri != null) {
                        moveToResultScanFood(currentUri, result)
                    }
                }, 1000)
            } else {
                val alertDialog = AlertDialog.Builder(requireActivity()).apply {
                    setTitle("Infomasi")
                    setMessage(result.message)
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

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                Toast.makeText(requireActivity(), "Permission request granted", Toast.LENGTH_LONG)
                    .show()
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

    private fun moveToResultOcr(uri: Uri?, response: OcrResponse?) {
        if (uri == null || response == null) {
            Toast.makeText(requireContext(), "Data tidak valid untuk OCR", Toast.LENGTH_SHORT)
                .show()
            return
        }

        try {
            val bundle = Bundle().apply {
                putString("image_uri", uri.toString())
                putParcelable("result", response)
            }
            if (isAdded) {
                findNavController().navigate(
                    R.id.action_scanFoodFragment_to_resultOcrFragment,
                    bundle
                )
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Terjadi kesalahan saat navigasi", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun moveToResultScanFood(uri: Uri?, response: ResponseModel?) {
        if (uri == null || response == null) {
            Toast.makeText(
                requireContext(),
                "Data tidak valid untuk hasil scan makanan",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            val bundle = Bundle().apply {
                putString("image_uri", uri.toString())
                putParcelable("result", response)
            }
            if (isAdded) {
                findNavController().navigate(
                    R.id.action_scanFoodFragment_to_resultScanFoodFragment,
                    bundle
                )
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Terjadi kesalahan saat navigasi", Toast.LENGTH_SHORT)
                .show()
        }
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

