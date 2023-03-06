package com.example.pakuganda.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.pakuganda.Adapters.TypeAdapter;
import com.example.pakuganda.Apis.APIBase;
import com.example.pakuganda.Apis.ApiEndPoints;
import com.example.pakuganda.MalUtils.FilterCallBack;
import com.example.pakuganda.Models.HomeFeed;
import com.example.pakuganda.Models.Infrastructure;
import com.example.pakuganda.Models.Type;
import com.example.pakuganda.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapFragment extends Fragment implements FilterCallBack {

    private static final int LOCATION_MIN_UPDATE_TIME = 10;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;
    private static final String TAG = "HomeFragment";


    private MapView mapView;
    private GoogleMap googleMap;
    private Location location = null;


    private static final int PAGE_START = 1;

    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private ApiEndPoints apiEndPoints;
    List<Infrastructure> all_infrastructures;
    List<Type> all_typeList;
    ArrayList<String> filtered_tags;
    ArrayList<Infrastructure> filtered_infrastructures;


    Button infrastructure_filter;
    ProgressBar main_progress;


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (googleMap != null && isAdded()) {
                drawMarker(location, getContext().getText(R.string.i_am_here).toString());
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

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Retrieve saved state information for your fragment here
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        main_progress = view.findViewById(R.id.main_progress);
        apiEndPoints = APIBase.getClient(getContext()).create(ApiEndPoints.class);
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        initView(view, savedInstanceState);
        infrastructure_filter = view.findViewById(R.id.infrastructure_filter);
        infrastructure_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFilter(MapFragment.this);
            }
        });
        loadFirstPage();
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
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
        mapView.onSaveInstanceState(outState);
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
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

//                create satelite view
//                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                try {

                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.style));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style.", e);
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }


    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getContext(), getText(R.string.provider_failed), Toast.LENGTH_LONG).show();
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
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
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
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.8f));

        }
    }


    private void loadFirstPage() {

        currentPage = PAGE_START;

        callInfrastructure().enqueue(new Callback<HomeFeed>() {
            @Override
            public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {

                List<Infrastructure> infrastructures = fetchResults(response);
                if (infrastructures != null && !infrastructures.isEmpty()) {
                    all_infrastructures = infrastructures;
                    initMarker(all_infrastructures);
                } else {
                    main_progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error Fetching Data",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<HomeFeed> call, Throwable t) {
                t.printStackTrace();
                Log.i("onFailure", String.valueOf(t));

            }
        });
    }

    private void initMarker(List<Infrastructure> listData) {

        if (listData != null) {
            for (int i = 0; i < listData.size(); i++) {
                LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLatitude()), Double.parseDouble(listData.get(i).getLongitude()));

                int finalI = i;
                Glide.with(this)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .override(60, 60)
                                .placeholder(R.drawable.ic_dashicons_trash)
                                .error(R.drawable.ic_dashicons_trash))
                        .asBitmap()
                        .load(listData.get(i).getIconpath())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                googleMap.addMarker(
                                        new MarkerOptions()
                                                .position(location)
                                                .zIndex(finalI)
                                                .title(listData.get(finalI).getType())
                                                .icon(BitmapDescriptorFactory.fromBitmap(resource)));
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });


            }
            LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLatitude()), Double.parseDouble(listData.get(0).getLongitude()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 15.0f));

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    int markerId = (int) marker.getZIndex();
                    showDialog(listData.get(markerId));
                    return false;
                }
            });

            main_progress.setVisibility(View.GONE);
        }

    }


    private BitmapDescriptor bitmapDescriptor(Context context, int resourceId) {
        Drawable vectordrawable = ContextCompat.getDrawable(context, resourceId);
        vectordrawable.setBounds(0, 0, vectordrawable.getIntrinsicWidth(), vectordrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectordrawable.getIntrinsicWidth(), vectordrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectordrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void showDialog(Infrastructure infrastructure) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_show_round_dialog);

        TextView type_text = dialog.findViewById(R.id.type_text);
        TextView aim_lable = dialog.findViewById(R.id.aim_lable);
        TextView latittude = dialog.findViewById(R.id.latittude);
        TextView longitude = dialog.findViewById(R.id.longitude);
        TextView waste_infras_desc = dialog.findViewById(R.id.waste_infras_desc);

        type_text.setText(infrastructure.getType());
        aim_lable.setText(infrastructure.getAim());
        latittude.setText("Lat: " + infrastructure.getLatitude());
        longitude.setText("Long: " + infrastructure.getLongitude());
        waste_infras_desc.setText(infrastructure.getDescription());


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private void showDialogFilter(FilterCallBack mcallback) {
        final Dialog dialog = new Dialog(getContext());
        filtered_tags = new ArrayList<>();
        filtered_infrastructures = new ArrayList<>();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_show_infras_filters);
        if (all_typeList != null) {
            TypeAdapter typeAdapter = new TypeAdapter(getContext(), (ArrayList<Type>) all_typeList, mcallback);
            Log.e(TAG, "showDialogFilter: " + all_typeList);
            ListView listView = (ListView) dialog.findViewById(R.id.list_view_1);
            listView.setAdapter(typeAdapter);
        }

        MaterialButton cancel = dialog.findViewById(R.id.cancel);
        MaterialButton apply = dialog.findViewById(R.id.apply_filter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtered_infrastructures != null && !(filtered_infrastructures.isEmpty())) {
                    googleMap.clear();
                    initMarker(filtered_infrastructures);
                } else {
                    Toast.makeText(getContext(), "No filter Applied", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    @Override
    public void filterCallback(String name) {
        if (filtered_tags.contains(name)) {
            filtered_tags.remove(name);
        } else {
            filtered_tags.add(name);
        }

        for (int i = 0; i < all_infrastructures.size(); i++) {
            if (filtered_tags.contains(all_infrastructures.get(i).getType())) {
                filtered_infrastructures.add(all_infrastructures.get(i));
            }

        }
    }


    private List<Infrastructure> fetchResults(Response<HomeFeed> response) {
        HomeFeed homeFeed = response.body();
        if (homeFeed != null) {
            TOTAL_PAGES = homeFeed.getTotalPages();
            all_typeList = homeFeed.getTypes();
            System.out.println("total pages" + TOTAL_PAGES);
            return homeFeed.getInfrastructure();
        } else {
            return null;
        }

    }


    private Call<HomeFeed> callInfrastructure() {
        return apiEndPoints.getAllInfrastructure(
                currentPage
        );
    }
}