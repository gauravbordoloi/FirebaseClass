package com.codercampy.firebaseclass.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codercampy.firebaseclass.databinding.FragmentUsersBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private val adapter: UserAdapter by lazy { UserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        Firebase.firestore.collection("users")
            .whereNotEqualTo("id", Firebase.auth.currentUser?.uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("REALTIME", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val users = mutableListOf<UserModel>()
                for (doc in value!!.documentChanges) {
                    if (doc.type == DocumentChange.Type.ADDED) {
                        val user = doc.document.toObject(UserModel::class.java)
                        users.add(user)
                    }
                }
                Log.e("adapter.itemCount", adapter.itemCount.toString())
                adapter.setUsers(users)
            }

    }

}