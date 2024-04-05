package com.codercampy.firebaseclass.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codercampy.firebaseclass.MainActivity
import com.codercampy.firebaseclass.databinding.ActivityAuthBinding
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

}