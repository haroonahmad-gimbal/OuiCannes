package com.gimbal.kotlin.ouicannes.ui.login.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.LoggedInUser
import com.gimbal.kotlin.ouicannes.data.model.toMap
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginRepository
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.ui.login.LoggedInUserView
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.ui.login.LoginResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val auth = Firebase.auth
    private val _registerResult = MutableLiveData<LoginResult>()
    val registerResult: LiveData<LoginResult> = _registerResult
    val db = Firebase.firestore

    fun register(email: String, password: String, name: String, company: String, phone: String, title: String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if (it.isSuccessful){
                val firebaseUser = auth.currentUser
                firebaseUser?.let { fbUser ->
                    val user = LoggedInUser(fbUser.uid, name, company, phone, 0, title)
                    loginRepository.login(user)
                    db.collection("users").document(user.userId)
                        .set(user.toMap())
                        .addOnSuccessListener {
                            Log.d("Register User","User Registered")
                        }
                }

                _registerResult.value =
                    LoginResult(success = auth.currentUser?.email?.let { it1 -> LoggedInUserView(displayName = it1) })
                Log.d("Register", firebaseUser?.email, it.exception)

            }else{
                _registerResult.value = LoginResult(error = R.string.login_failed)
                Log.d("Register", "Failure", it.exception)
            }
        }
    }
}