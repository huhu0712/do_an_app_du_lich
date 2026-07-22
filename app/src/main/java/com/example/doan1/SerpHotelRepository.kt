package com.example.doan1

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SerpHotelRepository {
    private val apiKey = "3086a2912252146e576e8e6a6d9b6421b77a251861b00f03b363cab779428fc6"

    fun searchHotels(
        query: String,
        checkIn: String,
        checkOut: String,
        adults: Int,
        onResult: (List<HotelProperty>) -> Unit,
        onError: (String) -> Unit
    ) {
        SerpApiClient.instance.searchHotels(
            query = query,
            checkIn = checkIn,
            checkOut = checkOut,
            adults = adults,
            apiKey = apiKey
        ).enqueue(object : Callback<SerpHotelResponse> {
            override fun onResponse(call: Call<SerpHotelResponse>, response: Response<SerpHotelResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.error != null) {
                        onError(body.error)
                    } else {
                        onResult(body?.properties ?: emptyList())
                    }
                } else {
                    onError("Lỗi server: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SerpHotelResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.localizedMessage}")
            }
        })
    }
}
