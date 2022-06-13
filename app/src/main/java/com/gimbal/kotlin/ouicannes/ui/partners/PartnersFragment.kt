package com.gimbal.kotlin.ouicannes.ui.partners

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.Partner
import com.gimbal.kotlin.ouicannes.data.model.UpcomingEvent
import com.gimbal.kotlin.ouicannes.databinding.FragmentPartnersBinding
import com.gimbal.kotlin.ouicannes.ui.upcomingevents.UpcomingEventsAdapter
import com.gimbal.kotlin.ouicannes.ui.upcomingevents.UpcomingEventsListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PartnersFragment : Fragment() {

    private var _binding: FragmentPartnersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.GONE
        _binding = FragmentPartnersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val adapter = PartnersAdapter(PartnersListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(browserIntent)
        })
        binding.partnersRv.addItemDecoration( DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.partnersRv.adapter = adapter

        db.collection("partners")
            .get()
            .addOnSuccessListener { result ->
                adapter.data = toObj(result)
            }
            .addOnFailureListener {
                Log.w("Upcoming Events","Error in getting events", it)
            }
    }

    private fun toObj(result: QuerySnapshot) : List<Partner>{
        var partners = mutableListOf<Partner>()
        for(event in result){
            Log.d("Upcoming Events","${event.id} => ${event.data}")
            partners.add(Partner(event.data["company"].toString(),event.data["image_url"].toString(),event.data["landing_url"].toString()))
        }
        return partners
    }

}