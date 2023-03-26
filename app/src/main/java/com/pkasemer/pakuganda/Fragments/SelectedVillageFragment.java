package com.pkasemer.pakuganda.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pkasemer.pakuganda.Adapters.SingleVillageAdapter;
import com.pkasemer.pakuganda.Apis.APIBase;
import com.pkasemer.pakuganda.Apis.ApiEndPoints;
import com.pkasemer.pakuganda.MalUtils.MapCoordinates;
import com.pkasemer.pakuganda.MalUtils.PaginationAdapterCallback;
import com.pkasemer.pakuganda.MalUtils.PaginationScrollListener;
import com.pkasemer.pakuganda.Models.SelectedVillage;
import com.pkasemer.pakuganda.Models.SingleVillage;
import com.pkasemer.pakuganda.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectedVillageFragment extends Fragment implements PaginationAdapterCallback, MapCoordinates {

    private String m_mapFeatureID;
    private static final String TAG = "selectedVillage";

    SingleVillageAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static int TOTAL_PAGES;
    private int currentPage = PAGE_START;

    List<SingleVillage> singleVillageList;
    PaginationScrollListener paginationScrollListener;
    private ApiEndPoints apiEndPoints;

    public SelectedVillageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_mapFeatureID = getArguments().getString("mapFeatureID");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_village, container, false);
        rv = view.findViewById(R.id.main_recycler);
        progressBar = view.findViewById(R.id.main_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);
        adapter = new SingleVillageAdapter(getContext(), this,this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        paginationScrollListener = new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
        rv.addOnScrollListener(paginationScrollListener);

        //init service and load data
        apiEndPoints = APIBase.getClient(getContext()).create(ApiEndPoints.class);
        loadFirstPage();
        btnRetry.setOnClickListener(v -> loadFirstPage());


        return view;
    }




    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callHomeCategories().enqueue(new Callback<SelectedVillage>() {
            @Override
            public void onResponse(Call<SelectedVillage> call, Response<SelectedVillage> response) {
                hideErrorView();

                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                singleVillageList = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                if (singleVillageList.isEmpty()) {
                    showCategoryErrorView();
                    return;
                } else {
                    adapter.addAll(singleVillageList);
                }

                if (currentPage < TOTAL_PAGES) {
                    adapter.addLoadingFooter();
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<SelectedVillage> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callHomeCategories().enqueue(new Callback<SelectedVillage>() {
            @Override
            public void onResponse(Call<SelectedVillage> call, Response<SelectedVillage> response) {
                Log.i(TAG, "onResponse: " + currentPage
                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                adapter.removeLoadingFooter();
                isLoading = false;

                singleVillageList = fetchResults(response);
                adapter.addAll(singleVillageList);

                if (currentPage != TOTAL_PAGES) {
                    adapter.addLoadingFooter();
                    isLastPage = false;
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<SelectedVillage> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }


    private List<SingleVillage> fetchResults(Response<SelectedVillage> response) {
        SelectedVillage selectedVillage = response.body();
        TOTAL_PAGES = selectedVillage.getTotalPages();
        return selectedVillage.getSingleVillage();
    }


    private Call<SelectedVillage> callHomeCategories() {
        return apiEndPoints.getSelectedVillage(
                Integer.parseInt(m_mapFeatureID)
        );
    }


    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showCategoryErrorView() {

        progressBar.setVisibility(View.GONE);

        Toast.makeText(getContext(), "Error Fetching Results", Toast.LENGTH_SHORT).show();


    }


    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getContext().getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getContext().getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getContext().getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void plot(String latitude, String longitude, String icon_path, String Village_name) {

    }
}