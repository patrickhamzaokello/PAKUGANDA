package com.example.pakuganda.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.pakuganda.MalUtils.PaginationAdapterCallback;
import com.example.pakuganda.Models.Village;
import com.example.pakuganda.R;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class SingleVillageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;


    private List<Village> singleVillageDetailList;
    private final Context context;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private String errorMsg;
    private Village selected_village;


    public SingleVillageAdapter(Context context, Village village) {
        this.context = context;
        this.selected_village = village;

    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.selected_single_village_layout, parent, false);
        RecyclerView.ViewHolder viewHolder = new SingleActivityViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        final SingleActivityViewHolder singleActivityViewHolder = (SingleActivityViewHolder) holder;

        singleActivityViewHolder.heading.setText(selected_village.getName());


    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

















   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */

    protected class SingleActivityViewHolder extends RecyclerView.ViewHolder {
        private final TextView heading;
        private RecyclerView itemRecyclerView;


        public SingleActivityViewHolder(View itemView) {
            super(itemView);
            // init views
            heading = itemView.findViewById(R.id.heading);
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }



}