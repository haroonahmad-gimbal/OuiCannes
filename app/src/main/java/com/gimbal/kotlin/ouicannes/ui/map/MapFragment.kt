package com.gimbal.kotlin.ouicannes.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var map : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.VISIBLE
        binding.mapView.getMapAsync {
            map = it
            map.uiSettings.isMyLocationButtonEnabled = true
            //map.isMyLocationEnabled = true
        }
//        val textView: TextView = binding.textDashboard
//        mapViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}