package com.gimbal.kotlin.ouicannes.ui.login.loginscreen.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gimbal.kotlin.ouicannes.OuiCannes
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginDataSource
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory( val context : Context?) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(), context
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}