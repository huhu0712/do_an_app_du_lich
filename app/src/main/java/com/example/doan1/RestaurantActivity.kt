package com.example.doan1

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RestaurantActivity : AppCompatActivity() {

    private val overpassRepository = OverpassRepository()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var tvDiningDate: TextView
    private lateinit var tvDiningTime: TextView
    private lateinit var tvPeople: TextView
    private val diningCalendar = Calendar.getInstance()
    private var peopleCount = 2

    companion object {
        private const val LOCATION_PERMISSION_REQ_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val rvRestaurants = findViewById<RecyclerView>(R.id.rvRestaurants)
        val pbLoading = findViewById<ProgressBar>(R.id.pbLoading)
        val etLocation = findViewById<EditText>(R.id.etRestaurantLocation)
        val btnSearch = findViewById<Button>(R.id.btnSearchRestaurant)
        val btnOpenMap = findViewById<ImageButton>(R.id.btnOpenMap)
        val ivMyLocation = findViewById<ImageView>(R.id.ivMyLocation)

        tvDiningDate = findViewById(R.id.tvDiningDate)
        tvDiningTime = findViewById(R.id.tvDiningTime)
        tvPeople = findViewById(R.id.tvPeople)
        val dateContainer = findViewById<LinearLayout>(R.id.dateContainer)
        val timeContainer = findViewById<LinearLayout>(R.id.timeContainer)
        val btnMinusPeople = findViewById<ImageButton>(R.id.btnMinusPeople)
        val btnPlusPeople = findViewById<ImageButton>(R.id.btnPlusPeople)

        restaurantAdapter = RestaurantAdapter(emptyList())
        rvRestaurants.layoutManager = LinearLayoutManager(this)
        rvRestaurants.adapter = restaurantAdapter

        updateDateLabel()
        updateTimeLabel()
        updatePeopleLabel()

        dateContainer.setOnClickListener { showDatePicker() }
        timeContainer.setOnClickListener { showTimePicker() }
        btnMinusPeople.setOnClickListener { if (peopleCount > 1) { peopleCount--; updatePeopleLabel() } }
        btnPlusPeople.setOnClickListener { peopleCount++; updatePeopleLabel() }

        btnOpenMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        ivMyLocation.setOnClickListener {
            checkLocationPermissionAndGetLocation(pbLoading)
        }

        btnSearch.setOnClickListener {
            val queryText = etLocation.text.toString().trim()

            if (queryText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa điểm! Đang tìm ở Hà Nội.", Toast.LENGTH_SHORT).show()
                pbLoading.visibility = View.VISIBLE
                searchRestaurants(21.028511, 105.804817, pbLoading)
                return@setOnClickListener
            }

            pbLoading.visibility = View.VISIBLE
            Toast.makeText(this, "Đang định vị: $queryText", Toast.LENGTH_SHORT).show()

            Thread {
                try {
                    val geocoder = Geocoder(this)
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocationName(queryText, 1)

                    runOnUiThread {
                        if (addresses != null && addresses.isNotEmpty()) {
                            val address = addresses[0]
                            searchRestaurants(address.latitude, address.longitude, pbLoading)
                        } else {
                            Toast.makeText(this, "Không tìm thấy địa điểm này! Mặc định tìm ở Hà Nội.", Toast.LENGTH_SHORT).show()
                            searchRestaurants(21.028511, 105.804817, pbLoading)
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Log.e("GeocoderError", "Lỗi: ${e.message}")
                        Toast.makeText(this, "Lỗi khi định vị. Mặc định tìm ở Hà Nội.", Toast.LENGTH_SHORT).show()
                        searchRestaurants(21.028511, 105.804817, pbLoading)
                    }
                }
            }.start()
        }
    }

    private fun updateDateLabel() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        tvDiningDate.text = sdf.format(diningCalendar.time)
    }

    private fun updateTimeLabel() {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        tvDiningTime.text = sdf.format(diningCalendar.time)
    }

    private fun updatePeopleLabel() {
        tvPeople.text = peopleCount.toString()
    }

    private fun showDatePicker() {
        DatePickerDialog(this, { _, year, month, day ->
            diningCalendar.set(Calendar.YEAR, year)
            diningCalendar.set(Calendar.MONTH, month)
            diningCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateDateLabel()
        }, diningCalendar.get(Calendar.YEAR), diningCalendar.get(Calendar.MONTH), diningCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(this, { _, hour, minute ->
            diningCalendar.set(Calendar.HOUR_OF_DAY, hour)
            diningCalendar.set(Calendar.MINUTE, minute)
            updateTimeLabel()
        }, diningCalendar.get(Calendar.HOUR_OF_DAY), diningCalendar.get(Calendar.MINUTE), true).show()
    }

    private fun checkLocationPermissionAndGetLocation(pbLoading: ProgressBar) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
            return
        }

        pbLoading.visibility = View.VISIBLE
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Toast.makeText(this, "Đang tìm quanh vị trí hiện tại của bạn", Toast.LENGTH_SHORT).show()
                searchRestaurants(location.latitude, location.longitude, pbLoading)
            } else {
                pbLoading.visibility = View.GONE
                Toast.makeText(this, "Không thể lấy vị trí hiện tại. Vui lòng bật GPS.", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            pbLoading.visibility = View.GONE
            Toast.makeText(this, "Lỗi khi lấy vị trí: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền vị trí!", Toast.LENGTH_SHORT).show()
                val pbLoading = findViewById<ProgressBar>(R.id.pbLoading)
                checkLocationPermissionAndGetLocation(pbLoading)
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền vị trí để sử dụng tính năng này.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun searchRestaurants(lat: Double, lon: Double, pbLoading: ProgressBar) {
        val radiusInMeters = 3000
        overpassRepository.searchRestaurantsAround(
            lat = lat,
            lon = lon,
            radius = radiusInMeters,
            onResult = { restaurantList ->
                runOnUiThread {
                    pbLoading.visibility = View.GONE
                    if (restaurantList.isEmpty()) {
                        Toast.makeText(this, "Không tìm thấy nhà hàng nào quanh đây!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Tìm thấy ${restaurantList.size} nhà hàng!", Toast.LENGTH_SHORT).show()
                        restaurantAdapter.updateData(restaurantList, lat, lon)
                    }
                }
            },
            onError = { errorMessage ->
                runOnUiThread {
                    pbLoading.visibility = View.GONE
                    Toast.makeText(this, "Lỗi: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}
