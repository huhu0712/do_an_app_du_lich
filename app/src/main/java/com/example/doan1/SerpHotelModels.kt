package com.example.doan1

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SerpHotelResponse(
    @SerializedName("properties") val properties: List<HotelProperty>? = null,
    @SerializedName("error") val error: String? = null
) : Serializable

data class HotelProperty(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("link") val link: String? = null,
    @SerializedName("gps_coordinates") val gps: GpsCoords? = null,
    @SerializedName("rate_per_night") val rate: HotelRate? = null,
    @SerializedName("overall_rating") val rating: Double? = null,
    @SerializedName("reviews") val reviewsCount: Int? = null,
    @SerializedName("images") val images: List<HotelImage>? = null,
    @SerializedName("amenities") val amenities: List<String>? = null,
    @SerializedName("check_in_time") val checkInTime: String? = null,
    @SerializedName("check_out_time") val checkOutTime: String? = null
) : Serializable

data class GpsCoords(
    @SerializedName("latitude") val lat: Double,
    @SerializedName("longitude") val lon: Double
) : Serializable

data class HotelRate(
    @SerializedName("lowest") val lowest: String? = null,
    @SerializedName("before_taxes_fees") val beforeTax: String? = null
) : Serializable

data class HotelImage(
    @SerializedName("thumbnail") val thumbnail: String? = null
) : Serializable
