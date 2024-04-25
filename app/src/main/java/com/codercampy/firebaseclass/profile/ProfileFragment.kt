package com.codercampy.firebaseclass.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codercampy.firebaseclass.MainActivity
import com.codercampy.firebaseclass.R
import com.codercampy.firebaseclass.databinding.FragmentProfileBinding
import com.codercampy.firebaseclass.util.FirebaseUserUtil
import com.codercampy.firebaseclass.util.SharedPref
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storageMetadata
import com.yalantis.ucrop.UCrop
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val sharedPref by lazy { SharedPref(requireContext()) }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val file = File(requireContext().cacheDir, "temp.png")
                UCrop.of(uri, Uri.fromFile(file))
                    .withAspectRatio(1f, 1f)
                    .start(requireContext(), this)
            } else {
                Log.e("PhotoPicker", "No media selected")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        binding.btnUploadPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSave.setOnClickListener {
            val name = binding.inputName.editText?.text?.toString()?.trim()
            if (!name.isNullOrEmpty()) {
                FirebaseUserUtil.updateUser(name) {
                    (requireActivity() as MainActivity).updateUser()
                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initViews() {
        sharedPref.getUser()?.let { user ->
            Glide.with(binding.ivProfile).load(user.photo).error(R.drawable.ic_profile)
                .into(binding.ivProfile)
            binding.inputName.editText?.setText(user.name)
        }
    }

    private fun uploadPhoto(uri: Uri) {
        val ref = Firebase.storage.getReference("images")
            .child("${Firebase.auth.currentUser?.uid}.png")
        val metadata = storageMetadata {
            contentType = "image/png"
        }
        binding.progressBar.visibility = View.VISIBLE
        val uploadTask = ref.putFile(uri, metadata)
        uploadTask.addOnCompleteListener {
            binding.progressBar.visibility = View.GONE
        }.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseUserUtil.updateUser(photo = task.result) {
                    (requireActivity() as MainActivity).updateUser()
                    Toast.makeText(
                        requireContext(),
                        "Photo uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Handle failures
                // ...
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data) ?: return
            Glide.with(binding.ivProfile).load(resultUri).into(binding.ivProfile)
//            val mimeType = MimeTypeMap.getSingleton()
//                .getExtensionFromMimeType(requireContext().contentResolver.getType(resultUri!!))
//            Log.e("MimeType", mimeType.toString())
            uploadPhoto(resultUri)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data)
            Toast.makeText(requireContext(), cropError?.message.toString(), Toast.LENGTH_SHORT)
                .show()
        }
    }

}