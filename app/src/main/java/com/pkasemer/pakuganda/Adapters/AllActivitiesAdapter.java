package com.pkasemer.pakuganda.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.pakuganda.MalUtils.PaginationAdapterCallback;
import com.pkasemer.pakuganda.Models.ActivitiesHome;
import com.pkasemer.pakuganda.Models.NoticeHome;
import com.pkasemer.pakuganda.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class AllActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ARTIST = 1;
    private static final int LOADING = 0;


    private List<ActivitiesHome> activitiesHomes;
    private final Context context;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private final PaginationAdapterCallback mCallback;
    private String errorMsg;


    public AllActivitiesAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        activitiesHomes = new ArrayList<>();

    }

    public List<ActivitiesHome> getMovies() {
        return activitiesHomes;
    }

    public void setAlbumList(List<ActivitiesHome> albumList) {
        this.activitiesHomes = albumList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ARTIST:
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
        View v1 = inflater.inflate(R.layout.needs_layout_design, parent, false);
        viewHolder = new AlbumVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ActivitiesHome searchResult = activitiesHomes.get(position); // Movie
        switch (getItemViewType(position)) {

            case ARTIST:
                final AlbumVH artistVH = (AlbumVH) holder;
                artistVH.needsTitle.setText(searchResult.getTitle());
                artistVH.subheading.setText("Incharge: "+searchResult.getInchargePerson() +"Date: "+searchResult.getDateCarriedout() + " - Village: "+searchResult.getVillage() + " - Due Date: "+searchResult.getNextDueDate()+ " - Date Completed: "+searchResult.getDateCompleted());
                artistVH.description.setText(searchResult.getDescription());
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


    @Override
    public int getItemCount() {
        return activitiesHomes == null ? 0 : activitiesHomes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == activitiesHomes.size() - 1 && isLoadingAdded) ?
                LOADING : ARTIST;
    }







    /*
   Helpers
   _________________________________________________________________________________________________
    */


    public void add(ActivitiesHome r) {
        activitiesHomes.add(r);
        notifyItemInserted(activitiesHomes.size() - 1);
    }

    public void addAll(List<ActivitiesHome> selectedAlbum) {
        for (ActivitiesHome activitiesHome : selectedAlbum) {
            add(activitiesHome);
        }
    }

    public void remove(ActivitiesHome r) {
        int position = activitiesHomes.indexOf(r);
        if (position > -1) {
            activitiesHomes.remove(position);
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
        add(new ActivitiesHome());
    }


    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = activitiesHomes.size() - 1;
        ActivitiesHome noticeHome = getItem(position);

        if (noticeHome != null) {
            activitiesHomes.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(activitiesHomes.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public ActivitiesHome getItem(int position) {
        return activitiesHomes.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */

    protected class AlbumVH extends RecyclerView.ViewHolder {
        private final TextView needsTitle, subheading, description;


        public AlbumVH(View itemView) {
            super(itemView);
            // init views
            needsTitle = itemView.findViewById(R.id.needsTitle);
            subheading = itemView.findViewById(R.id.subheading);
            description = itemView.findViewById(R.id.description);
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