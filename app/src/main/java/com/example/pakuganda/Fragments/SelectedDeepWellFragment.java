package com.example.pakuganda.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pakuganda.R;
public class SelectedDeepWellFragment extends Fragment {
    private String m_mapFeatureID;
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
        View view = inflater.inflate(R.layout.fragment_selected_deep_well, container, false);
        Log.e("mapFeatureID", m_mapFeatureID);
        return view;
    }
}