package com.example.bbb.boundaryLayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;

public class POIFragment extends Fragment {
    private POI poi;
    private TextView title;
    private ImageView imageView;
    private TextView description;
    private ImageButton ibBack;
    private POIListFragment poiListFragment;
    private ReplacePOI replacePOI;

    public POIFragment(POI poi, ReplacePOI replacePOI){
        this.poi = poi;
        this.replacePOI = replacePOI;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        this.title = (TextView) view.findViewById(R.id.TextViewTitle);
        this.imageView = (ImageView) view.findViewById(R.id.imageViewPOI);
        this.description = (TextView) view.findViewById(R.id.textViewPOI);

        ibBack = view.findViewById(R.id.imageButtonBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(getFragmentManager().getBackStackEntryCount()>0){
                   getFragmentManager().popBackStackImmediate();
               }
            }
        });

        title.setText(poi.POIName);
        description.setText(poi.Description);
        imageView.setImageResource(R.drawable.breda);
        return view;
    }

    public void setPoiListFragment(FragmentManager fm) {
        if (fm.findFragmentById(R.id.fragment_poi_list) == null) {
            poiListFragment = new POIListFragment(getContext(), replacePOI);
        } else {
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
        poiListFragment.setButtonBackVisibility(false);
    }
}
