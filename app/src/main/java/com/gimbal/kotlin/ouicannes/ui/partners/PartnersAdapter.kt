package com.gimbal.kotlin.ouicannes.ui.partners

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gimbal.kotlin.ouicannes.data.model.Partner
import com.gimbal.kotlin.ouicannes.databinding.PartnersRowBinding
import com.squareup.picasso.Picasso

class PartnersAdapter(val clickListener: PartnersListener) : RecyclerView.Adapter<PartnersAdapter.ViewHolder>() {

    var data = listOf<Partner>()
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

    class ViewHolder private constructor(private val binding: PartnersRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: PartnersListener, item : Partner){
            binding.partnerNameTv.text = item.companyName
            Picasso.get().load(item.imageUrl).into(binding.partnerImage)
            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
        }


        companion object{
            fun from(parent : ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PartnersRowBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}

class PartnersListener(val clickListener: (partner: Partner) -> Unit){
    fun onClick(partner: Partner) = clickListener(partner)
}