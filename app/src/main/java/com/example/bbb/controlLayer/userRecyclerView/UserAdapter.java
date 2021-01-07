package com.example.bbb.controlLayer.userRecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.IOnItemClickListener;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private final IOnItemClickListener listener;
    private final UserListManager userListManager;
    private final String currentLanguage;
    private Context context;

    public UserAdapter(IOnItemClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        currentLanguage = prefs.getString("language",
                Locale.getDefault().getLanguage());//"No name defined" is the default value.
        this.userListManager = new UserListManager(currentLanguage);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_walkedroute_list_item, parent, false);
        return new UserViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        WalkedRoute walkedRoute = userListManager.getWalkedRouteList().get(position);
        holder.Routename.setText(userListManager.getRouteLanguage(walkedRoute.routeID));
        holder.RoutePOIAmount.setText(String.valueOf(userListManager.getPOIAmount(walkedRoute).size()));
        holder.RouteDate.setText(walkedRoute.date);
    }

    @Override
    public int getItemCount() {
        return userListManager.getWalkedRouteList().size();
    }
}
