package com.gimbal.kotlin.ouicannes.ui.upcomingevents

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.gimbal.kotlin.ouicannes.data.model.UpcomingEvent
import com.gimbal.kotlin.ouicannes.databinding.UpcomingEventsRowBinding

class UpcomingEventsAdapter(val clickListener: UpcomingEventsListener) : RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>() {

    var data = listOf<UpcomingEvent>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener,data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: UpcomingEventsRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: UpcomingEventsListener, item : UpcomingEvent){
            binding.eventTitleTv.text = item.title
            binding.eventTimeTv.text = item.time
            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
        }


        companion object{
            fun from(parent : ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UpcomingEventsRowBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}

class UpcomingEventsListener(val clickListener: (upcomingEvent: UpcomingEvent) -> Unit){
    fun onClick(upcomingEvent: UpcomingEvent) = clickListener(upcomingEvent)
}