package org.ieselcaminas.proyectofinal

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthClass {

    companion object {

        fun logIn(mail: String, pass: String): Boolean? {
            val auth = FirebaseAuth.getInstance()
            var returnBool: Boolean? = null

            auth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener() {
                    returnBool = if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        true
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", it.exception)
                        false
                    }
                }

            return returnBool
        }

        fun signIn(mail: String, pass: String): Boolean? {
            val auth = FirebaseAuth.getInstance()
            var returnBool: Boolean? = null
            auth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener() {
                    returnBool = if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        true
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", it.exception)
                        false
                    }
                }

            return returnBool
        }
    }
}