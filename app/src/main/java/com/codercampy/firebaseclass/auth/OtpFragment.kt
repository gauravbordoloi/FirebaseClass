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
        val name = OtpFragmentArgs.fromBundle(requireArguments()).name

        binding.btnVerify.setOnClickListener {

            val otp = binding.etOtp.text?.toString()?.trim()

            if (otp.isNullOrEmpty() || otp.length != 6) {
                Toast.makeText(requireContext(), "Wrong OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            (requireActivity() as AuthActivity).signInWithPhoneAuthCredential(name, credential)

        }

    }

}