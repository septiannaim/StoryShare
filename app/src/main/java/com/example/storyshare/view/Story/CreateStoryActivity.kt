package com.example.storyshare.view.Story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyshare.databinding.ActivityUploadBinding
import com.example.storyshare.response.UploadStoryResponse
import com.example.storyshare.utils.Outcome
import com.example.storyshare.utils.ViewModelFactory
import com.example.storyshare.utils.adjustBitmapOrientation
import com.example.storyshare.utils.convertUriToFile
import com.example.storyshare.utils.optimizeFileImage
import com.example.storyshare.view.Camera.CameraActivity
import com.example.storyshare.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUploadBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CreateStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var selectedImageFile: File? = null
    private var previewBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkPermissions()

        binding.btnGallery.setOnClickListener { openGallery() }
        binding.btnCamera.setOnClickListener { startCameraIfPermissionsGranted() }
        binding.btnUpload.setOnClickListener { uploadStory() }
    }

    private fun checkPermissions() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        }
    }

    private fun startCameraIfPermissionsGranted() {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraIntent = Intent(this, CameraActivity::class.java)
        cameraActivityResultLauncher.launch(cameraIntent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT_CODE) {
            val imageFile = it.data?.getSerializableExtra("picture") as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) ?: true

            imageFile?.let { file ->
                selectedImageFile = file
                previewBitmap = adjustBitmapOrientation(BitmapFactory.decodeFile(file.path), isBackCamera)
                binding.previewImageView.setImageBitmap(previewBitmap)
            } ?: Toast.makeText(this, "Error capturing photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadStory() {
        viewModel.retrieveUser().observe(this) { user ->
            val token = "Bearer ${user.token}"
            selectedImageFile?.let { file ->
                val reducedFile = optimizeFileImage(file)
                val description = binding.edtDescription.text.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
                val imageBody = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart =
                    MultipartBody.Part.createFormData("photo", reducedFile.name, imageBody)

                viewModel.submitStory(token, imagePart, description).observe(this) { result ->
                    handleUploadResult(result)
                }
            } ?: Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUploadResult(result: Outcome<UploadStoryResponse>) {
        when (result) {
            is Outcome.Success -> {
                showLoading(false)
                Toast.makeText(this, "Story uploaded successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            is Outcome.InProgress -> showLoading(true)
            is Outcome.Failure -> {
                showLoading(false)
                Toast.makeText(
                    this,
                    "Failed to upload story: ${result.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        galleryActivityResultLauncher.launch(
            Intent.createChooser(galleryIntent, "Choose a Picture")
        )
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri = result.data?.data as? Uri ?: return@registerForActivityResult
            val file = convertUriToFile(selectedImageUri, this@CreateStoryActivity)
            selectedImageFile = file
            binding.previewImageView.setImageURI(selectedImageUri)
        }
    }

    private fun permissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!permissionsGranted()) {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_LONG).show()
            } else {
                startCamera()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT_CODE = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
