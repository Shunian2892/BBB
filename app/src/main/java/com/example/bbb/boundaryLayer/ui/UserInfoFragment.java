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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bbb.R;
import com.example.bbb.controlLayer.DatabaseManager;
import com.example.bbb.controlLayer.OnItemClickListener;
import com.example.bbb.controlLayer.userRecyclerView.UserAdapter;
import com.example.bbb.controlLayer.userRecyclerView.UserListManager;
import com.example.bbb.entityLayer.data.POI;
import com.example.bbb.entityLayer.data.WalkedRoute;

import java.util.ArrayList;
import java.util.List;

public class UserInfoFragment extends Fragment implements OnItemClickListener {
    private ImageButton ibBack;
    private TextView listSize;
    private TextView distance;
    private DatabaseManager databaseManager;

    private RecyclerView walkedRoutesRv;
    private List<WalkedRoute> walkedRouteList;
    private UserAdapter userAdapter;
    private ViewGroup container;
    private POIListFragment poiListFragment;
    private UIViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(UIViewModel.class);
        viewModel.setCurrentFragment(R.id.user_info_fragment);

        this.container = container;
        ibBack = view.findViewById(R.id.imageButtonBack);
        listSize = view.findViewById(R.id.textViewValueWalkedRoutes);
        distance = view.findViewById(R.id.textViewValueWalkedDistance);
        databaseManager = DatabaseManager.getInstance(getActivity().getApplicationContext());

        listSize.setText(String.valueOf(databaseManager.getWalkedRoutes().size()));

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(getFragmentManager().getBackStackEntryCount()>0){
                  getFragmentManager().popBackStackImmediate();
              }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        walkedRouteList = new ArrayList<>();
        this.walkedRoutesRv = this.container.findViewById(R.id.RvWalkedRoutes);
        this.userAdapter = new UserAdapter(this, getActivity().getApplicationContext());
        this.walkedRoutesRv.setLayoutManager( new LinearLayoutManager(getActivity().getApplicationContext()));
        this.walkedRoutesRv.setAdapter(this.userAdapter);
    }

    public void setPoiListFragment(FragmentManager fm){
        if(fm.findFragmentById(R.id.fragment_poi_list) == null){
            poiListFragment = new POIListFragment();
        } else {
            poiListFragment = (POIListFragment) fm.findFragmentById(R.id.fragment_poi_list);
        }
        viewModel.setPointOfInterests(testData());
        viewModel.setBackButtonState(true);
    }

    private ArrayList<POI> testData(){
        ArrayList<POI> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            POI poi = new POI();
            poi.ID = i;
            poi.POIName = "name " + i;
            poi.latitude = i;
            poi.longitude = i;
            poi.Description_nl = "Temp Description";
            poi.Description_en = "Temp Description";
            poi.Description_fr = "Temp Description";
            list.add(poi);
        }
        return list;
    }

    @Override
    public void OnItemClick(int clickedPosition) {
        setPoiListFragment(getFragmentManager());
        viewModel.setPointOfInterests(databaseManager.getPOIsFromRoute(databaseManager.getWalkedRoutes().get(clickedPosition).routeID));
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, poiListFragment).addToBackStack(null).commit();
    }
}
