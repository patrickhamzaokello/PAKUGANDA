package com.pkasemer.pakuganda.Adapters;


import android.content.Context;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.pakuganda.Models.ActivityModel;
import com.pkasemer.pakuganda.R;


import java.util.List;

public class SingleVillageActivityAdapter extends RecyclerView.Adapter<SingleVillageActivityAdapter.ItemViewHolder> {

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView heading,subheading,description;

        View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            subheading = itemView.findViewById(R.id.subheading);
            description = itemView.findViewById(R.id.description);
            view = itemView;


        }
    }

    private final Context context;
    private final List<ActivityModel> activities;


    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();



    public SingleVillageActivityAdapter(Context context, List<ActivityModel> activityList) {
        this.context = context;
        this.activities = activityList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.village_activity_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ActivityModel activity = activities.get(position);
         holder.heading.setText(activity.getTitle());
         holder.subheading.setText(activity.getInchargePerson() + ", Date Carried out:"+activity.getDateCarriedout());
         holder.description.setText(activity.getDescription());

    }




    @Override
    public int getItemCount() {
        return activities.size();
    }


}