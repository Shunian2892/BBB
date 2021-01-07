package com.example.bbb.controlLayer.poiRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.IOnItemClickListener;

public class POIViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView POIRVTextviewName;
    TextView POIRVTextviewLatitude;
    TextView POIRVTextviewLongtitude;
    ImageView POIRVImageview;
    private IOnItemClickListener clickListener;

    public POIViewHolder(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView);
        POIRVTextviewName = itemView.findViewById(R.id.poi_list_item_name);
        POIRVTextviewLatitude = itemView.findViewById(R.id.poi_list_item_latitude);
        POIRVTextviewLongtitude = itemView.findViewById(R.id.poi_list_item_longtitude);
        POIRVImageview = itemView.findViewById(R.id.poi_list_item_image);
        clickListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedPos = getAdapterPosition();
        clickListener.OnItemClick(clickedPos);
    }
}
