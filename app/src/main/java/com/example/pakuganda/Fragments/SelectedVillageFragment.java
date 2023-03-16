package com.example.pakuganda.Fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pakuganda.Adapters.SingleVillageAdapter;
import com.example.pakuganda.Apis.APIBase;
import com.example.pakuganda.Apis.ApiEndPoints;
import com.example.pakuganda.MalUtils.NetworkStatusIntentService;
import com.example.pakuganda.MalUtils.PaginationScrollListener;
import com.example.pakuganda.Models.SingleVillage;
import com.example.pakuganda.Models.Village;
import com.example.pakuganda.NetworkReceiver;
import com.example.pakuganda.R;

import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectedVillageFragment extends Fragment {

    private String m_mapFeatureID;
    private static final String TAG = "selectedVillage";

    SingleVillageAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    private ApiEndPoints apiEndPoints;

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
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        //init service and load data
        apiEndPoints = APIBase.getClient(getContext()).create(ApiEndPoints.class);


        loadFirstPage();

        btnRetry.setOnClickListener(v -> loadFirstPage());

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(new NetworkReceiver(),new IntentFilter(NetworkStatusIntentService.ACTION_NETWORK_STATUS));
        Intent networkIntent = new Intent(getContext(), NetworkStatusIntentService.class);
        getContext().startService(networkIntent);
    }




    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();

        callHomeCategories().enqueue(new Callback<SingleVillage>() {
            @Override
            public void onResponse(Call<SingleVillage> call, Response<SingleVillage> response) {
                hideErrorView();

                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                Village village = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                if (village != null) {
                    adapter = new SingleVillageAdapter(getContext(), village);
                    rv.setAdapter(adapter);
                } else {
                    showCategoryErrorView();
                    return;
                }


            }

            @Override
            public void onFailure(Call<SingleVillage> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private Village fetchResults(Response<SingleVillage> response) {
        Village village = null;
        SingleVillage singleVillage = response.body();
        if (singleVillage.getVillage() == null) {
            village = null;
        } else {
            village = singleVillage.getVillage();
        }
        return village;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private Call<SingleVillage> callHomeCategories() {
        return apiEndPoints.getSingleVillage(
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
}