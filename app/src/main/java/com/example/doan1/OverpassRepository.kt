package com.example.doan1

import android.util.Log
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverpassRepository {

    /**
     * Hàm chung để tìm kiếm các địa điểm theo tags (nhà hàng, khách sạn, v.v.)
     */
    private fun searchPlaces(
        lat: Double,
        lon: Double,
        radius: Int,
        tags: String,
        limit: Int = 50,
        onResult: (List<Element>) -> Unit,
        onError: (String) -> Unit
    ) {
        val query = String.format(
            Locale.US,
            "[out:json][timeout:30];(node[%s](around:%d,%.6f,%.6f);way[%s](around:%d,%.6f,%.6f););out center %d;",
            tags, radius, lat, lon, tags, radius, lat, lon, limit
        )

        Log.d("OverpassAPI", "Request: $query")

        RetrofitClient.instance.searchPlaces(query).enqueue(object : Callback<OverpassResponse> {
            override fun onResponse(call: Call<OverpassResponse>, response: Response<OverpassResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.elements ?: emptyList())
                } else {
                    onError("Lỗi server: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<OverpassResponse>, t: Throwable) {
                onError("Lỗi kết nối: ${t.localizedMessage}")
            }
        })
    }

    fun searchRestaurantsAround(lat: Double, lon: Double, radius: Int = 3000, onResult: (List<Element>) -> Unit, onError: (String) -> Unit) {
        searchPlaces(lat, lon, radius, "\"amenity\"=\"restaurant\"", onResult = onResult, onError = onError)
    }

    fun searchHotelsAround(lat: Double, lon: Double, radius: Int = 5000, onResult: (List<Element>) -> Unit, onError: (String) -> Unit) {
        // Tìm kiếm khách sạn, nhà khách, v.v.
        searchPlaces(lat, lon, radius, "\"tourism\"~\"hotel|guest_house|hostel\"", onResult = onResult, onError = onError)
    }

    fun searchAirportsAround(lat: Double, lon: Double, radius: Int = 50000, onResult: (List<Element>) -> Unit, onError: (String) -> Unit) {
        // Tìm kiếm sân bay (bán kính rộng hơn vì sân bay thường ở xa)
        searchPlaces(lat, lon, radius, "\"aeroway\"=\"aerodrome\"", limit = 5, onResult = onResult, onError = onError)
    }
}
