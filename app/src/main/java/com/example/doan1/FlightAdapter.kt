package com.example.doan1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

data class Flight(
    val airline: String,
    val price: String,
    val departure: String,
    val arrival: String,
    val duration: String
)

class FlightAdapter(private val flights: List<Flight>) :
    RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAirline: TextView = itemView.findViewById(R.id.tvAirlineName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvFlightPrice)
        val tvDeparture: TextView = itemView.findViewById(R.id.tvDepartureTime)
        val tvArrival: TextView = itemView.findViewById(R.id.tvArrivalTime)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flight, parent, false)
        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flights[position]
        holder.tvAirline.text = flight.airline
        holder.tvPrice.text = flight.price
        holder.tvDeparture.text = flight.departure
        holder.tvArrival.text = flight.arrival
        holder.tvDuration.text = flight.duration

        holder.itemView.setOnClickListener {
            showPlatformSelection(it.context, flight.airline)
        }
    }

    private fun showPlatformSelection(context: Context, airline: String) {
        val options = arrayOf("Booking.com", "Agoda")
        AlertDialog.Builder(context)
            .setTitle("Chọn nền tảng đặt vé cho $airline")
            .setItems(options) { _, which ->
                val platform = options[which]
                openPlatform(context, platform, airline)
            }
            .show()
    }

    private fun openPlatform(context: Context, platform: String, airline: String) {
        val query = Uri.encode(airline)
        val url = if (platform == "Booking.com") {
            "https://www.booking.com/flights/index.html" // Booking flights landing page
        } else {
            "https://www.agoda.com/vi-vn/flights" // Agoda flights
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = flights.size
}
