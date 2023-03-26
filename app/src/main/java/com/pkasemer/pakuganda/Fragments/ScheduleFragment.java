package com.pkasemer.pakuganda.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pkasemer.pakuganda.Adapters.AllActivitiesAdapter;
import com.pkasemer.pakuganda.Models.ActivitiesBase;
import com.pkasemer.pakuganda.Models.ActivitiesHome;
import com.pkasemer.pakuganda.R;


import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkasemer.pakuganda.Adapters.AllNeedsAdapter;
import com.pkasemer.pakuganda.Apis.APIBase;
import com.pkasemer.pakuganda.Apis.ApiEndPoints;
import com.pkasemer.pakuganda.MalUtils.NetworkStatusIntentService;
import com.pkasemer.pakuganda.MalUtils.PaginationAdapterCallback;
import com.pkasemer.pakuganda.MalUtils.PaginationScrollListener;
import com.pkasemer.pakuganda.Models.NeedsBase;
import com.pkasemer.pakuganda.Models.NoticeHome;
import com.pkasemer.pakuganda.NetworkReceiver;
import com.pkasemer.pakuganda.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScheduleFragment extends Fragment implements com.pkasemer.pakuganda.MalUtils.PaginationAdapterCallback {

    private static final String TAG = "notification";

    AllActivitiesAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    SwipeRefreshLayout swipeRefreshLayout;
    private PaginationScrollListener paginationScrollListener;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private final int selectCategoryId = 3;

    List<ActivitiesHome> activitiesHomes;

    private ApiEndPoints apiEndPoints;


    public ScheduleFragment() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_fragment, container, false);
        rv = view.findViewById(R.id.main_recycler);
        progressBar = view.findViewById(R.id.main_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);
        swipeRefreshLayout = view.findViewById(R.id.main_swiperefresh);

        adapter = new AllActivitiesAdapter(getContext(), this);
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


        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);
        return view;
    }

    private void doRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (callHomeCategories().isExecuted())
            callHomeCategories().cancel();

        adapter.getMovies().clear();
        adapter.notifyDataSetChanged();
        rv.removeOnScrollListener(paginationScrollListener);
        rv.addOnScrollListener(paginationScrollListener);
        loadFirstPage();
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // network register and start background serice for network status
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(new NetworkReceiver(),new IntentFilter(NetworkStatusIntentService.ACTION_NETWORK_STATUS));
        Intent networkIntent = new Intent(getContext(), NetworkStatusIntentService.class);
        getContext().startService(networkIntent);
    }



    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;

        callHomeCategories().enqueue(new Callback<ActivitiesBase>() {
            @Override
            public void onResponse(Call<ActivitiesBase> call, Response<ActivitiesBase> response) {
                hideErrorView();

                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                activitiesHomes = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                if (activitiesHomes.isEmpty()) {
                    showCategoryErrorView();
                    return;
                } else {
                    adapter.addAll(activitiesHomes);
                }


                if (currentPage != TOTAL_PAGES){
                    adapter.addLoadingFooter();
                    isLastPage = false;
                }
                else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ActivitiesBase> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<ActivitiesHome> fetchResults(Response<ActivitiesBase> response) {
        ActivitiesBase activitiesBase = response.body();
        TOTAL_PAGES = activitiesBase.getTotalPages();
        return activitiesBase.getActivitiesHome();
    }


    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        callHomeCategories().enqueue(new Callback<ActivitiesBase>() {
            @Override
            public void onResponse(Call<ActivitiesBase> call, Response<ActivitiesBase> response) {
                Log.i(TAG, "onResponse: " + currentPage
                        + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                adapter.removeLoadingFooter();
                isLoading = false;

                activitiesHomes = fetchResults(response);
                adapter.addAll(activitiesHomes);

                if (currentPage != TOTAL_PAGES){
                    adapter.addLoadingFooter();
                    isLastPage = false;
                }
                else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ActivitiesBase> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }



    private Call<ActivitiesBase> callHomeCategories() {
        return apiEndPoints.getAllActivities(
                currentPage
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

    }


    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
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

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }
}