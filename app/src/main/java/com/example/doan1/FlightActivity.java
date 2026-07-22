package com.example.doan1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FlightActivity extends AppCompatActivity {

    private ProgressBar pbLoading;
    private RecyclerView rvFlights;
    private FlightAdapter adapter;
    private TextView tvFlightDate, tvFlightPassengers;
    private TextView tvOrigin, tvDestination;
    private Calendar flightCalendar;
    
    private int adultCount = 1;
    private String cabinClass = "Phổ thông";

    private final String[] cities = {"Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Nha Trang", "Phú Quốc", "Đà Lạt", "Huế", "Hải Phòng", "Cần Thơ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        flightCalendar = Calendar.getInstance();
        flightCalendar.add(Calendar.DAY_OF_YEAR, 7);

        ImageButton btnBack = findViewById(R.id.btnBackFlight);
        btnBack.setOnClickListener(v -> finish());

        pbLoading = findViewById(R.id.pbLoadingFlight);
        rvFlights = findViewById(R.id.rvFlights);
        Button btnSearch = findViewById(R.id.btnSearchFlight);
        
        tvFlightDate = findViewById(R.id.tvFlightDate);
        tvFlightPassengers = findViewById(R.id.tvFlightPassengers);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvDestination = findViewById(R.id.tvDestination);
        
        LinearLayout dateContainer = findViewById(R.id.dateContainerFlight);
        LinearLayout passengerContainer = findViewById(R.id.passengerContainerFlight);
        LinearLayout originContainer = findViewById(R.id.originContainer);
        LinearLayout destinationContainer = findViewById(R.id.destinationContainer);
        ImageButton btnSwapRoute = findViewById(R.id.btnSwapRoute);

        rvFlights.setLayoutManager(new LinearLayoutManager(this));

        updateDateLabel();
        updatePassengerLabel();

        originContainer.setOnClickListener(v -> showCitySelectionDialog(true));
        destinationContainer.setOnClickListener(v -> showCitySelectionDialog(false));
        
        btnSwapRoute.setOnClickListener(v -> {
            String temp = tvOrigin.getText().toString();
            tvOrigin.setText(tvDestination.getText().toString());
            tvDestination.setText(temp);
        });

        dateContainer.setOnClickListener(v -> showDatePicker());
        passengerContainer.setOnClickListener(v -> showPassengerDialog());

        btnSearch.setOnClickListener(v -> performSearch());
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd 'thg' MM", new Locale("vi", "VN"));
        tvFlightDate.setText(sdf.format(flightCalendar.getTime()));
    }

    private void updatePassengerLabel() {
        tvFlightPassengers.setText(adultCount + " người lớn, " + cabinClass);
    }

    private void showCitySelectionDialog(boolean isOrigin) {
        new AlertDialog.Builder(this)
                .setTitle(isOrigin ? "Chọn điểm khởi hành" : "Chọn điểm đến")
                .setItems(cities, (dialog, which) -> {
                    if (isOrigin) {
                        tvOrigin.setText(cities[which]);
                    } else {
                        tvDestination.setText(cities[which]);
                    }
                })
                .show();
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, day) -> {
            flightCalendar.set(Calendar.YEAR, year);
            flightCalendar.set(Calendar.MONTH, month);
            flightCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateLabel();
        }, flightCalendar.get(Calendar.YEAR), flightCalendar.get(Calendar.MONTH), flightCalendar.get(Calendar.DAY_OF_MONTH))
        .show();
    }

    private void showPassengerDialog() {
        String[] options = {"1 người lớn, Phổ thông", "2 người lớn, Phổ thông", "1 người lớn, Thương gia", "2 người lớn, Thương gia"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn số hành khách và hạng ghế")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) { adultCount = 1; cabinClass = "Phổ thông"; }
                    else if (which == 1) { adultCount = 2; cabinClass = "Phổ thông"; }
                    else if (which == 2) { adultCount = 1; cabinClass = "Thương gia"; }
                    else if (which == 3) { adultCount = 2; cabinClass = "Thương gia"; }
                    updatePassengerLabel();
                })
                .show();
    }

    private void performSearch() {
        String origin = tvOrigin.getText().toString();
        String destination = tvDestination.getText().toString();

        if (origin.equals(destination)) {
            Toast.makeText(this, "Điểm đi và điểm đến không được trùng nhau!", Toast.LENGTH_SHORT).show();
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        rvFlights.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            pbLoading.setVisibility(View.GONE);
            rvFlights.setVisibility(View.VISIBLE);

            List<Flight> mockFlights = new ArrayList<>();
            mockFlights.add(new Flight("Vietnam Airlines", "1.250.000đ", "08:00", "10:10", "2h 10m"));
            mockFlights.add(new Flight("VietJet Air", "890.000đ", "11:30", "13:45", "2h 15m"));
            mockFlights.add(new Flight("Bamboo Airways", "1.100.000đ", "15:20", "17:30", "2h 10m"));
            mockFlights.add(new Flight("Vietravel Airlines", "950.000đ", "19:00", "21:15", "2h 15m"));

            adapter = new FlightAdapter(mockFlights);
            rvFlights.setAdapter(adapter);

            Toast.makeText(this, "Tìm chuyến bay từ " + origin + " đi " + destination, Toast.LENGTH_SHORT).show();
        }, 1500);
    }
}
