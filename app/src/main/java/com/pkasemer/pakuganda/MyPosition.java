package com.pkasemer.pakuganda;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyPosition extends AppCompatActivity {

    private static final int LOCATION_MIN_UPDATE_TIME = 10;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;
    private static final String TAG = "My Location";

    
    private MapView mapView;
    private GoogleMap googleMap;
    private Location location = null;

    ProgressBar main_progress;
    TextView latitude_cords,longitude_cord;
    
    LinearLayout refresh_gps;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (googleMap != null) {
                drawMarker(location, "Me");
                locationManager.removeUpdates(locationListener);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_position);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.mapView);
        latitude_cords = findViewById(R.id.latitude_cords);
        longitude_cord = findViewById(R.id.longitude_cord);
        refresh_gps = findViewById(R.id.refresh_gps);
        main_progress = findViewById(R.id.main_progress);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapView_onMapReady(googleMap);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        getCurrentLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                try {

                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style));
                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style.", e);
                }


            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }


    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(this, getText(R.string.provider_failed), Toast.LENGTH_LONG).show();
            } else {
                location = null;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
                    drawMarker(location, getText(R.string.i_am_here).toString());

                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void mapView_onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initMap();
        getCurrentLocation();
    }

    private void drawMarker(Location location, String title) {
        if (this.googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.8f));

            latitude_cords.setText("Latitude: " +location.getLatitude());
            longitude_cord.setText("Longitude: " + location.getLongitude());

            refresh_gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MyPosition.this, "Location Refreshed Successfully", Toast.LENGTH_SHORT).show();
                    googleMap.clear();
                    main_progress.setVisibility(View.VISIBLE);
                    initMarker(location.getLatitude(),location.getLongitude());
                }
            });
            main_progress.setVisibility(View.GONE);
        }
    }


    private void initMarker(Double latitude, Double longitude) {
        LatLng location = new LatLng(latitude, longitude);
//
//        Glide.with(this)
//                .applyDefaultRequestOptions(new RequestOptions()
//                        .override(60, 60)
//                        .placeholder(R.drawable.resource_default)
//                        .error(R.drawable.resource_default))
//                .asBitmap()
//                // to be added
//                .load("")
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, 100, 100, false);
//                        googleMap.addMarker(
//                                new MarkerOptions()
//                                        .position(location)
//                                        .zIndex(1)
//                                        .title("ME")
//                                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 18.8f));
        main_progress.setVisibility(View.GONE);
    }


}



