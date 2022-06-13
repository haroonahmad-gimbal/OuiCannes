package com.gimbal.kotlin.ouicannes.ui.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.GimbalEvent
import com.gimbal.kotlin.ouicannes.databinding.VisitRowBinding
import com.gimbal.kotlin.ouicannes.utils.calculatePoints
import java.util.concurrent.TimeUnit

class GimbalVisitsAdapter : RecyclerView.Adapter<GimbalVisitsAdapter.ViewHolder>() {

    var data = listOf<GimbalEvent>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: VisitRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : GimbalEvent){
            binding.visitDescription.text = binding.root.context.getString(R.string.visits_message,item.placeName,TimeUnit.MILLISECONDS.toMinutes(item.dwellTime))
            binding.pointsValueRowTv.text = calculatePoints(item.dwellTime,item.points).toString()
        }


        companion object{
            fun from(parent : ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VisitRowBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}