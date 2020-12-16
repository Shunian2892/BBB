package com.example.bbb.controlLayer.poiRecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;

import java.util.ArrayList;

public class POIAdapter extends RecyclerView.Adapter<POIViewHolder> {

    private ArrayList<POI> poiList;
    private OnItemClickListener listener;
    public POIAdapter(OnItemClickListener listener, ArrayList<POI> poiList){
        this.listener = listener;
        this.poiList = poiList;

    }

    @NonNull
    @Override
    public POIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_list_item,parent,false);
        return new POIViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull POIViewHolder holder, int position) {
        POI poi = this.poiList.get(position);
        holder.POIRVTextviewName.setText(poi.getName());
        holder.POIRVTextviewLatitude.setText(poi.getLatitude());
        holder.POIRVTextviewLongtitude.setText(poi.getLongtitude());
        holder.POIRVImageview.setImageResource(R.drawable.blindwalls);

    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }
}
