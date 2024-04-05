package com.codercampy.firebaseclass

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codercampy.firebaseclass.auth.AuthActivity
import com.codercampy.firebaseclass.databinding.FragmentHomeBinding
import com.codercampy.firebaseclass.util.formatTimestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser

        binding.tvTitle.text = """
            Name - ${user?.displayName}
            Phone - ${user?.phoneNumber}
            Last Logged In - ${formatTimestamp(user?.metadata?.lastSignInTimestamp)}
        """.trimIndent()

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }

    }

}