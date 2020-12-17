package com.example.bbb.controlLayer.userRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.controlLayer.poiRecyclerView.POIViewHolder;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<WalkedRoute> walkedRouteList;
    private OnItemClickListener listener;
    private UserListManager userListManager;
    public UserAdapter(OnItemClickListener listener, Context context){
        this.listener = listener;
        this.userListManager = new UserListManager(context);
        this.walkedRouteList = this.userListManager.getWalkedRouteList();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_walkedroute_list_item,parent,false);
        return new UserViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        WalkedRoute walkedRoute = this.walkedRouteList.get(position);
        holder.Routename.setText(String.valueOf(walkedRoute.routeID));
        holder.RouteDate.setText(walkedRoute.date);

    }

    @Override
    public int getItemCount() {
        return walkedRouteList.size();
    }
}
