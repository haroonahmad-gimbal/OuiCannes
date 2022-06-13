package com.gimbal.kotlin.ouicannes.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.gimbal.android.Gimbal
import com.gimbal.kotlin.ouicannes.MainActivity
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentHomeBinding
import com.gimbal.kotlin.ouicannes.utils.getPoints
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val auth = Firebase.auth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.VISIBLE

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        binding.howToTv.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_earnPointsFragment)
        }
        binding.redeemBtn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder =
                NotificationCompat.Builder(requireContext(), NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("You Entered a Place!")
                    .setContentText("Dwell here for 5 minutes to earn 5 points")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(requireContext())) {
                // notificationId is a unique int for each notification that you must define
                notify(0, builder.build())
            }
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_redeemPointsFragment)

        }
        binding.eventsBtn.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_upcomingEventsFragment)
        }

        binding.partnersTv.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_partnersFragment)
        }
        binding.textPoints.text = getPoints(requireContext()).toString()
        Log.d("Gimbal", "Gimbal Started: " + Gimbal.isStarted())
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}