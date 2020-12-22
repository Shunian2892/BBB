package com.example.bbb.boundaryLayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private TextView description;
    private ImageButton ibBack;
    private POIListFragment poiListFragment;
    private FragmentManager fragmentManager;

    private VideoFragment videoFragment;
    private ImageFragment imageFragment;

    private Button buttonVideo;
    private boolean isVideo;

    private UIViewModel viewModel;
        this.poi = poi;
        this.replacePOI = replacePOI;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);
        viewModel.setCurrentFragment(R.id.fragment_poi);

        this.title = (TextView) view.findViewById(R.id.TextViewTitle);
        this.description = (TextView) view.findViewById(R.id.textViewPOI);

        isVideo = false;

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

        fragmentManager = getFragmentManager();
        setImageFragment();
        setVideoFragment();

        fragmentManager.beginTransaction().replace(R.id.detail_container,imageFragment).commit();

        buttonVideo = view.findViewById(R.id.buttonVideo);
        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideo) {
                    fragmentManager.beginTransaction().replace(R.id.detail_container,imageFragment).commit();
                    buttonVideo.setText("Show Video");
                }else{
                    fragmentManager.beginTransaction().replace(R.id.detail_container, videoFragment).commit();
                    buttonVideo.setText("Hide Video");
                }
                isVideo = !isVideo;
            }
        });

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

    public void setImageFragment() {
        if (fragmentManager.findFragmentById(R.id.fragment_image) == null) {
            imageFragment = new ImageFragment();
        } else {
            imageFragment = (ImageFragment) fragmentManager.findFragmentById(R.id.fragment_image);
        }

    }
    public void setVideoFragment() {
        if (fragmentManager.findFragmentById(R.id.fragment_video) == null) {
            videoFragment = new VideoFragment();
        } else {
            videoFragment = (VideoFragment) fragmentManager.findFragmentById(R.id.fragment_video);
        }

    }
}
