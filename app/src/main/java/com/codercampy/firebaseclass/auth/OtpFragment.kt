package com.codercampy.firebaseclass.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codercampy.firebaseclass.MainActivity
import com.codercampy.firebaseclass.databinding.FragmentOtpBinding
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OtpFragment: Fragment() {

    private lateinit var binding: FragmentOtpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val verificationId = OtpFragmentArgs.fromBundle(requireArguments()).verificationId

        binding.btnVerify.setOnClickListener {

            val otp = binding.etOtp.text?.toString()?.trim()

            if (otp.isNullOrEmpty() || otp.length != 6) {
                Toast.makeText(requireContext(), "Wrong OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            signInWithPhoneAuthCredential(credential)

        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                Toast.makeText(requireContext(), "User logged in", Toast.LENGTH_SHORT).show()

                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()

            } else {
                val error = task.exception?.message
                Log.e("signIn", "signIn", task.exception)
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

}