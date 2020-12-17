package com.example.bbb.boundaryLayer.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    public POIListFragment(Context context, ReplacePOI replacePOI) {
        this.context = context;
        this.replacePOI = replacePOI;
        poiList = new ArrayList<>();
    }

    public void setPoiList(List<POI> poiList) {
        this.poiList = poiList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        return inflater.inflate(R.layout.fragment_poi_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.poiRv = this.container.findViewById(R.id.poi_rv);
        this.poiListManager = new POIListManager(this.context);
        if (poiList.size() == 0) {
            this.poiList = this.poiListManager.getPOIList();
        }
        this.poiAdapter = new POIAdapter(this, this.poiList);
        this.poiRv.setLayoutManager(new LinearLayoutManager(this.context));
        this.poiRv.setAdapter(this.poiAdapter);

    }

    @Override
    public void OnItemClick(int clickedPosition) {
        POI poi = poiList.get(clickedPosition);
        replacePOI.setDetailPOI(poi);
    }
}
