package com.ankit.insiderapp.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankit.insiderapp.databinding.LayoutLocationItemBinding

class LocationAdapter(
    private val cities: List<String>,
    private val onItemClickedListener: OnItemClickedListener
) :
    RecyclerView.Adapter<LocationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: LayoutLocationItemBinding =
            LayoutLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemBinding.txtLocation.text = cities[position].capitalize()
    }

    inner class MyViewHolder(val itemBinding: LayoutLocationItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemBinding.txtLocation.setOnClickListener {
                if (adapterPosition != -1) {
                    onItemClickedListener.onItemClicked(cities[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun onItemClicked(value: String)
    }

}

