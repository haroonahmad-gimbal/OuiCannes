package com.gimbal.kotlin.ouicannes.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.databinding.FragmentWalletBinding
import com.gimbal.kotlin.ouicannes.utils.getGimbalVisits
import com.gimbal.kotlin.ouicannes.utils.getPoints
import com.google.android.material.bottomnavigation.BottomNavigationView

class WalletFragment : Fragment() {

    private lateinit var walletViewModel: WalletViewModel
    private var _binding: FragmentWalletBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        walletViewModel =
            ViewModelProvider(this).get(WalletViewModel::class.java)

        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navBar?.visibility = View.VISIBLE

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pointsValueTv.text = getPoints(requireContext()).toString()
        val adapter = GimbalVisitsAdapter()
        binding.visitsRv.addItemDecoration( DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.visitsRv.adapter = adapter
        getGimbalVisits(requireContext())?.let {
            adapter.data = it
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}