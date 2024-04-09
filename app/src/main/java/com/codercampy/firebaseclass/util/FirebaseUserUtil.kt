package com.codercampy.firebaseclass.util

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

object FirebaseUserUtil {

//    fun sumOfNumbers(a: Int, b: Int): Int {
//        //API Network call
//        //2s/3s
//        return a + b
//    }
//
//    fun sumOfNumbers1(a: Int, b: Int, onSuccess: (Boolean) -> Unit): Int {
//        //API Network call
//        //2s/3s
//        onSuccess(true)
//        return a + b
//    }

    fun updateUser(name: String, cb: (Boolean) -> Unit) {
        Firebase.auth.currentUser?.updateProfile(
            userProfileChangeRequest {
                displayName = name
                setPhotoUri(Uri.parse(""))
            }
        )?.addOnCompleteListener {
            cb(it.isSuccessful)
        }

    }

}