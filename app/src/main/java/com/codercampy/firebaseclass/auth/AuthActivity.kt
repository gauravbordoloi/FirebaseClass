package com.codercampy.firebaseclass.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codercampy.firebaseclass.MainActivity
import com.codercampy.firebaseclass.databinding.ActivityAuthBinding
import com.codercampy.firebaseclass.util.FirebaseUserUtil
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        if (Firebase.auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    fun signInWithPhoneAuthCredential(name: String, credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseUserUtil.updateUser(name) {
                    Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                    goToHome()
                }
            } else {
                val error = task.exception?.message
                Log.e("signIn", "signIn", task.exception)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}