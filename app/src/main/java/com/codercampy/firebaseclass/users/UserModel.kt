package com.codercampy.firebaseclass.users

data class UserModel(
    val id: String = "",
    val phone: String = "",
    val name: String? = "",
    val photo: String? = "",
    val createdAt: Long? = null
)