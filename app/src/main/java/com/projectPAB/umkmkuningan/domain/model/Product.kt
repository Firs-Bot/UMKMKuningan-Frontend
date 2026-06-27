package com.projectPAB.umkmkuningan.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import com.google.gson.annotations.SerializedName

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    @SerializedName("shop_name")
    val shopName: String? = null,
    val description: String? = null,
    val phone: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("category")
    val category: String? = "Kuliner"
) : Parcelable
