package com.codercampy.firebaseclass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codercampy.firebaseclass.chat.ChatModel
import com.codercampy.firebaseclass.databinding.ItemChatMineBinding
import com.codercampy.firebaseclass.databinding.ItemChatOtherBinding
import com.codercampy.firebaseclass.util.formatTimestampForChat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val chats = arrayListOf<ChatModel>()

    private val ITEM_MINE = 1
    private val ITEM_OTHER = 2

    fun addChats(chats: List<ChatModel>) {
        val oldSize = chats.size
        this.chats.addAll(chats)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (chats[position].user["id"] == Firebase.auth.currentUser?.uid) {
            ITEM_MINE
        } else {
            ITEM_OTHER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_MINE) {
            ChatMineViewBinder(
                ItemChatMineBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            ChatOtherViewBinder(
                ItemChatOtherBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChatMineViewBinder) {
            holder.bind(chats[position])
        } else if (holder is ChatOtherViewBinder) {
            val chat = chats[position]

            if (position ==  0 || chats[position - 1].user["id"] != chat.user["id"]) {
                holder.binding.tvInitial.visibility = View.VISIBLE
                holder.binding.tvInitial.text = buildString {
                    chat.user["name"]?.split(" ")?.take(2)?.forEach {
                        append(it.getOrNull(0)?.uppercase())
                    }
                }
            } else {
                holder.binding.tvInitial.visibility = View.INVISIBLE
            }

            holder.bind(chat)
        }
    }

}

class ChatOtherViewBinder(val binding: ItemChatOtherBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: ChatModel) {
        binding.tvName.text = chat.user["name"]
        binding.tvMessage.text = chat.message
        binding.tvTime.text = formatTimestampForChat(chat.timestamp)

    }

}

class ChatMineViewBinder(val binding: ItemChatMineBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: ChatModel) {

        binding.tvName.text = chat.user["name"]
        binding.tvMessage.text = chat.message
        binding.tvTime.text = formatTimestampForChat(chat.timestamp)

    }

}