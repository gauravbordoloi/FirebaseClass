package com.codercampy.firebaseclass.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codercampy.firebaseclass.MessageAdapter
import com.codercampy.firebaseclass.conversation.Conversation
import com.codercampy.firebaseclass.databinding.FragmentConversationChatBinding
import com.codercampy.firebaseclass.users.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConversationChatFragment : Fragment() {

    private lateinit var binding: FragmentConversationChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var conversation: Conversation
    private var otherUser: UserModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConversationChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser ?: return
        conversation = ConversationChatFragmentArgs.fromBundle(requireArguments()).extras

        if (conversation.user1 == user.uid) {
            fetchOtherUser(conversation.user2)
        } else {
            fetchOtherUser(conversation.user1)
        }

        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
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

    private fun listenMessages() {
        Firebase.firestore.collection("conversations")
            .document(conversation.id)
            .collection("chats")
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
    }

    private fun sendMessage() {
        val user = Firebase.auth.currentUser ?: return
        val text = binding.etChat.text?.toString()?.trim()
        if (text.isNullOrEmpty()) {
            return
        }

        val chat = ChatModel(
            System.currentTimeMillis().toString(),
            user.uid,
            System.currentTimeMillis(),
            text
        )

        Firebase.firestore.collection("conversations")
            .document(conversation.id)
            .collection("chats")
            .add(chat)
            .addOnSuccessListener { documentReference ->
                binding.etChat.text = null
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Unable to send message", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    private fun fetchOtherUser(userId: String) {
        Firebase.firestore.collection("users").document(userId).get()
            .addOnCompleteListener {
                otherUser = it.result.toObject(UserModel::class.java)
                binding.tvName.text = otherUser?.name
                Glide.with(binding.ivProfile).load(otherUser?.photo).into(binding.ivProfile)

                adapter = MessageAdapter(otherUser!!)
                binding.recyclerView.adapter = adapter

                listenMessages()
            }
    }

}