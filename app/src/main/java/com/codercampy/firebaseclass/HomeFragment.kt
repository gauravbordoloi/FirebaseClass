package com.codercampy.firebaseclass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codercampy.firebaseclass.chat.ChatModel
import com.codercampy.firebaseclass.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MessageAdapter

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

        adapter = MessageAdapter()
        binding.recyclerView.adapter = adapter

        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        // Get data only once
//        Firebase.firestore.collection("chats")
//            .get()
//            .addOnSuccessListener { result ->
//                val chats = mutableListOf<ChatModel>()
//                for (document in result) {
//                    val chat = document.toObject(ChatModel::class.java)
//                    chats.add(chat)
//                }
//                adapter.addChats(chats)
//            }
//            .addOnFailureListener { exception ->
//                Log.e("Chats", "Error getting documents.", exception)
//            }

        //Get realtime updates
        Firebase.firestore.collection("chats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("REALTIME", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val chats = mutableListOf<ChatModel>()
                for (doc in value!!.documentChanges) {
                    if (doc.type == DocumentChange.Type.ADDED) {
                        val chat = doc.document.toObject(ChatModel::class.java)
                        chats.add(chat)
                    }
                }
                Log.e("adapter.itemCount", adapter.itemCount.toString())
                adapter.addChats(chats)
                binding.recyclerView.postDelayed({
                    Log.e("adapter.itemCount", adapter.itemCount.toString())
                    binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                }, 100)
            }


//        binding.tvTitle.text = """
//            Name - ${user?.displayName}
//            Phone - ${user?.phoneNumber}
//            Last Logged In - ${formatTimestamp(user?.metadata?.lastSignInTimestamp)}
//        """.trimIndent()
//
//        binding.btnLogout.setOnClickListener {
//            Firebase.auth.signOut()
//            startActivity(Intent(requireContext(), AuthActivity::class.java))
//            requireActivity().finish()
//        }

    }

    private fun sendMessage() {
        val user = Firebase.auth.currentUser ?: return
        val text = binding.etChat.text?.toString()?.trim()
        if (text.isNullOrEmpty()) {
            return
        }

        val chat = ChatModel(
            System.currentTimeMillis().toString(),
            System.currentTimeMillis(),
            text,
            buildMap {
                put("id", user.uid)
                put("name", user.displayName)
                put("image", user.photoUrl?.toString())
            }
        )

        Firebase.firestore.collection("chats")
            .add(chat)
            .addOnSuccessListener { documentReference ->
                binding.etChat.text = null
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Unable to send message", Toast.LENGTH_SHORT)
                    .show()
            }

    }

}