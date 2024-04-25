package com.codercampy.firebaseclass.conversation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codercampy.firebaseclass.MessageAdapter
import com.codercampy.firebaseclass.chat.ChatModel
import com.codercampy.firebaseclass.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ConversationAdapter

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

        adapter = ConversationAdapter()
        adapter.setListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToConversationChatFragment(it))
        }
        binding.recyclerView.adapter = adapter

        //Get realtime updates
        Firebase.firestore.collection("conversations")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("REALTIME", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val conversationMutableList = mutableListOf<Conversation>()
                for (doc in value!!.documentChanges) {
                    if (doc.type == DocumentChange.Type.ADDED) {
                        val chat = doc.document.toObject(Conversation::class.java)
                        conversationMutableList.add(chat)
                    }
                }
                Log.e("adapter.itemCount", adapter.itemCount.toString())
                adapter.setConversations(conversationMutableList)
            }


    }

}