package com.gimbal.kotlin.ouicannes.ui.login.loginhome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.gimbal.android.Gimbal
import com.gimbal.kotlin.ouicannes.MainActivity
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentLoginHomeBinding
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginDataSource
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.data.LoginRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.concurrent.schedule

class LoginHome : Fragment() {
    private var _binding: FragmentLoginHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginRepository = LoginRepository(LoginDataSource(),context)
        Gimbal.start()
        Timer().schedule(2000){
            activity?.runOnUiThread {
                if(loginRepository.isLoggedIn){
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                  //view.findNavController().navigate(R.id.action_loginHome_to_registerFragment)
                }else{
                    view.findNavController().navigate(R.id.action_loginHome_to_registerFragment)
                }

            }

        }
    }

}