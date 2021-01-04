package com.example.bbb.controlLayer.userRecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.controlLayer.poiRecyclerView.POIViewHolder;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.Route;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<WalkedRoute> walkedRouteList;
    private OnItemClickListener listener;
    private UserListManager userListManager;
    private DatabaseManager databaseManager;
    private String currentLanguage;
    private Context context;

    public UserAdapter(OnItemClickListener listener, Context context){
        this.listener = listener;
        this.context = context;
        this.userListManager = new UserListManager(context);
        this.walkedRouteList = this.userListManager.getWalkedRouteList();
        this.databaseManager = DatabaseManager.getInstance(context);

        SharedPreferences prefs = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        currentLanguage = prefs.getString("language", Locale.getDefault().getLanguage());//"No name defined" is the default value.
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
        switch (currentLanguage) {
            case "en":
                holder.Routename.setText(databaseManager.getRoute(walkedRoute.routeID).RouteName_en);
                break;
            case "fr":
                holder.Routename.setText(databaseManager.getRoute(walkedRoute.routeID).RouteName_fr);
                break;
            case "nl":
                holder.Routename.setText(databaseManager.getRoute(walkedRoute.routeID).RouteName_nl);
                break;
        }
        holder.RouteDate.setText(walkedRoute.date);
        holder.RoutePOIAmount.setText(context.getString(R.string.amount_pois) + " " + databaseManager.getPOIsFromRoute(walkedRoute.routeID).size());

    }

    @Override
    public int getItemCount() {
        return walkedRouteList.size();
    }
}
