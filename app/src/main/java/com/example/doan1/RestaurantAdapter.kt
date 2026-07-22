package com.example.doan1

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class RestaurantAdapter(
    private var restaurants: List<Element>,
    private var userLat: Double? = null,
    private var userLon: Double? = null
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val tvType: TextView = itemView.findViewById(R.id.tvRestaurantType)
        val tvAddress: TextView = itemView.findViewById(R.id.tvRestaurantAddress)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        val tags = restaurant.tags

        holder.tvName.text = tags?.get("name") ?: "Nhà hàng không tên"
        
        val cuisine = tags?.get("cuisine") ?: "Nhà hàng"
        val amenity = tags?.get("amenity") ?: ""
        holder.tvType.text = if (cuisine != "Nhà hàng") cuisine else amenity

        val city = tags?.get("addr:city") ?: ""
        val street = tags?.get("addr:street") ?: ""
        val housenumber = tags?.get("addr:housenumber") ?: ""
        val fullAddress = tags?.get("addr:full") ?: "$housenumber $street $city".trim()
        
        holder.tvAddress.text = if (fullAddress.isNotEmpty()) fullAddress else "Đang cập nhật địa chỉ"

        // Tính toán khoảng cách
        val resLat = restaurant.lat ?: restaurant.center?.lat
        val resLon = restaurant.lon ?: restaurant.center?.lon

        if (userLat != null && userLon != null && resLat != null && resLon != null) {
            val results = FloatArray(1)
            Location.distanceBetween(userLat!!, userLon!!, resLat, resLon, results)
            val distanceInMeters = results[0]

            holder.tvDistance.visibility = View.VISIBLE
            if (distanceInMeters < 1000) {
                holder.tvDistance.text = String.format(Locale.US, "%d m", distanceInMeters.toInt())
            } else {
                holder.tvDistance.text = String.format(Locale.US, "%.1f km", distanceInMeters / 1000.0)
            }
        } else {
            holder.tvDistance.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = restaurants.size

    fun updateData(newRestaurants: List<Element>, lat: Double? = null, lon: Double? = null) {
        this.restaurants = newRestaurants
        this.userLat = lat
        this.userLon = lon
        notifyDataSetChanged()
    }
}
