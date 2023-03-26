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
import com.pkasemer.pakuganda.Models.Need;
import com.pkasemer.pakuganda.R;

import java.util.List;

public class SingleVillageDeepwellAdapter extends RecyclerView.Adapter<SingleVillageDeepwellAdapter.ItemViewHolder> {

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
    private final List<DeepWell> deepWellList;


    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();



    public SingleVillageDeepwellAdapter(Context context, List<DeepWell> deepWells) {
        this.context = context;
        this.deepWellList = deepWells;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.village_need_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final DeepWell deepWell = deepWellList.get(position);
         holder.heading.setText(deepWell.getName());
         holder.subheading.setText(deepWell.getDepth());
         holder.description.setText(deepWell.getInChargeName());

    }




    @Override
    public int getItemCount() {
        return deepWellList.size();
    }


}