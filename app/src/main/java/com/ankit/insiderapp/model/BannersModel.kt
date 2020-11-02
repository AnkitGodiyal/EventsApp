package com.ankit.insiderapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BannersModel(
        @SerializedName("_id")
        @Expose
        var id: String? = null,

        @SerializedName("is_internal")
        @Expose
        var isInternal: Boolean? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("category_id")
        @Expose
        var categoryId: CategoryId? = null,

        @SerializedName("group_id")
        @Expose
        var groupId: GroupId? = null,

        @SerializedName("map_link")
        @Expose
        var mapLink: String? = null,

        @SerializedName("vertical_cover_image")
        @Expose
        var verticalCoverImage: String? = null,

        @SerializedName("priority")
        @Expose
        var priority: Int? = null,

        @SerializedName("display_details")
        @Expose
        var displayDetails: DisplayDetails? = null,

        @SerializedName("description")
        @Expose
        var description: String? = null,

        @SerializedName("horizontal_cover_image")
        @Expose
        var horizontalCoverImage: String? = null
)

data class CategoryId(
        @SerializedName("_id")
        @Expose
        var id: String? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("slug")
        @Expose
        var slug: String? = null
)


data class DisplayDetails(
        @SerializedName("link_type")
        @Expose
        var linkType: String? = null,

        @SerializedName("link_slug")
        @Expose
        var linkSlug: String? = null
)


data class GroupId(
        @SerializedName("_id")
        @Expose
        var id: String? = null,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("slug")
        @Expose
        var slug: String? = null
)

