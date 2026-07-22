package com.example.doan1

import android.content.Intent
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class HotelAdapter(
    private var hotels: List<HotelProperty>,
    private var userLat: Double? = null,
    private var userLon: Double? = null
) : RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    class HotelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvHotelName)
        val tvType: TextView = itemView.findViewById(R.id.tvHotelType)
        val tvAddress: TextView = itemView.findViewById(R.id.tvHotelAddress)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val ivHotel: ImageView = itemView.findViewById(R.id.ivHotel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hotel, parent, false)
        return HotelViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel = hotels[position]
        val name = hotel.name

        holder.tvName.text = name
        holder.tvType.text = if (hotel.rating != null) "Rating: ${hotel.rating} (${hotel.reviewsCount ?: 0})" else "Chưa có đánh giá"
        holder.tvAddress.text = hotel.rate?.lowest ?: "Xem giá trên chi tiết"
        
        // Hiển thị ảnh
        val imageUrl = hotel.images?.firstOrNull()?.thumbnail
        
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_gallery)
            .into(holder.ivHotel)

        holder.tvDistance.visibility = View.GONE

        // Xử lý click mở màn hình chi tiết
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, HotelDetailActivity::class.java)
            intent.putExtra("hotel_data", hotel)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = hotels.size

    fun updateData(newHotels: List<HotelProperty>, lat: Double? = null, lon: Double? = null) {
        this.hotels = newHotels
        this.userLat = lat
        this.userLon = lon
        notifyDataSetChanged()
    }
}
