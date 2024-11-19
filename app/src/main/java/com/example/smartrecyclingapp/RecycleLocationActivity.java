package com.example.smartrecyclingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class RecycleLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 100;
    private Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_location);
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(v -> {
            Intent intent = new Intent(RecycleLocationActivity.this, danhgiadiadiem.class);
            startActivity(intent);
        });

        // Khởi tạo FusedLocationClient để lấy vị trí
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Tìm SupportMapFragment và khởi tạo bản đồ
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Kiểm tra quyền và lấy vị trí
        if (checkLocationPermission()) {
            getCurrentLocation();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
    }

    private void getCurrentLocation() {
        if (!checkLocationPermission()) {
            Toast.makeText(this, "Quyền vị trí chưa được cấp", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                showNearbyRecyclingCenters(location);
            } else {
                // Nếu không có vị trí, yêu cầu cập nhật vị trí
                fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);  // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        return locationRequest;
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult != null && locationResult.getLocations().size() > 0) {
                Location location = locationResult.getLocations().get(0);
                showNearbyRecyclingCenters(location);
            }
        }
    };

    private void showNearbyRecyclingCenters(Location userLocation) {
        String wasteType = getIntent().getStringExtra("wasteType");
        List<LocationMap> locations = RecyclingData.getLocationsByWasteType(wasteType);

        if (locations.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy địa điểm tái chế phù hợp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm địa điểm gần nhất
        LocationMap nearestLocation = LocationUtils.findNearestLocation(
                userLocation.getLatitude(), userLocation.getLongitude(), locations);

        // Thêm marker cho tất cả các địa điểm
        for (LocationMap location : locations) {
            LatLng latLng = new LatLng(location.latitude, location.longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title(location.name));
        }

        // Di chuyển camera đến địa điểm gần nhất
        if (nearestLocation != null) {
            LatLng nearestLatLng = new LatLng(nearestLocation.latitude, nearestLocation.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestLatLng, 15));
            Toast.makeText(this, "Địa điểm gần nhất: " + nearestLocation.name, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy bỏ cập nhật vị trí khi không cần thiết
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tiếp tục cập nhật vị trí khi cần
        if (checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
        }
    }
}




