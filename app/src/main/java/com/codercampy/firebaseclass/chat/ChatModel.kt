package com.codercampy.firebaseclass.chat

data class ChatModel(
    val id: String = "",
    val timestamp: Long = 0,
    val message: String = "",
    val user: Map<String, String?> = emptyMap()
)