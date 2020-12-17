package com.example.bbb.boundaryLayer.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.controlLayer.userRecyclerView.UserAdapter;
import com.example.bbb.controlLayer.userRecyclerView.UserListManager;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends Fragment implements OnItemClickListener {
    private ImageButton ibBack;
    private TextView listSize;
    private TextView distance;
    private DatabaseManager databaseManager;
    private MapFragment mapFragment;

    private RecyclerView walkedRoutesRv;
    private List<WalkedRoute> walkedRouteList;
    private UserAdapter userAdapter;
    private ViewGroup container;
    private Context context;
    private ReplacePOI replacePOI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        this.container = container;
        ibBack = view.findViewById(R.id.imageButtonBack);
        listSize = view.findViewById(R.id.textViewValueWalkedRoutes);
        distance = view.findViewById(R.id.textViewValueWalkedDistance);
        databaseManager = DatabaseManager.getInstance(getActivity().getApplicationContext());

        listSize.setText(String.valueOf(databaseManager.getWalkedRoutes().size()));


        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMapFragment(getActivity().getSupportFragmentManager());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        walkedRouteList = new ArrayList<>();
        this.walkedRoutesRv = this.container.findViewById(R.id.RvWalkedRoutes);
        this.userAdapter = new UserAdapter(this, this.context);
        this.walkedRoutesRv.setLayoutManager( new LinearLayoutManager(this.context));
        this.walkedRoutesRv.setAdapter(this.userAdapter);
    }

    public void setMapFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.map_fragment) == null){
            mapFragment = new MapFragment();
        } else {
            mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        }
    }

    @Override
    public void OnItemClick(int clickedPosition) {

    }
}
