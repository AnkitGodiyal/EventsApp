package com.ankit.insiderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankit.insiderapp.R
import com.ankit.insiderapp.databinding.LayoutEventItemBinding
import com.ankit.insiderapp.helpers.HelperMethods
import com.ankit.insiderapp.helpers.setImageWithGlide
import com.ankit.insiderapp.model.MasterListEventModel

class EventAdapter(
    private var mEventList: ArrayList<MasterListEventModel>? = null
) : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    private lateinit var mContext: Context
    fun updateList(eventList: ArrayList<MasterListEventModel>?) {
        mEventList = eventList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.MyViewHolder {
        mContext = parent.context
        val layoutEventItemBinding: LayoutEventItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_event_item, parent, false
        )
        return MyViewHolder(layoutEventItemBinding)
    }

    override fun getItemCount(): Int = mEventList?.size ?: 0

    override fun onBindViewHolder(holder: EventAdapter.MyViewHolder, position: Int) {
        val eventData = mEventList?.get(position)
        holder.itemBinding.model = eventData
        holder.itemBinding.eventPrice.text = HelperMethods.withPriceOrFree(eventData?.eventPriceDisplayString)
        holder.itemBinding.eventDate.isSelected = true
        eventData?.horizontalCoverImage?.let {
            holder.itemBinding.eventIv.setImageWithGlide(it)
        }
    }

    inner class MyViewHolder(val itemBinding: LayoutEventItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}