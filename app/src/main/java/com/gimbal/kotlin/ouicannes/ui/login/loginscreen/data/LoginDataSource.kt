package com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data

import com.gimbal.kotlin.ouicannes.data.model.LoggedInUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    fun login(user: LoggedInUser): Result<LoggedInUser> {
        try {
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

}