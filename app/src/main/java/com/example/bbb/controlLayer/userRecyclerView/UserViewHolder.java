package com.example.bbb.controlLayer.userRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.OnItemClickListener;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final OnItemClickListener clickListener;
    TextView Routename;
    TextView RouteDate;
    TextView RoutePOIAmount;

    public UserViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        Routename = itemView.findViewById(R.id.userRoute_list_item_name);
        RouteDate = itemView.findViewById(R.id.userRoute_list_item_Date);
        RoutePOIAmount = itemView.findViewById(R.id.userRoute_list_item_AmountPOIint);
        clickListener = listener;
//        itemView.setOnClickListener(this);
        itemView.setOnClickListener(view -> listener.OnItemClick(getAdapterPosition()));
    }

    @Override
    public void onClick(View view) {
        int clickedPos = getAdapterPosition();
        clickListener.OnItemClick(clickedPos);
    }
}
