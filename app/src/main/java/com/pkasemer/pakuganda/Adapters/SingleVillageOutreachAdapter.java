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
import com.pkasemer.pakuganda.Models.DeepWell;
import com.pkasemer.pakuganda.Models.Outreach;
import com.pkasemer.pakuganda.R;

import java.util.List;

public class SingleVillageOutreachAdapter extends RecyclerView.Adapter<SingleVillageOutreachAdapter.ItemViewHolder> {

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
    private final List<Outreach> outreachList;


    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();



    public SingleVillageOutreachAdapter(Context context, List<Outreach> outreaches) {
        this.context = context;
        this.outreachList = outreaches;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.village_need_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Outreach outreach = outreachList.get(position);
         holder.heading.setText(outreach.getTitle());
         holder.subheading.setText("Start: "+outreach.getStartDate()+"End: "+ outreach.getEndDate());
         holder.description.setText(outreach.getDescription());

    }




    @Override
    public int getItemCount() {
        return outreachList.size();
    }


}