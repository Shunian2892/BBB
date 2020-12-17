package com.example.bbb.boundaryLayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bbb.R;

public class UserInfoFragment extends Fragment {
    private ImageButton ibBack;
    private MapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        ibBack = view.findViewById(R.id.imageButtonBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMapFragment(getActivity().getSupportFragmentManager());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
            }
        });

        return view;
    }

    public void setMapFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
        } else {
            mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        }
    }
}
