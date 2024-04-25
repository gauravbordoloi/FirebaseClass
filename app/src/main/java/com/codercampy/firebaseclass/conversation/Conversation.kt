package com.codercampy.firebaseclass.conversation

import android.os.Parcelable
import com.codercampy.firebaseclass.chat.ChatModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Conversation(
    val id: String,
    val createdAt: Long,
    val user1: String,
    val user2: String,
    val chats: List<ChatModel>
) : Parcelable