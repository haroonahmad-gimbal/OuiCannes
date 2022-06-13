package com.gimbal.kotlin.ouicannes.ui.login.loginscreen.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.LoggedInUser
import com.gimbal.kotlin.ouicannes.data.model.Partner
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginRepository
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private  var auth = Firebase.auth
    val db = Firebase.firestore

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        auth.signInWithEmailAndPassword(username,password).addOnCompleteListener { it ->
            if(it.isSuccessful){
                auth.currentUser?.let { user ->
                    db.collection("users").document(user.uid)
                        .get()
                        .addOnSuccessListener {
                            loginRepository.login(toUserObj(it))
                        }
                        .addOnFailureListener {
                            Log.w("Upcoming Events","Error in getting events", it)
                        }
                }
                _loginResult.value =
                    LoginResult(success = auth.currentUser?.email?.let { it1 -> LoggedInUserView(displayName = it1) })
            }else{
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun toUserObj(result: DocumentSnapshot) : LoggedInUser{
        return LoggedInUser(result.data?.get("uid").toString(), result.data?.get("name").toString(), result.data?.get("company").toString(),
        result.data?.get("phone").toString(),result.data?.get("points").toString().toInt(),result.data?.get("title").toString())
    }
}