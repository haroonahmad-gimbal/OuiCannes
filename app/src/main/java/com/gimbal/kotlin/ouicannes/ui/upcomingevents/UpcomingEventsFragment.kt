package com.gimbal.kotlin.ouicannes.ui.upcomingevents

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.UpcomingEvent
import com.gimbal.kotlin.ouicannes.databinding.FragmentUpcomingEventsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [UpcomingEventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpcomingEventsFragment : Fragment() {

    private var _binding: FragmentUpcomingEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.GONE
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val adapter = UpcomingEventsAdapter(UpcomingEventsListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(browserIntent)
        })
        binding.upcomingEventsRv.addItemDecoration( DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.upcomingEventsRv.adapter = adapter
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                adapter.data = toObj(result)
            }
            .addOnFailureListener {
                Log.w("Upcoming Events","Error in getting events", it)
            }
    }

    private fun toObj(result: QuerySnapshot) : List<UpcomingEvent>{
        var upcomingEvents = mutableListOf<UpcomingEvent>()
        for(event in result){
            Log.d("Upcoming Events","${event.id} => ${event.data}")
            upcomingEvents.add(UpcomingEvent(event.id,event.data["title"].toString(),event.data["time"].toString(),event.data["url"].toString()))
        }
        return upcomingEvents
    }
}