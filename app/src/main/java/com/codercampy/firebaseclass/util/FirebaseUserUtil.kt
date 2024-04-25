package com.codercampy.firebaseclass.util

import android.net.Uri
import com.codercampy.firebaseclass.users.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
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

    fun updateUser(userModel: UserModel, cb: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser ?: return
        Firebase.firestore
            .collection("users")
            .document(user.uid)
            .set(
                userModel,
                SetOptions.merge()
            )
            .addOnCompleteListener {
                cb(it.isSuccessful)
            }
    }

    fun updateUser(name: String? = null, photo: Uri? = null, cb: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser ?: return
        Firebase.firestore
            .collection("users")
            .document(user.uid)
            .set(
                buildMap<String, Any> {
                    if (!name.isNullOrEmpty()) {
                        put("name", name)
                    }
                    if (photo != null) {
                        put("photo", photo.toString())
                    }
                },
                SetOptions.merge()
            )
            .addOnCompleteListener {
                cb(it.isSuccessful)
            }
//        Firebase.auth.currentUser?.updateProfile(
//            userProfileChangeRequest {
//                if (!name.isNullOrEmpty()) {
//                    displayName = name
//                }
//                if (photo != null) {
//                    photoUri = photo
//                }
//            }
//        )?.addOnCompleteListener {
//            cb(it.isSuccessful)
//
//            Firebase.firestore.collection("users")
//
//        }
    }

}