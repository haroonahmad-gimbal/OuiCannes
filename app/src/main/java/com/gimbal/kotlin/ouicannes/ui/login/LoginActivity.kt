package com.gimbal.kotlin.ouicannes.ui.login

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.gimbal.kotlin.ouicannes.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.hide()
    }
}
