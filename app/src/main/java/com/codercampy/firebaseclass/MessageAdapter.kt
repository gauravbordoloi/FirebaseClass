package com.codercampy.firebaseclass

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.codercampy.firebaseclass.chat.ChatModel
import com.codercampy.firebaseclass.databinding.ItemChatMineBinding
import com.codercampy.firebaseclass.databinding.ItemChatOtherBinding
import com.codercampy.firebaseclass.users.UserModel
import com.codercampy.firebaseclass.util.formatTimestampForChat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessageAdapter(
    private val otherUser: UserModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val chats = arrayListOf<ChatModel>()

    private val ITEM_MINE = 1
    private val ITEM_OTHER = 2

    fun addChats(chats: List<ChatModel>) {
        val oldSize = chats.size
        this.chats.addAll(chats)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (chats[position].userId == Firebase.auth.currentUser?.uid) {
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

            if (position == 0 || chats[position - 1].userId != chat.userId) {
                holder.binding.ivImage.visibility = View.VISIBLE

                val avatar = AvatarGenerator.AvatarBuilder(holder.binding.root.context)
                    .setLabel(buildString {
                        otherUser.name?.split(" ")?.take(2)?.forEach {
                            append(it.getOrNull(0)?.uppercase())
                        }
                    }).setAvatarSize(120)
                    .setTextSize(30)
                    .toSquare()
                    .toCircle()
                    .setBackgroundColor(Color.RED)
                    .build()

                Glide.with(holder.binding.ivImage)
                    .load(otherUser.photo)
                    .error(avatar)
                    .into(holder.binding.ivImage)
            } else {
                holder.binding.ivImage.visibility = View.INVISIBLE
            }

            holder.bind(chat, otherUser)
        }
    }

}

class ChatOtherViewBinder(val binding: ItemChatOtherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: ChatModel, otherUser: UserModel) {
        binding.tvName.text = otherUser.name
        binding.tvMessage.text = chat.message
        binding.tvTime.text = formatTimestampForChat(chat.timestamp)

    }

}

class ChatMineViewBinder(val binding: ItemChatMineBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: ChatModel) {

        binding.tvMessage.text = chat.message
        binding.tvTime.text = formatTimestampForChat(chat.timestamp)

    }

}