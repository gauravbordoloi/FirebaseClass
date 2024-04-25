package com.codercampy.firebaseclass.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codercampy.firebaseclass.chat.ChatModel
import com.codercampy.firebaseclass.databinding.ItemConversationBinding
import com.codercampy.firebaseclass.databinding.ItemUserBinding
import com.codercampy.firebaseclass.users.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConversationAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val conversations: ArrayList<Conversation> = ArrayList()
    private lateinit var listener: ConversationAdapterListener

    fun setConversations(conversations: List<Conversation>) {
        this.conversations.addAll(conversations)
        notifyDataSetChanged()
    }

    fun setListener(listener: ConversationAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(conversations[position], position)
    }


}

class ViewHolder(val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(conversation: Conversation, position: Int) {
        val meUser = Firebase.auth.currentUser

        binding.tvMessage.text = ""
        binding.tvName.text = ""

        Firebase.firestore.collection("conversations")
            .document(conversation.id)
            .collection("chats")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val lastChat = it.result.toObjects(ChatModel::class.java).firstOrNull()
                    binding.tvMessage.text = lastChat?.message
                }
            }

        Firebase.firestore.collection("users")
            .document(if (meUser?.uid == conversation.user1) conversation.user2 else conversation.user1)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(UserModel::class.java)
                    binding.tvName.text = user?.name
                    Glide.with(binding.ivImage).load(user?.photo).into(binding.ivImage)
                }
            }
    }

}

fun interface ConversationAdapterListener {

    fun onConversationClicked(conversation: Conversation)

}