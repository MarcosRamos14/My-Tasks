package com.marcos.mytasks.framework.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.marcos.mytasks.R

class FirebaseHelper {

    companion object {
        fun getDatabase() = FirebaseDatabase.getInstance().reference

        private fun getAuth() = FirebaseAuth.getInstance()

        fun getIdUser() = getAuth().uid

        fun validError(error: String) : Int {
            return when {
                error.contains("There is no user record corresponding to this identifier") -> {
                    R.string.account_not_registered_register_fragment
                }
                error.contains("The email address is badly formatted") -> {
                    R.string.invalid_email_register_fragment
                }
                error.contains("The password is invalid or the user does not have a password") -> {
                    R.string.invalid_password_register_fragment
                }
                error.contains("The email address is already in user by another account") -> {
                    R.string.email_in_use_register_fragment
                }
                error.contains("Password should be at least 6 characters") -> {
                    R.string.strong_password_register_fragment
                }
                else -> {
                    R.string.generic_error
                }
            }
        }

        fun validSuccess() : Int {
            return R.string.app_message_recover
        }
    }
}