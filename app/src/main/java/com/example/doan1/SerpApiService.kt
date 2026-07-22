package com.example.doan1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SerpApiService {
    @GET("search.json")
    fun searchHotels(
        @Query("engine") engine: String = "google_hotels",
        @Query("q") query: String,
        @Query("check_in_date") checkIn: String,
        @Query("check_out_date") checkOut: String,
        @Query("adults") adults: Int = 2,
        @Query("currency") currency: String = "VND",
        @Query("gl") gl: String = "vn",
        @Query("hl") hl: String = "vi",
        @Query("api_key") apiKey: String
    ): Call<SerpHotelResponse>
}
