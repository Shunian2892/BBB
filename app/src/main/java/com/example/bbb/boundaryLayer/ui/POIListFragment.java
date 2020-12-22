package com.example.bbb.boundaryLayer.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.controlLayer.poiRecyclerView.POIAdapter;
import com.example.bbb.controlLayer.poiRecyclerView.POIListManager;
import com.example.bbb.entityLayer.data.POI;

import java.util.ArrayList;
import java.util.List;

public class POIListFragment extends Fragment implements OnItemClickListener {
    private RecyclerView poiRv;
    private List<POI> poiList;
    private POIAdapter poiAdapter;
    private POIListManager poiListManager;
    private ViewGroup container;
    private Context context;
    private ReplacePOI replacePOI;
    private Boolean isBackButtonVisible;
    private UIViewModel viewModel;

//    public void setPoiList(List<POI> poiList) {
//        this.poiList = poiList;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        View view = inflater.inflate(R.layout.fragment_poi_list, container, false);
        ImageButton buttonBack = view.findViewById(R.id.imageButtonBackPOIList);

        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);

        if (viewModel.getBackButtonState().getValue()) {
            buttonBack.setVisibility(View.VISIBLE);
        } else {
            buttonBack.setVisibility(View.GONE);
        }
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager().getBackStackEntryCount()>0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
        poiList = new ArrayList<>();



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.poiRv = this.container.findViewById(R.id.poi_rv);
        this.poiListManager = new POIListManager(getActivity().getApplicationContext());
        if (poiList.size() == 0) {
            this.poiList = this.poiListManager.getPOIList();
//            viewModel.setPointOfInterests(poiList);
        }
        this.poiAdapter = new POIAdapter(this, this.poiList);
        this.poiRv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        this.poiRv.setAdapter(this.poiAdapter);

    }

    @Override
    public void OnItemClick(int clickedPosition) {
        POI poi = poiList.get(clickedPosition);
        viewModel.setSelectedPOI(poi);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new POIFragment()).addToBackStack(null).commit();

    }

//    public void setButtonBackVisibility(boolean state) {
//        isBackButtonVisible = state;
//    }
}
