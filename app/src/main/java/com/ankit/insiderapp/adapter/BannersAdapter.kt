package com.ankit.insiderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankit.insiderapp.R
import com.ankit.insiderapp.databinding.RowItemBinding
import com.ankit.insiderapp.helpers.setImageWithGlide
import com.ankit.insiderapp.model.BannersModel


class BannersAdapter(val bannersModelList: ArrayList<BannersModel>?,
                     var openBannerWebView: (String) -> Unit) : RecyclerView.Adapter<BannersAdapter.BannersViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannersViewHolder {
        mContext = parent.context
        val rowItemBinding: RowItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_item, parent, false
        )
        return BannersViewHolder(rowItemBinding)
    }

    override fun onBindViewHolder(holder: BannersViewHolder, position: Int) {
        var imageUrl = bannersModelList?.get(position)?.horizontalCoverImage
                ?: bannersModelList?.get(position)?.verticalCoverImage

           if (imageUrl != null)
                 holder.itemBinding.imgBanner.setImageWithGlide(imageUrl)
    }

    override fun getItemCount(): Int {
        return bannersModelList?.size ?: 0
    }

    inner class BannersViewHolder(val itemBinding: RowItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemBinding.root.setOnClickListener {
                var url = bannersModelList?.get(adapterPosition)?.mapLink
                if (!url.isNullOrBlank())
                    openBannerWebView(url)
             }
        }
    }
}
