package com.example.doan1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.Locale;

public class HotelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        HotelProperty hotel = (HotelProperty) getIntent().getSerializableExtra("hotel_data");
        if (hotel == null) {
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(hotel.getName());

        ImageView ivHotel = findViewById(R.id.ivHotelLarge);
        TextView tvName = findViewById(R.id.tvHotelNameDetail);
        TextView tvRating = findViewById(R.id.tvRatingDetail);
        TextView tvType = findViewById(R.id.tvHotelTypeDetail);
        TextView tvDescription = findViewById(R.id.tvDescriptionDetail);
        TextView tvLocation = findViewById(R.id.tvLocationDetail);
        TextView tvPrice = findViewById(R.id.tvPriceDetail);
        ChipGroup cgAmenities = findViewById(R.id.cgAmenities);
        Button btnBook = findViewById(R.id.btnBookNow);

        tvName.setText(hotel.getName());
        
        if (hotel.getRating() != null) {
            tvRating.setText(String.format(Locale.getDefault(), "%.1f (%d đánh giá)", 
                hotel.getRating(), hotel.getReviewsCount() != null ? hotel.getReviewsCount() : 0));
        } else {
            tvRating.setText("Chưa có đánh giá");
        }

        tvType.setText(hotel.getType() != null ? hotel.getType() : "Khách sạn");
        
        String desc = hotel.getDescription();
        if (desc == null || desc.isEmpty()) {
            desc = "Khám phá không gian nghỉ dưỡng tuyệt vời tại " + hotel.getName() + ". Khách sạn cung cấp các dịch vụ chất lượng cao và không gian thoải mái cho kỳ nghỉ của bạn.";
        }
        tvDescription.setText(desc);

        // Hiển thị vị trí (Nếu API không có addr trực tiếp, ta có thể dùng tọa độ hoặc để mặc định)
        tvLocation.setText("Tọa độ: " + (hotel.getGps() != null ? 
            String.format(Locale.US, "%.4f, %.4f", hotel.getGps().getLat(), hotel.getGps().getLon()) : "Đang cập nhật"));

        tvPrice.setText(hotel.getRate() != null && hotel.getRate().getLowest() != null ? hotel.getRate().getLowest() : "Liên hệ để biết giá");

        // Hiển thị tiện nghi
        if (hotel.getAmenities() != null) {
            cgAmenities.removeAllViews();
            for (String amenity : hotel.getAmenities()) {
                Chip chip = new Chip(this);
                chip.setText(amenity);
                chip.setChipBackgroundColorResource(android.R.color.white);
                chip.setChipStrokeWidth(1f);
                chip.setChipStrokeColorResource(android.R.color.darker_gray);
                cgAmenities.addView(chip);
            }
        }

        String imageUrl = hotel.getImages() != null && !hotel.getImages().isEmpty() ? hotel.getImages().get(0).getThumbnail() : null;
        Glide.with(this)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(ivHotel);

        btnBook.setOnClickListener(v -> {
            String query = Uri.encode(hotel.getName());
            String url = "https://www.booking.com/searchresults.html?ss=" + query;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
