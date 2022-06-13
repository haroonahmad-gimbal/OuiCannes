package com.gimbal.kotlin.ouicannes.ui.redeempoints

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.Partner
import com.gimbal.kotlin.ouicannes.data.model.Prize
import com.gimbal.kotlin.ouicannes.databinding.FragmentPartnersBinding
import com.gimbal.kotlin.ouicannes.databinding.FragmentRedeemPointsBinding
import com.gimbal.kotlin.ouicannes.ui.partners.PartnersAdapter
import com.gimbal.kotlin.ouicannes.ui.partners.PartnersListener
import com.gimbal.kotlin.ouicannes.utils.addPoints
import com.gimbal.kotlin.ouicannes.utils.getPoints
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RedeemPointsFragment : Fragment() {
    private var _binding: FragmentRedeemPointsBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.GONE
        _binding = FragmentRedeemPointsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PrizesAdapter(PrizesListener {
            if(getPoints(requireContext()) >= it.points.toInt()){
                showConfirmDialog(it)
            } else {
                Toast.makeText(context,"You do not have enough points to redeem this prize!",Toast.LENGTH_LONG).show()
            }
        })
        binding.pointsTv.text = getString(R.string.points_amount, getPoints(requireContext()))
        binding.prizesRv.addItemDecoration( DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.prizesRv.adapter = adapter

        db.collection("prizes")
            .get()
            .addOnSuccessListener { result ->
                adapter.data = toObj(result)
            }
            .addOnFailureListener {
                Log.w("Upcoming Events","Error in getting events", it)
            }
    }

    private fun toObj(result: QuerySnapshot) : List<Prize>{
        var partners = mutableListOf<Prize>()
        for(event in result){
            Log.d("Upcoming Events","${event.id} => ${event.data}")
            partners.add(Prize(event.id,event.data["title"].toString(),event.data["subtitle"].toString(),event.data["body"].toString(),event.data["url"].toString(),
                event.data["points"].toString(),event.data["image_url"].toString()))
        }
        return partners
    }

    private fun showConfirmDialog(prize : Prize){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Are you sure you want to redeem this prize?")
            .setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                db.collection("redemptions").document()
                    .set(hashMapOf("prize_name" to prize.title, "uid" to auth.currentUser?.uid))
                    .addOnSuccessListener {
                        showRedeemedDialog()
                        addPoints(requireContext(),-prize.points.toInt())
                    }
            })
            .setNegativeButton("No",DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
            } )
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun showRedeemedDialog(){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage( "You have redeemed a prize. Look out for an email from Infillion on your prize.")
            .setPositiveButton("OK",DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Congratulations!")
        alert.show()
    }
}