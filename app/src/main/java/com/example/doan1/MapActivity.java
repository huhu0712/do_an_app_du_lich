package com.example.doan1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapActivity extends AppCompatActivity {
    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    private final String MAPTILER_KEY = "5e1sYopkv7DLxzvoB8tq";
    private static final int LOCATION_PERMISSION_REQ_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Thiết lập cấu hình osmdroid
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        
        setContentView(R.layout.activity_map);

        map = findViewById(R.id.mapView);
        map.setMultiTouchControls(true);

        // Cấu hình nguồn bản đồ từ MapTiler
        String mapUrl = "https://api.maptiler.com/maps/streets-v2/256/{z}/{x}/{y}.png?key=" + MAPTILER_KEY;
        
        map.setTileSource(new OnlineTileSourceBase("MapTiler", 1, 20, 256, ".png", new String[]{mapUrl}) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                return getBaseUrl() 
                    .replace("{z}", String.valueOf(MapTileIndex.getZoom(pMapTileIndex)))
                    .replace("{x}", String.valueOf(MapTileIndex.getX(pMapTileIndex)))
                    .replace("{y}", String.valueOf(MapTileIndex.getY(pMapTileIndex)));
            }
        });

        // Lớp phủ vị trí người dùng
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation(); // Tự động đi theo vị trí
        map.getOverlays().add(mLocationOverlay);

        // Kiểm tra quyền
        checkPermissions();

        // Đặt vị trí mặc định tại Hà Nội (nếu chưa lấy được GPS)
        GeoPoint startPoint = new GeoPoint(21.0285, 105.8542);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);

        // Nút quay lại
        ImageButton btnBack = findViewById(R.id.btnBackMap);
        btnBack.setOnClickListener(v -> finish());
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationOverlay.enableMyLocation();
            } else {
                Toast.makeText(this, "Cần quyền vị trí để hiển thị trên bản đồ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        if (mLocationOverlay != null) mLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        if (mLocationOverlay != null) mLocationOverlay.disableMyLocation();
    }
}
