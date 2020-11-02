package com.ankit.insiderapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponseModel(
    @SerializedName("groups")
    @Expose
    val groups : ArrayList<String>?,
    @SerializedName("list")
    @Expose
    val list: EventsList?,
    @SerializedName("popular")
    @Expose
    val popularEventList: ArrayList<MasterListEventModel>?,
    @SerializedName("featured")
    @Expose
    val featuredEventList: ArrayList<MasterListEventModel>?,
    @SerializedName("banners")
    @Expose
    val bannersList: ArrayList<BannersModel>?
)

data class EventsList(
    @SerializedName("masterList")
    @Expose
    val masterList: LinkedHashMap<String, MasterListEventModel>?,
    @SerializedName("groupwiseList")
    @Expose
    val groupList: LinkedHashMap<String, ArrayList<String>>?,
    @SerializedName("categorywiseList")
    @Expose
    val categoryList: LinkedHashMap<String, ArrayList<String>>
)

data class MasterListEventModel(
    @SerializedName("_id")
    @Expose
    val eventId: String?,
    @SerializedName("min_show_start_time")
    @Expose
    val minShowStartTime: Int,
    @SerializedName("name")
    @Expose
    val eventName: String?,
    @SerializedName("type")
    @Expose
    val eventType: String?,
    @SerializedName("slug")
    @Expose
    val eventSlug: String?,
    @SerializedName("horizontal_cover_image")
    @Expose
    val horizontalCoverImage: String?,
    @SerializedName("city")
    @Expose
    val eventCity: String?,
    @SerializedName("venue_id")
    @Expose
    val eventVenueId: String?,
    @SerializedName("venue_name")
    @Expose
    val eventVenueName: String?,
    @SerializedName("venue_date_string")
    @Expose
    val eventVenueDateString: String?,
    @SerializedName("is_rsvp")
    @Expose
    val eventIsRsvp: String?,
    @SerializedName("event_state")
    @Expose
    val eventState: String?,
    @SerializedName("price_display_string")
    @Expose
    val eventPriceDisplayString: String?,
    @SerializedName("communication_strategy")
    @Expose
    val eventCommunicationStrategy: String?,
    @SerializedName("model")
    @Expose
    val eventModel: String?,
    @SerializedName("popularity_score")
    @Expose
    val eventPopularityScore: Double?,
    @SerializedName("min_price")
    @Expose
    val eventMinPrice: Int?,
    @SerializedName("category_id")
    @Expose
    val categoryInfo: CategoryInfo?
)

data class CategoryInfo(
    @SerializedName("_id")
    @Expose
    val cat_id: String?,
    @SerializedName("name")
    @Expose
    val cat_name: String?,
    @SerializedName("icon_img")
    @Expose
    val cat_img: String?
)


data class GroupWiseEventModelList(
    val list:ArrayList<MasterListEventModel>?
)