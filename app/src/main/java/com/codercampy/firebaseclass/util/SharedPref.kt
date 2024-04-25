package com.codercampy.firebaseclass.util

import android.content.Context
import com.codercampy.firebaseclass.users.UserModel
import com.google.gson.Gson

class SharedPref(context: Context) {

    private val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()
    private val gson = Gson()

    fun setUser(userModel: UserModel) {
        editor.putString("USER", gson.toJson(userModel))
        editor.apply()
    }

    fun getUser(): UserModel? {
        return try {
            gson.fromJson(sharedPref.getString("USER", ""), UserModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

}