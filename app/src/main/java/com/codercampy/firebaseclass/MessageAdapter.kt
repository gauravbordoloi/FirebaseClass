package com.codercampy.firebaseclass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codercampy.firebaseclass.chat.ChatModel
import com.codercampy.firebaseclass.databinding.ItemChatBinding
import com.codercampy.firebaseclass.util.formatTimestampForChat

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {

    private val chats = arrayListOf<ChatModel>()

    fun addChats(chats: List<ChatModel>) {
        val oldSize = chats.size
        this.chats.addAll(chats)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(chats[position])
    }


}

class MessageViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: ChatModel) {

        binding.tvName.text = chat.user["name"]
        binding.tvMessage.text = chat.message
        binding.tvTime.text = formatTimestampForChat(chat.timestamp)

    }

}