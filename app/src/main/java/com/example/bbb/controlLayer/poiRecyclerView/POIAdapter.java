package com.example.bbb.controlLayer.poiRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bbb.R;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.database.Database;

import java.util.ArrayList;
import java.util.List;

public class POIAdapter extends RecyclerView.Adapter<POIViewHolder> {

    private List<POI> poiList;
    private OnItemClickListener listener;
    private Context context;

    public POIAdapter(OnItemClickListener listener, List<POI> poiList, Context context){
        this.listener = listener;
        this.poiList = poiList;
        this.context = context;
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
        holder.POIRVTextviewName.setText(poi.POIName);
        holder.POIRVTextviewLatitude.setText(poi.latitude + "");
        holder.POIRVTextviewLongtitude.setText(poi.longitude + "");
        //holder.POIRVImageview.setImageResource(R.drawable.blindwalls);
        Glide.with(context).load(poi.imageURL).into(holder.POIRVImageview);
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    public void setPoiList(List<POI> poiList){
        this.poiList = poiList;
        this.notifyDataSetChanged();
    }
}
