package com.gimbal.kotlin.ouicannes.ui.login

import android.Manifest
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.gimbal.kotlin.ouicannes.MainActivity
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentLoginHomeBinding
import com.gimbal.kotlin.ouicannes.databinding.FragmentPermissionsBinding

class PermissionsFragment : Fragment() {
    private var _binding: FragmentPermissionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPermissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.continueTv.setOnClickListener {

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestMultiplePermissions?.launch(arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT))
            }
            else{
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth?.launch(enableBtIntent)
            }

        }


    }

    private var requestBluetooth = activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }else{
            //deny
        }
    }

    private val requestMultiplePermissions =
        activity?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

}