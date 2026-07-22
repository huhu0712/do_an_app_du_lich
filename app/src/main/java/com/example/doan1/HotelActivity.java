package com.example.doan1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import kotlin.Unit;

public class HotelActivity extends AppCompatActivity {

    private TextView tvCheckInDate, tvCheckOutDate;
    private EditText etLocation;
    private TextView tvRooms, tvAdults;
    private TextView tvOvernight, tvDayUse;
    private ProgressBar pbLoading;
    private RecyclerView rvHotels;
    private HotelAdapter hotelAdapter;
    private final SerpHotelRepository serpHotelRepository = new SerpHotelRepository();
    private Calendar checkInCalendar, checkOutCalendar;

    private int roomCount = 1;
    private int adultCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        checkInCalendar = Calendar.getInstance();
        checkInCalendar.add(Calendar.DAY_OF_YEAR, 1);
        checkOutCalendar = Calendar.getInstance();
        checkOutCalendar.add(Calendar.DAY_OF_YEAR, 2);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnSearch = findViewById(R.id.btnSearchHotel);
        LinearLayout checkInContainer = findViewById(R.id.checkInContainer);
        LinearLayout checkOutContainer = findViewById(R.id.checkOutContainer);
        
        tvCheckInDate = findViewById(R.id.tvCheckInDate);
        tvCheckOutDate = findViewById(R.id.tvCheckOutDate);
        etLocation = findViewById(R.id.etLocation);
        tvRooms = findViewById(R.id.tvRooms);
        tvAdults = findViewById(R.id.tvAdults);
        tvOvernight = findViewById(R.id.tvOvernight);
        tvDayUse = findViewById(R.id.tvDayUse);
        pbLoading = findViewById(R.id.pbLoading);
        rvHotels = findViewById(R.id.rvHotels);

        ImageButton btnMinusRooms = findViewById(R.id.btnMinusRooms);
        ImageButton btnPlusRooms = findViewById(R.id.btnPlusRooms);
        ImageButton btnMinusAdults = findViewById(R.id.btnMinusAdults);
        ImageButton btnPlusAdults = findViewById(R.id.btnPlusAdults);

        hotelAdapter = new HotelAdapter(new ArrayList<>(), null, null);
        rvHotels.setLayoutManager(new LinearLayoutManager(this));
        rvHotels.setAdapter(hotelAdapter);

        updateDateLabels();
        updateGuestLabels();

        btnBack.setOnClickListener(v -> finish());
        tvOvernight.setOnClickListener(v -> selectTab(true));
        tvDayUse.setOnClickListener(v -> selectTab(false));
        checkInContainer.setOnClickListener(v -> showDatePicker(true));
        checkOutContainer.setOnClickListener(v -> showDatePicker(false));

        btnMinusRooms.setOnClickListener(v -> { if (roomCount > 1) { roomCount--; updateGuestLabels(); } });
        btnPlusRooms.setOnClickListener(v -> { roomCount++; updateGuestLabels(); });
        btnMinusAdults.setOnClickListener(v -> { if (adultCount > 1) { adultCount--; updateGuestLabels(); } });
        btnPlusAdults.setOnClickListener(v -> { adultCount++; updateGuestLabels(); });

        btnSearch.setOnClickListener(v -> performSearch());

        ImageButton btnOpenMap = findViewById(R.id.btnOpenMap);
        btnOpenMap.setOnClickListener(v -> startActivity(new Intent(this, MapActivity.class)));
    }

    private void updateDateLabels() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvCheckInDate.setText(displayFormat.format(checkInCalendar.getTime()));
        tvCheckOutDate.setText(displayFormat.format(checkOutCalendar.getTime()));
    }

    private void updateGuestLabels() {
        tvRooms.setText(String.valueOf(roomCount));
        tvAdults.setText(String.valueOf(adultCount));
    }

    private void performSearch() {
        String locationText = etLocation.getText().toString().trim();

        if (locationText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa điểm!", Toast.LENGTH_SHORT).show();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String checkIn = apiFormat.format(checkInCalendar.getTime());
        String checkOut = apiFormat.format(checkOutCalendar.getTime());

        serpHotelRepository.searchHotels(locationText, checkIn, checkOut, adultCount,
            hotelList -> {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (hotelList.isEmpty()) {
                        Toast.makeText(this, "Không tìm thấy khách sạn nào!", Toast.LENGTH_LONG).show();
                    } else {
                        hotelAdapter.updateData(hotelList, null, null);
                    }
                });
                return Unit.INSTANCE;
            },
            errorMessage -> {
                runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
                });
                return Unit.INSTANCE;
            }
        );
    }

    private void selectTab(boolean overnight) {
        LinearLayout checkOutContainer = findViewById(R.id.checkOutContainer);
        if (overnight) {
            tvOvernight.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
            tvDayUse.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            checkOutContainer.setVisibility(android.view.View.VISIBLE);
        } else {
            tvDayUse.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
            tvOvernight.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            checkOutContainer.setVisibility(android.view.View.GONE);
        }
    }

    private void showDatePicker(boolean isCheckIn) {
        Calendar activeCalendar = isCheckIn ? checkInCalendar : checkOutCalendar;
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    activeCalendar.set(Calendar.YEAR, year);
                    activeCalendar.set(Calendar.MONTH, month);
                    activeCalendar.set(Calendar.DAY_OF_MONTH, day);
                    updateDateLabels();
                }, activeCalendar.get(Calendar.YEAR), activeCalendar.get(Calendar.MONTH), activeCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
