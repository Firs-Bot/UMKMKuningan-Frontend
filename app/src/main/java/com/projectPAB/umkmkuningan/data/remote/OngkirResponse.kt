package com.projectPAB.umkmkuningan.data.remote

import com.google.gson.annotations.SerializedName

// ===== Response Cek Ongkir =====
data class OngkirResponse(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: BinderByteData?
)

data class BinderByteData(
    @SerializedName("results") val results: List<ResultData>?
)

data class ResultData(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("costs") val costs: List<CostDetail>
)

data class CostDetail(
    @SerializedName("service") val service: String,
    @SerializedName("type") val type: String?,
    @SerializedName("price") val price: String,       // format baru binderbyte
    @SerializedName("estimated") val estimated: String // format baru binderbyte
)

// ===== Response Wilayah (API Wilayah) =====
data class WilayahResponse(
    @SerializedName("code") val code: String,
    @SerializedName("messages") val messages: String?,
    @SerializedName("value") val value: List<WilayahItem>?
)

data class WilayahItem(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
) {
    override fun toString(): String {
        return name // supaya Spinner otomatis nampilin nama wilayah
    }
}
