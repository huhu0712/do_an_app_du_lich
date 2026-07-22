package com.example.doan1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface OverpassApiService {

    /**
     * Gửi truy vấn đến Overpass API bằng phương thức POST.
     * Thêm Headers để định danh ứng dụng và yêu cầu kết quả JSON.
     */
    @Headers(
        "User-Agent: DuLichVietApp/1.0",
        "Accept: application/json"
    )
    @FormUrlEncoded
    @POST("api/interpreter")
    fun searchPlaces(
        @Field("data") query: String
    ): Call<OverpassResponse>
}
