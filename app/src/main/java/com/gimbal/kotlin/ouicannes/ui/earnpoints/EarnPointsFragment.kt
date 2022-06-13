package com.gimbal.kotlin.ouicannes.ui.earnpoints

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentEarnPointsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class EarnPointsFragment : Fragment() {
    private var _binding: FragmentEarnPointsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEarnPointsBinding.inflate(inflater, container, false)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.GONE
        return binding.root
    }
}