package com.codercampy.firebaseclass.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatModel(
    val id: String = "",
    val userId: String = "",
    val timestamp: Long = 0,
    val message: String = "",
) : Parcelable