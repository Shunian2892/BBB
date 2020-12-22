package com.example.bbb.boundaryLayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.bbb.R;
import com.example.bbb.entityLayer.data.POI;

public class POIFragment extends Fragment {
    private POI poi;
    private TextView title;
    private ImageView imageView;
    private TextView description;
    private ImageButton ibBack;
    private POIListFragment poiListFragment;

    private UIViewModel viewModel;

/*    public POIFragment(POI poi, ReplacePOI replacePOI){
        this.poi = poi;
        this.replacePOI = replacePOI;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        viewModel.setCurrentFragment(R.id.fragment_poi);

        this.title = (TextView) view.findViewById(R.id.TextViewTitle);
        this.imageView = (ImageView) view.findViewById(R.id.imageViewPOI);
        this.description = (TextView) view.findViewById(R.id.textViewPOI);

        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        poi = viewModel.getSelectedPOI().getValue();

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
            poiListFragment = new POIListFragment();
        } else {
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
        viewModel.setBackButtonState(false);
//        poiListFragment.setButtonBackVisibility(false);
    }
}
