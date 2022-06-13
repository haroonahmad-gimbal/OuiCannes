package com.gimbal.kotlin.ouicannes.ui.redeempoints


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.Prize
import com.gimbal.kotlin.ouicannes.databinding.PrizeRowBinding
import com.squareup.picasso.Picasso



class PrizesAdapter (val clickListener: PrizesListener) : RecyclerView.Adapter<PrizesAdapter.ViewHolder>() {

    var data = listOf<Prize>()
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

    class ViewHolder private constructor(private val binding: PrizeRowBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: PrizesListener, item : Prize){
            binding.prizeTitleTv.text = item.title
            binding.prizeSubtitleTv.text = item.subtitle
            binding.prizeDescTv.text = item.description
            binding.prizePointsValueTv.text = binding.root.context.getString(R.string.prize_points_string, item.points)
            Picasso.get().load(item.image).into(binding.prizeIv)

            binding.root.setOnClickListener {
                clickListener.onClick(item)
            }
        }


        companion object{
            fun from(parent : ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PrizeRowBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }
}

class PrizesListener(val clickListener: (prize: Prize) -> Unit){
    fun onClick(prize: Prize) = clickListener(prize)
}