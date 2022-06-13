package com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data

import android.content.Context
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.LoggedInUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource, var appContext: Context?) {

    // in-memory cache of the loggedInUser object
    private  var auth = Firebase.auth

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get(){
            val sharedPref = appContext?.getSharedPreferences(
                appContext?.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            return sharedPref?.getBoolean(appContext?.getString(R.string.is_logged_in),false) ?: false
        }

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(user: LoggedInUser): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(user)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        val sharedPref = appContext?.getSharedPreferences(
            appContext?.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with (sharedPref?.edit()) {
            this?.putBoolean(appContext?.getString(R.string.is_logged_in), true)
            this?.putString(appContext?.getString(R.string.user_id),loggedInUser.userId)
            this?.putInt(appContext?.getString(R.string.user_id),loggedInUser.points)
            this?.apply()
        }

        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(loggedInUser.name).build()
        auth.currentUser?.updateProfile(profileUpdates)
    }

}