package com.ankit.insiderapp.model

data class HomePageModel(
    val popularEventList: ArrayList<MasterListEventModel>?,
    val featuredEventList: ArrayList<MasterListEventModel>?,
    val bannersList: ArrayList<BannersModel>?
)