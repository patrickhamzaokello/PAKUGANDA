package com.pkasemer.pakuganda.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.pakuganda.MalUtils.MapCoordinates;
import com.pkasemer.pakuganda.MalUtils.PaginationAdapterCallback;
import com.pkasemer.pakuganda.Models.SingleVillage;
import com.pkasemer.pakuganda.R;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class SingleVillageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ACTIVITIES = 1;
    private static final int NEEDS = 2;
    private static final int LEADERS = 3;
    private static final int DEEPWELL = 4;
    private static final int OUTREACH = 5;
    private static final int VILLAGE = 0;
    private static final int LOADING = 9;


    private List<SingleVillage> selectedAlbum;
    private final Context context;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private final PaginationAdapterCallback mCallback;
    private String errorMsg;
    private final MapCoordinates mapCoordinatesCallback;

    public SingleVillageAdapter(Context context, PaginationAdapterCallback callback, MapCoordinates mapCoordinates) {
        this.context = context;
        this.mCallback = callback;
        mapCoordinatesCallback = mapCoordinates;
        selectedAlbum = new ArrayList<>();

    }

    public List<SingleVillage> getMovies() {
        return selectedAlbum;
    }

    public void setAlbumList(List<SingleVillage> albumList) {
        this.selectedAlbum = albumList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ACTIVITIES:
            case NEEDS:
            case LEADERS:
            case DEEPWELL:
            case OUTREACH:
                View view_recently_played = inflater.inflate(R.layout.recycle_with_header, parent, false);
                viewHolder = new RecyclerHeaderVH(view_recently_played);
                break;
            case VILLAGE:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.pagination_item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.selected_single_village_layout, parent, false);
        viewHolder = new VillageVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SingleVillage featured = selectedAlbum.get(position); // Movie


        switch (getItemViewType(position)) {
            case ACTIVITIES:
                final RecyclerHeaderVH recyclerHeaderVH = (RecyclerHeaderVH) holder;
                recyclerHeaderVH.heading.setText(featured.getHeading());
                LinearLayoutManager recentTrack = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                recyclerHeaderVH.heading.setText(featured.getHeading());
                recyclerHeaderVH.itemRecyclerView.setLayoutManager(recentTrack);
                recyclerHeaderVH.itemRecyclerView.setClipToPadding(false);
                recyclerHeaderVH.itemRecyclerView.setItemViewCacheSize(3);
                recyclerHeaderVH.itemRecyclerView.setPadding(0, 0, 100, 0);
                SingleVillageActivityAdapter singleVillageActivityAdapter = new SingleVillageActivityAdapter(context,featured.getActivities());
                recyclerHeaderVH.itemRecyclerView.setAdapter(singleVillageActivityAdapter);



                break;
            case NEEDS:

                final RecyclerHeaderVH needs_recyclerHeaderVH = (RecyclerHeaderVH) holder;
                LinearLayoutManager needs_linearlayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                needs_recyclerHeaderVH.heading.setText(featured.getHeading());
                needs_recyclerHeaderVH.itemRecyclerView.setLayoutManager(needs_linearlayout);
                needs_recyclerHeaderVH.itemRecyclerView.setClipToPadding(false);
                needs_recyclerHeaderVH.itemRecyclerView.setItemViewCacheSize(3);
                needs_recyclerHeaderVH.itemRecyclerView.setPadding(0, 0, 100, 0);
                SingleVillageNeedAdapter singleVillageNeedAdapter = new SingleVillageNeedAdapter(context,featured.getNeeds());
                needs_recyclerHeaderVH.itemRecyclerView.setAdapter(singleVillageNeedAdapter);

                break;
            case LEADERS:
                final RecyclerHeaderVH leaders_recyclerHeaderVH = (RecyclerHeaderVH) holder;
                LinearLayoutManager leaders_linearlayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                leaders_recyclerHeaderVH.heading.setText(featured.getHeading());
                leaders_recyclerHeaderVH.itemRecyclerView.setLayoutManager(leaders_linearlayout);
                leaders_recyclerHeaderVH.itemRecyclerView.setClipToPadding(false);
                leaders_recyclerHeaderVH.itemRecyclerView.setItemViewCacheSize(3);
                leaders_recyclerHeaderVH.itemRecyclerView.setPadding(0, 0, 100, 0);
                SingleVillageLeaderAdapter singleVillageLeaderAdapter = new SingleVillageLeaderAdapter(context,featured.getLeaders());
                leaders_recyclerHeaderVH.itemRecyclerView.setAdapter(singleVillageLeaderAdapter);

                break;

            case DEEPWELL:
                final RecyclerHeaderVH deepwell_recyclerHeaderVH = (RecyclerHeaderVH) holder;
                deepwell_recyclerHeaderVH.heading.setText(featured.getHeading());
                LinearLayoutManager deepwell_linearlayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                deepwell_recyclerHeaderVH.heading.setText(featured.getHeading());
                deepwell_recyclerHeaderVH.itemRecyclerView.setLayoutManager(deepwell_linearlayout);
                deepwell_recyclerHeaderVH.itemRecyclerView.setClipToPadding(false);
                deepwell_recyclerHeaderVH.itemRecyclerView.setItemViewCacheSize(3);
                deepwell_recyclerHeaderVH.itemRecyclerView.setPadding(0, 0, 100, 0);
//
                SingleVillageDeepwellAdapter singleVillageDeepwellAdapter = new SingleVillageDeepwellAdapter(context, featured.getDeepWell());
                deepwell_recyclerHeaderVH.itemRecyclerView.setAdapter(singleVillageDeepwellAdapter);

                break;

            case OUTREACH:
                final RecyclerHeaderVH outreach_recyclerHeaderVH = (RecyclerHeaderVH) holder;
                outreach_recyclerHeaderVH.heading.setText(featured.getHeading());
                LinearLayoutManager outreach_linearlayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                outreach_recyclerHeaderVH.heading.setText(featured.getHeading());
                outreach_recyclerHeaderVH.itemRecyclerView.setLayoutManager(outreach_linearlayout);
                outreach_recyclerHeaderVH.itemRecyclerView.setClipToPadding(false);
                outreach_recyclerHeaderVH.itemRecyclerView.setItemViewCacheSize(3);
                outreach_recyclerHeaderVH.itemRecyclerView.setPadding(0, 0, 100, 0);
                SingleVillageOutreachAdapter singleVillageOutreachAdapter = new SingleVillageOutreachAdapter(context,featured.getOutreach());
                outreach_recyclerHeaderVH.itemRecyclerView.setAdapter(singleVillageOutreachAdapter);
                break;

            case VILLAGE:
                final VillageVH villageVH = (VillageVH) holder;
                villageVH.heading.setText(featured.getName());
                villageVH.description.setText(featured.getDescription());
                villageVH.villagecounty.setText(featured.getSubcounty() +", "+ featured.getCounty());
                villageVH.familyno.setText(featured.getFamilies());
                villageVH.populationno.setText(featured.getPopulation());

                mapCoordinatesCallback.plot(featured.getLat(), featured.getLong(), featured.getIconPath(), featured.getName());


                break;


            case LOADING:
//                Do nothing
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    public int getItemCount() {
        return selectedAlbum == null ? 0 : selectedAlbum.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 1) {
            return ACTIVITIES;
        }
        if (position == 2) {
            return NEEDS;
        }
        if (position == 3) {
            return LEADERS;
        }
        if (position == 4) {
            return DEEPWELL;
        }
        if (position == 5) {
            return OUTREACH;
        } else {
            return (position == selectedAlbum.size() - 1 && isLoadingAdded) ?
                    LOADING : VILLAGE;
        }
    }






    /*
   Helpers
   _________________________________________________________________________________________________
    */


    public void add(SingleVillage r) {
        selectedAlbum.add(r);
        notifyItemInserted(selectedAlbum.size() - 1);
    }

    public void addAll(List<SingleVillage> selectedAlbum) {
        for (SingleVillage album : selectedAlbum) {
            add(album);
        }
    }

    public void remove(SingleVillage r) {
        int position = selectedAlbum.indexOf(r);
        if (position > -1) {
            selectedAlbum.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new SingleVillage());
    }


    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = selectedAlbum.size() - 1;
        SingleVillage album = getItem(position);

        if (album != null) {
            selectedAlbum.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(selectedAlbum.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public SingleVillage getItem(int position) {
        return selectedAlbum.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */


    protected static class VillageVH extends RecyclerView.ViewHolder {
        private final TextView heading,description,villagecounty,familyno,populationno;

        public VillageVH(View itemView) {
            super(itemView);
            // init views
            heading = (TextView) itemView.findViewById(R.id.heading);
            description = (TextView) itemView.findViewById(R.id.description);
            villagecounty = (TextView) itemView.findViewById(R.id.villagecounty);
            familyno = (TextView) itemView.findViewById(R.id.familyno);
            populationno = (TextView) itemView.findViewById(R.id.populationno);
        }
    }




    protected static class RecyclerHeaderVH extends RecyclerView.ViewHolder {
        private final RecyclerView itemRecyclerView;
        private final TextView heading;
        private TextView subheading;

        public RecyclerHeaderVH(View itemView) {
            super(itemView);
            // init views
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
            heading = (TextView) itemView.findViewById(R.id.heading);
        }
    }




    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProgressBar mProgressBar;
        private final ImageButton mRetryBtn;
        private final TextView mErrorTxt;
        private final LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }


}