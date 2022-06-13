package com.gimbal.kotlin.ouicannes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gimbal.android.Gimbal
import com.gimbal.kotlin.ouicannes.databinding.ActivityMainBinding

const val PERMISSION_REQUEST_LOCATION = 0
const val PERMISSION_REQUEST_BLUETOOTH = 1
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        actionbar?.hide()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Gimbal.start()
        } else {
            // Permission is missing and must be requested.
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       if(requestCode == PERMISSION_REQUEST_LOCATION){
           if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Permissions Granted", Toast.LENGTH_LONG).show()
               Gimbal.start()
               requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),3)
               requestBluetoothPermissions()

           }

       } else if (requestCode == PERMISSION_REQUEST_BLUETOOTH){
           if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               Toast.makeText(applicationContext," Bluetooth Permissions Granted", Toast.LENGTH_LONG).show()
           }
       }
    }

    fun requestLocationPermissions(){
        if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION )
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION )
        }
    }

    fun requestBluetoothPermissions(){
        if(shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_SCAN) || shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT) ){
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT), PERMISSION_REQUEST_BLUETOOTH )
        } else {
            requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT), PERMISSION_REQUEST_BLUETOOTH)
        }
    }

}