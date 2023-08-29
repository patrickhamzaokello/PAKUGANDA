package com.pkasemer.pakuganda.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.pkasemer.pakuganda.ManualNotes;
import com.pkasemer.pakuganda.MyPosition;
import com.pkasemer.pakuganda.R;


public class Home extends Fragment {


    private static final int PAGE_START = 1;

    LinearLayout viewMap, get_gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewMap = view.findViewById(R.id.viewmapbtn);
        get_gps = view.findViewById(R.id.getgps);

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManualNotes.class);
                getContext().startActivity(intent);

            }
        });

        get_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyPosition.class);
                getContext().startActivity(intent);
            }
        });



        return view;
    }

}