package com.example.bbb.boundaryLayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;

public class POIFragment extends Fragment {
    private POI poi;
    private TextView title;
    private ImageView imageView;
    private TextView description;

    public POIFragment(POI poi){
        this.poi =poi;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        this.title = (TextView) view.findViewById(R.id.TextViewTitle);
        this.imageView = (ImageView) view.findViewById(R.id.imageViewPOI);
        this.description = (TextView) view.findViewById(R.id.textViewPOI);


        title.setText(poi.POIName);
        description.setText(poi.Description);
        imageView.setImageResource(R.drawable.breda);
        return view;
    }
}
