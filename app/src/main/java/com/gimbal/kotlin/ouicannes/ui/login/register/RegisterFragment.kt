package com.gimbal.kotlin.ouicannes.ui.login.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.gimbal.kotlin.ouicannes.MainActivity
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentRegisterBinding
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.ui.login.LoginViewModel
import com.gimbal.kotlin.ouicannes.ui.login.loginscreen.ui.login.LoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(context))
            .get(RegisterViewModel::class.java)
        auth = Firebase.auth
        binding.submitBtn.setOnClickListener {
            registerViewModel.register(binding.emailEt.text.toString(),binding.passwordEt.text.toString(),
                binding.nameEt.text.toString(),binding.companyEt.text.toString(), binding.phonenumberEt.text.toString(),binding.titleEt.text.toString())
        }

        binding.signInTv.setOnClickListener {
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
           // view.findNavController().navigate(R.id.action_registerFragment_to_permissionsFragment)
        }

        registerViewModel.registerResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loginResult.error?.let {
                    Toast.makeText(context,"Registration Failed",Toast.LENGTH_LONG).show()
                }
                loginResult.success?.let {
                    Toast.makeText(context,"Registration Success",Toast.LENGTH_LONG).show()
                    view.findNavController().navigate(R.id.action_registerFragment_to_permissionsFragment)

                }
            })
    }
}