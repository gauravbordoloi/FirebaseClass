package com.codercampy.firebaseclass.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codercampy.firebaseclass.databinding.FragmentPhoneNumberLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class PhoneNumberLoginFragment : Fragment() {

    private lateinit var binding: FragmentPhoneNumberLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneNumberLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {

            val phone = binding.inputPhone.editText?.text?.toString()?.trim()

            if (phone?.length == 10) {

                val options = PhoneAuthOptions.newBuilder(Firebase.auth)
                    .setPhoneNumber("+91" + phone) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity())
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                            signInWithPhoneAuthCredential(p0)
                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            Log.e("onVerificationFailed", "onVerificationFailed", p0)
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            p1: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(verificationId, p1)
                            Toast.makeText(requireContext(), "OTP Sent", Toast.LENGTH_SHORT).show()

                            findNavController().navigate(PhoneNumberLoginFragmentDirections.actionPhoneNumberLoginFragmentToOtpFragment(verificationId))

                        }
                    })
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

            }

        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                Toast.makeText(requireContext(), "User logged in", Toast.LENGTH_SHORT).show()
            } else {
                val error = task.exception?.message
                Log.e("signIn", "signIn", task.exception)
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }


}