package com.gimbal.kotlin.ouicannes.ui.login.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gimbal.kotlin.ouicannes.OuiCannes
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginDataSource
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginRepository

class RegisterViewModelFactory(val context : Context?) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(), context
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}